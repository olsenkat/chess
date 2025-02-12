package server;

import com.google.gson.Gson;
import exception.ResponseException;
import request_result.CreateRequest;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;

public class Server {

    private final ClearService clearService;
    private final UserService userService;
    private final GameService gameService;
//    private final WebSocketHandler webSocketHandler;

    public Server(ClearService clearService, UserService userService, GameService gameService) {
        this.clearService = clearService;
        this.userService = userService;
        this.gameService = gameService;
//        webSocketHandler = new WebSocketHandler();
    }

    //    private final WebSocketHandler webSocketHandler;
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

//        Spark.webSocket("/ws", webSocketHandler);

        Spark.delete("/db", this::clear);
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::login);
        Spark.delete("/session", this::logout);
        Spark.get("/game", this::listGames);
        Spark.post("/game", this::createGame);
        Spark.put("/game", this::joinGame);


//        Spark.post("/pet", this::addPet);
//        Spark.get("/pet", this::listPets);
//        Spark.delete("/pet/:id", this::deletePet);
//        Spark.delete("/pet", this::deleteAllPets);
//        Spark.exception(ResponseException.class, this::exceptionHandler);

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private Object clear(Request req, Response res) throws ResponseException {
//        var pet = new Gson().fromJson(req.body(), Pet.class);
//        pet = service.addPet(pet);
//        webSocketHandler.makeNoise(pet.name(), pet.sound());
//        return new Gson().toJson(pet);
        return null;
    }

    private Object registerUser(Request req, Response res) throws ResponseException {
//        var pet = new Gson().fromJson(req.body(), Pet.class);
//        pet = service.addPet(pet);
//        webSocketHandler.makeNoise(pet.name(), pet.sound());
//        return new Gson().toJson(pet);
        return null;
    }

    private Object login(Request req, Response res) throws ResponseException {
//        var pet = new Gson().fromJson(req.body(), Pet.class);
//        pet = service.addPet(pet);
//        webSocketHandler.makeNoise(pet.name(), pet.sound());
//        return new Gson().toJson(pet);
        return null;
    }

    private Object logout(Request req, Response res) throws ResponseException {
//        var pet = new Gson().fromJson(req.body(), Pet.class);
//        pet = service.addPet(pet);
//        webSocketHandler.makeNoise(pet.name(), pet.sound());
//        return new Gson().toJson(pet);
        return null;
    }

    private Object listGames(Request req, Response res) throws ResponseException {
//        var pet = new Gson().fromJson(req.body(), Pet.class);
//        pet = service.addPet(pet);
//        webSocketHandler.makeNoise(pet.name(), pet.sound());
//        return new Gson().toJson(pet);
        return null;
    }

    private Object createGame(Request req, Response res) throws ResponseException {
        var game = new Gson().fromJson(req.body(), CreateRequest.class);
        var new_game = gameService.create(game);
//        webSocketHandler.makeNoise(pet.name(), pet.sound());
        return new Gson().toJson(new_game);
    }

    private Object joinGame(Request req, Response res) throws ResponseException {
//        var pet = new Gson().fromJson(req.body(), Pet.class);
//        pet = service.addPet(pet);
//        webSocketHandler.makeNoise(pet.name(), pet.sound());
//        return new Gson().toJson(pet);
        return null;
    }

}
