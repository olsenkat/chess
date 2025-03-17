package websocket;

import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

public interface NotificationHandler {
    void notify(NotificationMessage notification);
    void loadGame(LoadGameMessage game);
}