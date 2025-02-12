package server;

import com.google.gson.Gson;
import dataaccess.*;
import exception.ResponseException;
import request_result.*;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    private final ClearService clearService;
    private final UserService userService;
    private final GameService gameService;

    public Server(ClearService clearService, UserService userService, GameService gameService) {
        this.clearService = clearService;
        this.userService = userService;
        this.gameService = gameService;
    }
    public Server() {
        GameDAO gameDataAccess = new MemoryGameDAO();
        UserDAO userDataAccess = new MemoryUserDAO();
        AuthDAO authDataAccess = new MemoryAuthDAO();
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

        Spark.awaitInitialization();
        return Spark.port();
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
        var user_response = userService.register(user); // Generate a response
        return new Gson().toJson(user_response); // Serialize the data
    }

    private Object login(Request req, Response res) throws ResponseException {
        // Create a RegisterRequest object by deserializing the request
        var user = new Gson().fromJson(req.body(), LoginRequest.class);
        var user_login = userService.login(user); // Generate response
        return new Gson().toJson(user_login); // Serialize the data
    }

    private Object logout(Request req, Response res) throws ResponseException {
        // Create a RegisterRequest object by deserializing the request
        var user = new Gson().fromJson(req.body(), LogoutRequest.class);
        var user_logout = userService.logout(user); // Generate response
        return new Gson().toJson(user_logout); // Serialize the data
    }

    private Object listGames(Request req, Response res) throws ResponseException {
        // Create a RegisterRequest object by deserializing the request
        var auth = new Gson().fromJson(req.body(), ListRequest.class);
        var game_list = gameService.list(auth); // Generate response
        return new Gson().toJson(game_list); // Serialize the data
    }

    private Object createGame(Request req, Response res) throws ResponseException {
        // Serialize the JSON data into the create_request object to pass into gameService
        var game = new Gson().fromJson(req.body(), CreateRequest.class);
        // Create the game_response object to be retrieved from gameService
        var new_game = gameService.create(game);
        // Deserialize the JSON data
        return new Gson().toJson(new_game);
    }

    private Object joinGame(Request req, Response res) throws ResponseException {
        // Serialize the JSON data into the create_request object to pass into gameService
        var game = new Gson().fromJson(req.body(), JoinRequest.class);
        var game_data = gameService.join(game);
        return new Gson().toJson(game_data);
    }

}
