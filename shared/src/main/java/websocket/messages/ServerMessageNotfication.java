package websocket.messages;

public class ServerMessageNotfication extends ServerMessage{
    private final String message;
    public ServerMessageNotfication(String message) {
        super(ServerMessageType.NOTIFICATION);
        this.message = message;
    }
    public String getMessage()
    {
        return this.message;
    }
}
