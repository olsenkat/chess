package websocket.messages;

public class ServerMessageError  extends ServerMessage{
    private final String errorMessage;
    public ServerMessageError(ServerMessageType type, String errorMessage) {
        super(type);
        this.errorMessage = errorMessage;
    }
    public String getErrorMessage()
    {
        return this.errorMessage;
    }
}
