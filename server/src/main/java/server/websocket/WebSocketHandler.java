package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exception.UnauthorizedException;
import model.GameData;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import com.google.gson.Gson;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final HashMap<Integer, ConnectionManager> connectionManagerHashMap = new HashMap<>();
    UserDAO userDAO;
    GameDAO gameDAO;
    AuthDAO authDAO;


    public WebSocketHandler(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO)
    {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception{
        try {
            UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);

            // Throws a custom UnauthorizedException. Yours may work differently.
            String username = getUsername(command.getAuthToken());

            saveSession(command.getGameID(), session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command);
                case MAKE_MOVE -> makeMove(username, new Gson().fromJson(msg, MakeMoveCommand.class));
                case LEAVE -> leaveGame(username, command);
                case RESIGN -> resign(username, command);
            }
        } catch (UnauthorizedException ex) {
            // Serializes and sends the error message
            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
        }
    }

    private void sendMessage(RemoteEndpoint remote, ErrorMessage error) throws Exception
    {
        String message = new Gson().toJson(error);
        remote.sendString(message);
    }

    private String getUsername(String authToken) throws UnauthorizedException {
        try
        {
            return authDAO.getAuth(authToken).username();
        }
        catch(DataAccessException e)
        {
            throw new UnauthorizedException(500, e.getMessage());
        }
    }

    private void saveSession(int gameID, Session session)
    {
        if (!connectionManagerHashMap.containsKey(gameID))
        {
            connectionManagerHashMap.put(gameID, new ConnectionManager());
        }
    }

    private void connect(Session session, String username, UserGameCommand command) throws UnauthorizedException
    {
        try {
            if (connectionManagerHashMap.containsKey(command.getGameID()))
            {
                connectionManagerHashMap.get(command.getGameID()).add(username, session);
            }
            else
            {
                connectionManagerHashMap.put(command.getGameID(), new ConnectionManager());
                connectionManagerHashMap.get(command.getGameID()).add(username, session);
            }

            GameData game = gameDAO.getGame(command.getGameID());

            String message = new Gson().toJson(new LoadGameMessage(game.game()));
            session.getRemote().sendString(message);

            var notification = getNotificationMessage(username, game);
            connectionManagerHashMap.get(command.getGameID()).broadcast(username, notification);
        } catch (IOException | DataAccessException e) {
            throw new UnauthorizedException(500, e.getMessage());
        }
    }

    private static NotificationMessage getNotificationMessage(String username, GameData game) {
        var message = String.format("%s has joined the game as an observer.", username);
        if (Objects.equals(game.whiteUsername(), username))
        {
            message = String.format("%s has joined the game as the white team.", username);
        }
        else if (Objects.equals(game.blackUsername(), username))
        {
            message = String.format("%s has joined the game as the black team.", username);
        }
        return new NotificationMessage(message);
    }

    private void makeMove(String username, MakeMoveCommand command) throws UnauthorizedException
    {
        var startPos = command.getMove().getStartPosition();
        var startPosString = getPosition(startPos);

        var endPos = command.getMove().getEndPosition();
        var endPosString = getPosition(endPos);

        try
        {
            int gameID = command.getGameID();
            GameData currentGameModel = gameDAO.getGame(gameID);
            ChessGame currentGame = currentGameModel.game();

            if (currentGame.getGameOver())
            {
                throw new UnauthorizedException(500, "This Game is Over. No Moves Can Be Made.");
            }

            ChessMove move = command.getMove();
            if (currentGame.getBoard().getPiece(move.getStartPosition()) == null)
            {
                throw new UnauthorizedException(500, "Cannot move a null chess piece.");
            }
            ChessGame.TeamColor pieceColor = currentGame.getBoard().getPiece(move.getStartPosition()).getTeamColor();

            if (Objects.equals(currentGameModel.whiteUsername(), username))
            {
                if (pieceColor != ChessGame.TeamColor.WHITE)
                {
                    throw new UnauthorizedException(500, "Cannot Move Opposing Team's Piece.");
                }
            }
            else if (Objects.equals(currentGameModel.blackUsername(), username))
            {
                if (pieceColor != ChessGame.TeamColor.BLACK)
                {
                    throw new UnauthorizedException(500, "Cannot Move Opposing Team's Piece.");
                }
            }
            else
            {
                throw new UnauthorizedException(500, "Observer cannot participate in the game.");
            }

            ChessGame.TeamColor teamColor = getTeamColor(username, currentGame, currentGameModel);
            if (currentGame.getBoard().getPiece(move.getStartPosition()).getTeamColor() != teamColor)
            {
                throw new UnauthorizedException(500, "Cannot Move Opposing Team's Piece.");
            }

            currentGame.makeMove(move);
            GameData gameModel  = new GameData(currentGameModel.gameID(), currentGameModel.whiteUsername(),
                    currentGameModel.blackUsername(), currentGameModel.gameName(), currentGame);
            gameDAO.updateGame(gameModel);

            loadMessage(gameModel, command);

            var message = String.format("%s moved from %s to %s.", username, startPosString, endPosString);
            var notification = new NotificationMessage(message);
            connectionManagerHashMap.get(command.getGameID()).broadcast(username, notification);

            determineCheckCheckmateStalemate(command);
        }
        catch (IOException | DataAccessException | InvalidMoveException e)
        {
            throw new UnauthorizedException(500, e.getMessage());
        }

    }

    private static ChessGame.TeamColor getTeamColor(String username, ChessGame currentGame, GameData currentGameModel) throws UnauthorizedException {
        ChessGame.TeamColor teamColor = currentGame.getTeamTurn();
        if (Objects.equals(currentGameModel.whiteUsername(), username) &&
                !Objects.equals(teamColor, ChessGame.TeamColor.WHITE))
        {
            throw new UnauthorizedException(500, "Black Team's turn to move.");
        }
        else if (Objects.equals(currentGameModel.blackUsername(), username) &&
                !Objects.equals(teamColor, ChessGame.TeamColor.BLACK))
        {
            throw new UnauthorizedException(500, "White Team's turn to move.");
        }
        return teamColor;
    }

    private void leaveGame(String username, UserGameCommand command) throws UnauthorizedException
    {
        try
        {
            int gameID = command.getGameID();
            GameData currentGameModel = gameDAO.getGame(gameID);
            ChessGame currentGame = currentGameModel.game();
            GameData gameModel;

            if (Objects.equals(username, currentGameModel.blackUsername()))
            {
                 gameModel = new GameData(currentGameModel.gameID(), currentGameModel.whiteUsername(),
                        null, currentGameModel.gameName(), currentGame);
                gameDAO.updateGame(gameModel);
            }
            else if (Objects.equals(username, currentGameModel.whiteUsername()))
            {
                gameModel  = new GameData(currentGameModel.gameID(), null,
                        currentGameModel.blackUsername(), currentGameModel.gameName(), currentGame);
                gameDAO.updateGame(gameModel);
            }
            connectionManagerHashMap.get(command.getGameID()).remove(username);


            var message = String.format("%s has left the game", username);
            var notification = new NotificationMessage(message);
            connectionManagerHashMap.get(command.getGameID()).broadcast(username, notification);
        }
        catch (IOException | DataAccessException e)
        {
            throw new UnauthorizedException(500, e.getMessage());
        }
    }

    private void resign(String username, UserGameCommand command) throws UnauthorizedException
    {
        try
        {
            GameData currentGameModel = gameDAO.getGame(command.getGameID());
            if (!Objects.equals(currentGameModel.whiteUsername(), username) &&
                    !Objects.equals(currentGameModel.blackUsername(), username))
            {
                throw new UnauthorizedException(500, "Observer cannot resign from the game.");
            }

            ChessGame currentGame = currentGameModel.game();
            if (currentGame.getGameOver())
            {
                throw new UnauthorizedException(500, "Cannot resign from game twice.");
            }

            currentGame.setGameOver(true);
            GameData gameModel  = new GameData(currentGameModel.gameID(), currentGameModel.whiteUsername(),
                    currentGameModel.blackUsername(), currentGameModel.gameName(), currentGame);
            gameDAO.updateGame(gameModel);

            var message = String.format("%s has resigned from the game", username);
            var notification = new NotificationMessage(message);
            connectionManagerHashMap.get(command.getGameID()).broadcast(null, notification);
        }
        catch (IOException | DataAccessException e)
        {
            throw new UnauthorizedException(500, e.getMessage());
        }
    }

    private String getPosition(ChessPosition position)
    {
        String column = " ";
        String row = " ";
        switch (position.getColumn())
        {
            case 1-> column = "a";
            case 2-> column = "b";
            case 3-> column = "c";
            case 4-> column = "d";
            case 5-> column = "e";
            case 6-> column = "f";
            case 7-> column = "g";
            case 8-> column = "h";
        }

        switch (position.getRow())
        {
            case 1-> row = "1";
            case 2-> row = "2";
            case 3-> row = "3";
            case 4-> row = "4";
            case 5-> row = "5";
            case 6-> row = "6";
            case 7-> row = "7";
            case 8-> row = "8";
        }
        return column + row;
    }

    private void loadMessage(GameData gameModel, MakeMoveCommand command) throws IOException {
        LoadGameMessage newGame = new LoadGameMessage(gameModel.game());
        connectionManagerHashMap.get(command.getGameID()).broadcast(null, newGame);
    }

    private void determineCheckCheckmateStalemate(MakeMoveCommand command) throws DataAccessException, IOException {
        String message;
        NotificationMessage notification;
        var daoGame = gameDAO.getGame(command.getGameID());
        if (daoGame.game().isInCheckmate(ChessGame.TeamColor.WHITE))
        {
            message = String.format("%s is in checkmate, and loses the game.", daoGame.whiteUsername());
            notification = new NotificationMessage(message);
            connectionManagerHashMap.get(command.getGameID()).broadcast(null, notification);
        }
        else if (daoGame.game().isInCheck(ChessGame.TeamColor.WHITE))
        {
            message = String.format("%s is in check.", daoGame.whiteUsername());
            notification = new NotificationMessage(message);
            connectionManagerHashMap.get(command.getGameID()).broadcast(null, notification);
        }
        else if (daoGame.game().isInCheckmate(ChessGame.TeamColor.BLACK))
        {
            message = String.format("%s is in checkmate, and loses the game", daoGame.blackUsername());
            notification = new NotificationMessage(message);
            connectionManagerHashMap.get(command.getGameID()).broadcast(null, notification);
        }
        else if (daoGame.game().isInCheck(ChessGame.TeamColor.BLACK))
        {
            message = String.format("%s is in check.", daoGame.blackUsername());
            notification = new NotificationMessage(message);
            connectionManagerHashMap.get(command.getGameID()).broadcast(null, notification);
        }
        else if (daoGame.game().isInStalemate(ChessGame.TeamColor.WHITE))
        {
            message = String.format("No moves available for %s. Game ends in a stalemate.",
                    daoGame.whiteUsername());
            notification = new NotificationMessage(message);
            connectionManagerHashMap.get(command.getGameID()).broadcast(null, notification);
        }
        else if (daoGame.game().isInStalemate(ChessGame.TeamColor.BLACK))
        {
            message = String.format("No moves available for %s. Game ends in a stalemate.",
                    daoGame.blackUsername());
            notification = new NotificationMessage(message);
            connectionManagerHashMap.get(command.getGameID()).broadcast(null, notification);
        }
    }
}
