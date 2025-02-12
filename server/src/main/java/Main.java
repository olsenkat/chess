import chess.*;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.Spark;
import server.Server;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        ClearService clearService = new ClearService();
        GameService gameService = new GameService();
        UserService userService = new UserService();
        Server newServer = new Server(clearService, userService, gameService);
        int port = 8080;
        newServer.run(port);
    }
}