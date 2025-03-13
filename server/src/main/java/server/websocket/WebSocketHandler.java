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
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.ClearService;
import service.GameService;
import service.UserService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private final ConnectionManager connections = new ConnectionManager();
    private final HashMap<Integer, Session> gameSession = new HashMap<>();
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
                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
                case LEAVE -> leaveGame(session, username, command);
                case RESIGN -> resign(session, username, command);
            }
        } catch (UnauthorizedException ex) {
            // Serializes and sends the error message
            sendMessage(session.getRemote(), new ErrorMessage("Error: unauthorized"));
        }
    }

    private void sendMessage(RemoteEndpoint remote, ErrorMessage error) throws Exception
    {
        remote.sendString(error.getErrorMessage());
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
        gameSession.put(gameID,session);
    }

    private void connect(Session session, String username, UserGameCommand command) throws UnauthorizedException
    {
        try {
            connections.add(username, session);
            var message = String.format("%s has joined the game", username);
            var notification = new NotificationMessage(message);
            connections.broadcast(username, notification);
        } catch (IOException e) {
            throw new UnauthorizedException(500, e.getMessage());
        }
    }

    private void makeMove(Session session, String username, MakeMoveCommand command) throws UnauthorizedException
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
            ChessMove move = command.getMove();

            currentGame.makeMove(move);
            GameData gameModel  = new GameData(currentGameModel.gameID(), currentGameModel.whiteUsername(),
                    currentGameModel.blackUsername(), currentGameModel.gameName(), currentGame);
            gameDAO.updateGame(gameModel);

            var message = String.format("%s moved from %s to %s", username, startPosString, endPosString);
            var notification = new NotificationMessage(message);
            connections.broadcast(username, notification);
        }
        catch (IOException | DataAccessException | InvalidMoveException e)
        {
            throw new UnauthorizedException(500, e.getMessage());
        }
    }

    private void leaveGame(Session session, String username, UserGameCommand command) throws UnauthorizedException
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

            var message = String.format("%s has left the game", username);
            var notification = new NotificationMessage(message);
            connections.broadcast(username, notification);
        }
        catch (IOException | DataAccessException e)
        {
            throw new UnauthorizedException(500, e.getMessage());
        }
    }

    private void resign(Session session, String username, UserGameCommand command) throws UnauthorizedException
    {
        try
        {
            var message = String.format("%s has left the game", username);
            var notification = new NotificationMessage(message);
            connections.broadcast(username, notification);
        }
        catch (IOException e)
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
}
