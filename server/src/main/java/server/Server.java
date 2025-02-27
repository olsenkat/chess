package server;

import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import requestresult.*;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    private final ClearService clearService;
    private final UserService userService;
    private final GameService gameService;
    GameDAO gameDataAccess;
    UserDAO userDataAccess;
    AuthDAO authDataAccess;
    private boolean sqlDataAccess = true;

    public Server(ClearService clearService, UserService userService, GameService gameService) {
        this.clearService = clearService;
        this.userService = userService;
        this.gameService = gameService;
    }
    public Server() {
        if (sqlDataAccess)
        {
            try {
                gameDataAccess = new MySqlGameDAO();
                userDataAccess = new MySqlUserDAO();
                authDataAccess = new MySqlAuthDAO();
            }
            catch (Throwable ex)
            {
                System.out.printf("Unable to start server: %s%n", ex.getMessage());
            }
        }
        else
        {
            gameDataAccess = new MemoryGameDAO();
            userDataAccess = new MemoryUserDAO();
            authDataAccess = new MemoryAuthDAO();
        }
        this.clearService = new ClearService(userDataAccess, authDataAccess, gameDataAccess);
        this.userService = new UserService(userDataAccess, authDataAccess);
        this.gameService = new GameService(authDataAccess, gameDataAccess);
    }

    //    private final WebSocketHandler webSocketHandler;
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.delete("/db", this::clear);
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);
        Spark.exception(ResponseException.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void exceptionHandler(ResponseException ex, Request req, Response res) {
        res.status(ex.statusCode());
        res.body(ex.toJson());
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clear(Request req, Response res) throws ResponseException {
        var response = clearService.clear(); // generates a clear response
        return new Gson().toJson(response); // returns empty brackets if valid
    }

    private Object registerUser(Request req, Response res) throws ResponseException {
        // Create a RegisterRequest object by deserializing the request
        var user = new Gson().fromJson(req.body(), RegisterRequest.class);
        var userResponse = userService.register(user); // Generate a response
        return new Gson().toJson(userResponse); // Serialize the data
    }

    private Object login(Request req, Response res) throws ResponseException {
        // Create a RegisterRequest object by deserializing the request
        var user = new Gson().fromJson(req.body(), LoginRequest.class);
        var userLogin = userService.login(user); // Generate response
        return new Gson().toJson(userLogin); // Serialize the data
    }

    private Object logout(Request req, Response res) throws ResponseException {
        // Create a RegisterRequest object by deserializing the request
        var user = new LogoutRequest(req.headers("authorization"));
        var userLogout = userService.logout(user); // Generate response
        return new Gson().toJson(userLogout); // Serialize the data
    }

    private Object listGames(Request req, Response res) throws ResponseException {
        // Create a RegisterRequest object by deserializing the request
        var list = new ListRequest(req.headers("authorization"));
        var gameList = gameService.list(list); // Generate response
        return new Gson().toJson(gameList); // Serialize the data
    }

    private Object createGame(Request req, Response res) throws ResponseException {
        // Serialize the JSON data into the create request object to pass into gameService
        var game = new Gson().fromJson(req.body(), CreateRequest.class);
        game = new CreateRequest(req.headers("authorization"), game.gameName());
        // Create the game response object to be retrieved from gameService
        var newGame = gameService.create(game);
        // Deserialize the JSON data
        return new Gson().toJson(newGame);
    }

    private Object joinGame(Request req, Response res) throws ResponseException {
        // Serialize the JSON data into the create request object to pass into gameService
        var game = new Gson().fromJson(req.body(), JoinRequest.class);
        game = new JoinRequest(req.headers("authorization"), game.playerColor(), game.gameID());
        var gameData = gameService.join(game);
        return new Gson().toJson(gameData);
    }

}
