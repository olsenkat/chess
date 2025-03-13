package websocket.messages;

import chess.ChessGame;

public class ServerMessageLoadGame extends ServerMessage {
    private final ChessGame game;
    public ServerMessageLoadGame(ChessGame game) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public ChessGame getGame()
    {
        return this.game;
    }
}
