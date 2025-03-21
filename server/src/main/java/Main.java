import chess.*;
import dataaccess.*;
import exception.ResponseException;
import server.websocket.WebSocketHandler;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.Spark;
import server.Server;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        // Initialize data access
        GameDAO gameDataAccess = new MemoryGameDAO();
        UserDAO userDataAccess = new MemoryUserDAO();
        AuthDAO authDataAccess = new MemoryAuthDAO();

        try {
            if (args.length >= 1 && args[0].equals("sql")) {
                gameDataAccess = new MySqlGameDAO();
                userDataAccess = new MySqlUserDAO();
                authDataAccess = new MySqlAuthDAO();
            }
        }
        catch (ResponseException ex)
        {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }

        ClearService clearService = new ClearService(userDataAccess, authDataAccess, gameDataAccess);
        GameService gameService = new GameService(authDataAccess, gameDataAccess);
        UserService userService = new UserService(userDataAccess, authDataAccess);
        WebSocketHandler ws = new WebSocketHandler(userDataAccess, gameDataAccess, authDataAccess);
        Server newServer = new Server(clearService, userService, gameService, ws);
        int port = 8080;
        newServer.run(port);
    }
}