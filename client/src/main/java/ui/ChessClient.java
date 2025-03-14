package ui;

// Data Structure Imports
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

// Chess imports
import chess.*;

// Game imports
import exception.UnauthorizedException;
import model.GameData;
import requestresult.*;
import server.ServerFacade;

// Utility imports
import exception.ResponseException;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;
import static ui.EscapeSequences.*;
import java.util.logging.Logger;
import javax.websocket.*;

public class ChessClient {
    // Set initial
    private String username = null;
    private final ServerFacade server;
    private State state = State.SIGNEDOUT;
    private String authToken;
    private HashMap<Integer, Integer> serverToClientGameID;
    private HashMap<Integer, ChessGame> chessGame;
    private ChessGame currentGame;
    private static Logger logger;
    private ChessGame.TeamColor teamColor;

    private Session session;
    private final NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    private int serverGameID;
    private String serverUrl;

    public ChessClient(String serverUrl, NotificationHandler notificationHandler) {
        this.serverUrl = serverUrl;
        server = new ServerFacade(serverUrl);
        // Set web socket variables
        this.notificationHandler = notificationHandler;
        authToken = null;
        serverToClientGameID = new HashMap<>();
        logger = Logger.getLogger("ChessClient");
        logger.finest("Beginning logger");
        serverToClientGameID = new HashMap<>();
        chessGame = new HashMap<>();
        this.teamColor = ChessGame.TeamColor.WHITE;
        ws=null;
        serverGameID = 0;
    }

    // Evaluates user input
    public String eval(String input) {
        try {
            // Set up login input
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);

            // Determine which loop we are in
            if (state.equals(State.SIGNEDOUT)) {
                return switch (cmd) {
                    case "login" -> loginUser(params);
                    case "register" -> registerUser(params);
                    case "quit" -> "quit";
                    default -> help();
                };
            } else if (state.equals(State.SIGNEDIN)) {
                return switch (cmd) {
                    case "create" -> createGame(params);
                    case "list" -> listGames();
                    case "join" -> joinGame(params);
                    case "observe" -> observeGame(params);
                    case "logout" -> logoutUser();
                    case "quit" -> quitLogout();
                    default -> help();
                };
            } else if (state.equals(State.INGAME)){
                return switch (cmd) {
                    case "redraw" -> {
                        displayBoard();
                        yield "";
                    }
                    case "leave" -> leaveGame();
                    case "move" -> movePiece(params);
                    case "resign" -> resignFromGame();
                    case "highlight" -> highlightMoves(params);
                    default -> help();
                };
            }
            else
            {
                return switch (cmd)
                {
                    case "return" -> leaveGame();
                    default ->  help();
                };
            }
        } catch (ResponseException | UnauthorizedException ex) {
            return ex.getMessage();
        }
    }

    // Logs in the user
    public String loginUser(String... params) throws ResponseException {
        if (params.length >= 2) {
            // Set username and password
            username = params[0];
            String password = params[1];

            // Get the login result, set the authToken, and output data
            var loginResult = server.loginUser(new LoginRequest(username, password));
            authToken = loginResult.authToken();
            listAddGames(); // Determines the list of games available to join
            state = State.SIGNEDIN;
            return String.format("You signed in as %s.\n", loginResult.username());
        }
        throw new ResponseException(400, "Expected: <username> <password>\n");
    }

    // Registers the user
    public String registerUser(String... params) throws ResponseException {
        if (params.length >= 3) {
            // Set username, password, email
            username = params[0];
            String password = params[1];
            String email = params[2];

            // Register the user, retrieve the authToken, and return the username
            var registerResult = server.registerUser(new RegisterRequest(username, password, email));
            authToken = registerResult.authToken();
            listAddGames(); // Determines the list of games available to join
            state = State.SIGNEDIN;
            return String.format("You registered %s.\n", registerResult.username());
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>\n");
    }

    // Logs out the user
    public String logoutUser() throws ResponseException {
        assertSignedIn();

        // Log out of chess client
        server.logoutUser(new LogoutRequest(authToken));

        // Reset class info to null. Return logout script.
        authToken = null;
        String oldUser = username;
        username = null;
        serverToClientGameID = new HashMap<>();
        chessGame = new HashMap<>();
        state = State.SIGNEDOUT;
        return String.format("You logged out as %s.\n", oldUser);
    }

    // Ensure the user is signed in
    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in\n");
        }
    }

    // Creates the chess game
    public String createGame(String... params) throws ResponseException {
        // Ensure the user added a parameter
        if (params.length >= 1) {
            String gameName = params[0];
            assertSignedIn();

            // Create the game
            var createResult = server.createGame(new CreateRequest(authToken, gameName));
            listAddGames(); // Add the game to game list

            // Set the board
            ChessBoard newBoard = new ChessBoard();
            newBoard.resetBoard();

            // Retrieve the board and client id
            int gameID = serverToClientGameID.get(createResult.gameID());
            chessGame.get(gameID).setBoard(newBoard);
            return String.format("You created a game with ID = %d.\n", gameID);
        }
        throw new ResponseException(400, "Expected: <NAME>\n");
    }

    // list games
    public String listGames() throws ResponseException {
        assertSignedIn();
        return listAddGames();
    }

    // Joins Chess Game
    public String joinGame(String... params) throws ResponseException {
        // Determine both params are present
        if (params.length >= 2) {
            listAddGames(); // Double check game list

            // Set variables
            String gameIDString = params[0];
            String playerColor = params[1];
            int gameID = isInt(gameIDString); // Determine the game ID is an int
            if (!serverToClientGameID.containsValue(gameID))
            {
                throw new ResponseException(400, "Game ID is invalid \n");
            }

            // Join the game
            server.joinGame(new JoinRequest(authToken, playerColor, gameID));

            // Set state
            state = State.INGAME;

            // create currentGame variable to reference our game
            currentGame = chessGame.get(serverToClientGameID.get(gameID));
            setTeamColor(playerColor);
            serverGameID = gameID;

            // Start websocket
            ws = new WebSocketFacade(serverUrl, notificationHandler, logger);

            try
            {
                ws.connect(authToken, gameID);
            } catch (UnauthorizedException e)
            {
                throw new ResponseException(400, "Couldn't connect to game");
            }

            // Display our board
            System.out.printf("You successfully joined game %d.\n", gameID);
            displayBoard();
            return "";
        }
        throw new ResponseException(400, "Expected: <ID> <WHITE|BLACK>\n");
    }

    // Determines the given string can be cast into an int
    private int isInt(String integer) throws ResponseException {
        try {
            return Integer.parseInt(integer);
        } catch (NumberFormatException e) {
            throw new ResponseException(400, "Expected Integer: <ID> \n");
        }
    }

    // Observe an occurring game
    public String observeGame(String... params) throws ResponseException {
        // Check the user entered the ID
        if (params.length >= 1) {
            listAddGames(); // Ensure our list is updated
            
            // Set gameID, determine it is an integer
            String gameIDString = params[0];
            int gameID = isInt(gameIDString);
            if (!serverToClientGameID.containsValue(gameID))
            {
                throw new ResponseException(400, "Game ID is invalid \n");
            }
            
            state = State.INGAME; // Set state

            // Start websocket
            ws = new WebSocketFacade(serverUrl, notificationHandler, logger);

            try
            {
                ws.connect(authToken, gameID);
            } catch (UnauthorizedException e)
            {
                throw new ResponseException(400, "Couldn't connect to game");
            }

            // Get chess game and display the board
            currentGame = chessGame.get(serverToClientGameID.get(gameID));
            serverGameID = gameID;
            System.out.printf("You are observing game %d.\n%n", gameID);
            setTeamColor("WHITE");
            displayBoard();
            return "";
        }
        throw new ResponseException(400, "Expected: <ID>\n");
    }

    // Lists all the games and adds them to our game ID maps
    private String listAddGames() throws ResponseException {
        // refresh maps
        serverToClientGameID = new HashMap<>();
        chessGame = new HashMap<>();

        // Get the list result and prepare to parse it
        var listResult = server.listGames(new ListRequest(authToken));
        StringBuilder gameList = new StringBuilder();
        ArrayList<GameData> games = listResult.games();
        
        // Parse through all games
        for (int i = 1; i <= listResult.games().size(); i++) {
            int j = i - 1; // Our array indices go from 0 to 7

            // get the current game and append it
            String game = getString(games, j, i);
            gameList.append(game);

            // Put the game in the game maps
            serverToClientGameID.put(games.get(j).gameID(), i);
            chessGame.put(i, games.get(j).game());
        }
        return gameList.toString();
    }

    // Gets the individual strings for a game
    private static String getString(ArrayList<GameData> games, int j, int i) {
        String gameName = games.get(j).gameName();
        String whiteUsername = games.get(j).whiteUsername();
        String blackUsername = games.get(j).blackUsername();
        return i + ". Game Name: " + SET_TEXT_COLOR_LIGHT_GREY + gameName + EMPTY + SET_TEXT_COLOR_MAGENTA
                + " White: " + SET_TEXT_COLOR_LIGHT_GREY + whiteUsername + EMPTY + SET_TEXT_COLOR_MAGENTA +
                " Black: " + SET_TEXT_COLOR_LIGHT_GREY + blackUsername + "\n" + SET_TEXT_COLOR_MAGENTA;
    }

    // Prints help command
    public String help() {
        if (state == State.SIGNEDOUT) {
            return "- register <username> <password> <email>" + SET_TEXT_COLOR_LIGHT_GREY +
                    " - to create an account\n" + SET_TEXT_COLOR_MAGENTA + "- login <username> <password>" +
                    SET_TEXT_COLOR_LIGHT_GREY + " - to play chess\n" + SET_TEXT_COLOR_MAGENTA +
                    "- quit" + SET_TEXT_COLOR_LIGHT_GREY + " - playing chess\n" + SET_TEXT_COLOR_MAGENTA +
                    "- help" + SET_TEXT_COLOR_LIGHT_GREY + " - with possible commands\n";
        } else if (state == State.SIGNEDIN) {
            return "- logout" + SET_TEXT_COLOR_LIGHT_GREY + " - return to start\n" + SET_TEXT_COLOR_MAGENTA +
                    "- create <NAME>" + SET_TEXT_COLOR_LIGHT_GREY + " - a game\n" + SET_TEXT_COLOR_MAGENTA +
                    "- list" + SET_TEXT_COLOR_LIGHT_GREY + " - games\n" + SET_TEXT_COLOR_MAGENTA +
                    "- join <ID> [WHITE|BLACK]" + SET_TEXT_COLOR_LIGHT_GREY + " - a game\n" + SET_TEXT_COLOR_MAGENTA +
                    "- observe <ID>" + SET_TEXT_COLOR_LIGHT_GREY + " - a game\n" + SET_TEXT_COLOR_MAGENTA +
                    "- quit" + SET_TEXT_COLOR_LIGHT_GREY + " - playing chess\n" + SET_TEXT_COLOR_MAGENTA +
                    "- help" + SET_TEXT_COLOR_LIGHT_GREY + " - with possible commands\n";
        } else if (state == State.INGAME) {
            return "- highlight <startLocation>" + SET_TEXT_COLOR_LIGHT_GREY +
                    " - to highlight legal moves\n" + SET_TEXT_COLOR_MAGENTA +
                    "- move <startLocation> <endLocation> <promotion>" + SET_TEXT_COLOR_LIGHT_GREY +
                    " - to move pawn with promotion.\n" + SET_TEXT_COLOR_MAGENTA +
                    "- move <startLocation> <endLocation>" + SET_TEXT_COLOR_LIGHT_GREY + " - to make move.\n" +
                    SET_TEXT_COLOR_MAGENTA + "- redraw" + SET_TEXT_COLOR_LIGHT_GREY + " - to redraw board\n" +
                    SET_TEXT_COLOR_MAGENTA + "- resign" + SET_TEXT_COLOR_LIGHT_GREY + " - to forfeit the game\n" +
                    SET_TEXT_COLOR_MAGENTA + "- leave" + SET_TEXT_COLOR_LIGHT_GREY +
                    " - leave game (you will be removed from the game)\n" + SET_TEXT_COLOR_MAGENTA +
                    "- help" + SET_TEXT_COLOR_LIGHT_GREY + " - with possible commands\n";
        }
        return SET_TEXT_COLOR_MAGENTA + "- return" + SET_TEXT_COLOR_LIGHT_GREY +
                " - leave game (you will be removed from the game)\n" + SET_TEXT_COLOR_MAGENTA +
                "- help" + SET_TEXT_COLOR_LIGHT_GREY + " - with possible commands\n";


    }

    public String leaveGame() throws UnauthorizedException
    {
        // websocket leave the game!!
        ws.leaveGame(session, authToken, serverGameID);
        ws=null;

        state = State.SIGNEDIN;
        return "You exited the chess game";
    }

    private String movePiece(String... params) throws UnauthorizedException {
        if (params.length<2)
        {
            throw new UnauthorizedException(500, "Expected move <startLocation> <endLocation>");
        }
        else
        {
            int startCol = getColumn(params[0].charAt(0));
            int startRow = getRow(params[0].charAt(1));
            int endCol = getColumn(params[1].charAt(0));
            int endRow = getRow(params[1].charAt(1));
            ChessPiece.PieceType promotionPiece = null;
            String returnString = "You moved from " + params[0] + " to " + params[1];

            if (params.length>=3)
            {
                promotionPiece = getPromotionPiece(params[2]);
                returnString+= " with a promotion piece of " + params[2];
            }

            returnString += ".\n";
            ChessPosition start = new ChessPosition(startRow, startCol);
            if (currentGame.getBoard().getPiece(start) == null)
            {
                throw new UnauthorizedException(500, "No piece at start location");
            }
            ChessPosition end = new ChessPosition(endCol, endRow);
            ChessMove move = new ChessMove(start, end, promotionPiece);
            ws.makeMove(session, authToken, serverGameID, move);
            return "";
        }
    }

    private String resignFromGame() throws UnauthorizedException {
        ws.resign(session, authToken, serverGameID);
        state = State.GAMEOVER;
        return username + " has resigned, and therefore forfeits the game";
    }

    private String highlightMoves(String... params) throws UnauthorizedException {
        if (params.length<1)
        {
            throw new UnauthorizedException(500, "Expected highlight <startLocation>");
        }
        else {
            int col = getColumn(params[0].charAt(0));
            int row = getRow(params[0].charAt(1));
            ChessPosition startPos = new ChessPosition(row, col);
            Collection<ChessMove> validMoves = currentGame.validMoves(startPos);
            if (validMoves==null)
            {
                throw new UnauthorizedException(500, "Given location has no piece");
            }
            highlightValidMoves(startPos, validMoves);
            return "";
        }
    }

    // Quits and logs out of server
    private String quitLogout() throws ResponseException {
        logoutUser();
        return "quit";
    }

    // Sets the team color. Ensures that the color is valid
    private void setTeamColor(String playerColor) throws ResponseException {
        if (playerColor.equalsIgnoreCase("white")) {
            this.teamColor = ChessGame.TeamColor.WHITE;
        } else if (playerColor.equalsIgnoreCase("black")) {
            this.teamColor = ChessGame.TeamColor.BLACK;
        } else {
            throw new ResponseException(400, "Expected: <WHITE|BLACK>\n");
        }
    }

    // Runs functions in DrawBoard. Displays the board
    private void displayBoard() {
        DrawBoard drawBoard = new DrawBoard(new ChessPiece[8][8], new String[8][8], currentGame);
        if (teamColor == ChessGame.TeamColor.WHITE) {
            drawBoard.drawWhiteBoard();
        } else if (teamColor == ChessGame.TeamColor.BLACK) {
            drawBoard.drawBlackBoard();
        } else {
            drawBoard.drawWhiteBoard();
        }
    }

    // Runs functions in DrawBoard. Displays the board
    private void highlightValidMoves(ChessPosition startPos, Collection<ChessMove> validMoves) {
        DrawBoard drawBoard = new DrawBoard(new ChessPiece[8][8], new String[8][8], currentGame);
        if (teamColor == ChessGame.TeamColor.WHITE) {
            drawBoard.drawWhiteHighlighted(startPos, validMoves);
        } else if (teamColor == ChessGame.TeamColor.BLACK) {
            drawBoard.drawBlackHighlighted(startPos, validMoves);
        } else {
            drawBoard.drawWhiteHighlighted(startPos, validMoves);
        }
    }

    private int getColumn(char col) throws UnauthorizedException {
        return switch (col)
        {
            case 'a'-> 1;
            case 'b'-> 2;
            case 'c'-> 3;
            case 'd'-> 4;
            case 'e'-> 5;
            case 'f'-> 6;
            case 'g'-> 7;
            case 'h'-> 8;
            default -> throw new UnauthorizedException(500, "Invalid Column Letter");
        };
    }

    private int getRow(char row) throws UnauthorizedException {
        return switch (row)
        {
            case '1'-> 1;
            case '2'-> 2;
            case '3'-> 3;
            case '4'-> 4;
            case '5'-> 5;
            case '6'-> 6;
            case '7'-> 7;
            case '8'-> 8;
            default -> throw new UnauthorizedException(500, "Invalid Row Number");
        };
    }

    private ChessPiece.PieceType getPromotionPiece(String piece) throws UnauthorizedException
    {
        return switch(piece.toLowerCase())
        {
            case "queen" -> ChessPiece.PieceType.QUEEN;
            case "rook" -> ChessPiece.PieceType.ROOK;
            case "bishop" -> ChessPiece.PieceType.BISHOP;
            case "knight" -> ChessPiece.PieceType.KNIGHT;
            default -> throw new UnauthorizedException(500, "Invalid Promotion Piece");
        };
    }

}


