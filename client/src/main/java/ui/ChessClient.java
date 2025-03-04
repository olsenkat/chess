package ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import requestresult.*;
import websocket.NotificationHandler;
import server.ServerFacade;
import websocket.WebSocketFacade;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ChessClient {
    private String username = null;
    private final ServerFacade server;
    private final String serverUrl;
    private final NotificationHandler notificationHandler;
    private WebSocketFacade ws;
    private State state = State.SIGNEDOUT;
    private String authToken;
    private HashMap<Integer, Integer> serverToClientGameID;
    private HashMap<Integer, ChessGame> chessGame;
    private ChessGame currentGame;
    private static Logger logger;


    public ChessClient(String serverUrl, NotificationHandler notificationHandler) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        this.notificationHandler = notificationHandler;
        authToken = null;
        serverToClientGameID = new HashMap<>();
        logger = Logger.getLogger("ChessClient");
        logger.finest("Beginning logger");
        serverToClientGameID = new HashMap<>();
        chessGame = new HashMap<>();
    }

    public String evalPreLogin(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (state.equals(State.SIGNEDOUT)) {
                return switch (cmd) {
                    case "login" -> loginUser(params);
                    case "register" -> registerUser(params);
                    case "quit" -> "quit";
                    default -> help();
                };
            }
            else if (state.equals(State.SIGNEDIN))
            {
                return switch (cmd) {
                    case "create" -> createGame(params);
                    case "list" -> listGames();
                    case "join" -> joinGame(params);
                    case "observe" -> observeGame(params);
                    case "logout" -> logoutUser();
                    case "quit" -> quitLogout();
                    default -> help();
                };
            }
            else
            {
                return quitLogout();
            }
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String loginUser(String... params) throws ResponseException {
        if (params.length >= 2) {
            // Ensure we move to the signed in state
            state = State.SIGNEDIN;

            // Set username and password
            username = params[0];
            String password = params[1];

            // Get the login result, set the authToken, and output data
            var loginResult = server.loginUser(new LoginRequest(username, password));
            authToken = loginResult.authToken();
            listAddGames();
            state = State.SIGNEDIN;
            return String.format("You signed in as %s.", loginResult.username());
        }
        throw new ResponseException(400, "Expected: <username> <password>");
    }

    public String registerUser(String... params) throws ResponseException {
        if (params.length >= 3) {
            // Set username, password, email
            username = params[0];
            String password = params[1];
            String email = params[2];

            // Register the user, retrieve the authToken, and return the username
            var registerResult = server.registerUser(new RegisterRequest(username, password, email));
            authToken = registerResult.authToken();
            listAddGames();
            state = State.SIGNEDIN;
            return String.format("You signed in as %s.", registerResult.username());
        }
        throw new ResponseException(400, "Expected: <username> <password> <email>");
    }



    public String logoutUser() throws ResponseException {
        // Sign out of ws
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
        return String.format("You logged out as %s.", oldUser);
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(400, "You must sign in");
        }
    }

    public String createGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            String gameName = params[0];
            assertSignedIn();

            var createResult = server.createGame(new CreateRequest(authToken, gameName));
            listAddGames();
            return String.format("You created a game with ID = %d.", createResult.gameID());
        }
        throw new ResponseException(400, "Expected: <NAME>");
    }

    public String listGames() throws ResponseException {
            assertSignedIn();
            return listAddGames();
    }

    public String joinGame(String... params) throws ResponseException {
        if (params.length >= 2) {
            listAddGames();
            String gameIDString = params[0];
            String playerColor = params[1];
            int gameID = isInt(gameIDString);

            server.joinGame(new JoinRequest(authToken, playerColor, gameID));

            state = State.INGAME;
            currentGame = chessGame.get(serverToClientGameID.get(gameID));
            return String.format("You successfully joined a game with ID = %d.", gameID);
        }
        throw new ResponseException(400, "Expected: <ID>");
    }

    private int isInt(String integer) throws ResponseException
    {
        try {
            return Integer.parseInt(integer);
        }
        catch (NumberFormatException e)
        {
            throw new ResponseException(400, "Expected: <ID>");
        }
    }

    public String observeGame(String... params) throws ResponseException {
        if (params.length >= 1) {
            listAddGames();
            String gameIDString = params[0];
            int gameID = isInt(gameIDString);

            state = State.INGAME;
            currentGame = chessGame.get(serverToClientGameID.get(gameID));

            return String.format("You successfully are observing a game with ID = %d.", gameID);
        }
        throw new ResponseException(400, "Expected: <NAME>");
    }

    private String listAddGames() throws ResponseException
    {
        serverToClientGameID = new HashMap<>();
        chessGame = new HashMap<>();

        var listResult = server.listGames(new ListRequest(authToken));
        StringBuilder gameList = new StringBuilder();
        ArrayList<GameData> games = listResult.games();
        for (int i = 1; i <= listResult.games().size(); i++)
        {
            int j = i-1;
            String gameName = games.get(j).gameName();
            String whiteUsername = games.get(j).whiteUsername();
            String blackUsername = games.get(j).blackUsername();
            String game = i + ". Game name: " + gameName + " White: " + whiteUsername +
                    " Black: " + blackUsername + "\n";
            gameList.append(game);
            serverToClientGameID.put(games.get(j).gameID(), i);
            chessGame.put(i, games.get(j).game());
        }
        return gameList.toString();
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - register <username> <password> <email> - to create an account
                    - login <username> <password> - to play chess
                    - quit - playing chess
                    - help - with possible commands
                    """;
        }
        else if (state == State.SIGNEDIN)
        {
            return """
                    - logout - return to start
                    - create <NAME> - a game
                    - list - games
                    - join <ID> [WHITE|BLACK] - a game
                    - observe <ID> - a game
                    - quit - playing chess
                    - help - with possible commands
                    """;
        }
        return """
                - help
                - register <username> <password> <email>
                - login <username> <password>
                - quit
                """;
    }

    private String quitLogout() throws ResponseException
    {
        logoutUser();
        return "quit";
    }

}


