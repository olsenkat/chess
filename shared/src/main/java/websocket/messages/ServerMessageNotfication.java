package websocket.messages;

public class ServerMessageNotfication extends ServerMessage{
    private final String message;
    public ServerMessageNotfication(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }
    public String getMessage()
    {
        return this.message;
    }
}
