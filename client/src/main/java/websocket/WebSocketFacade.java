package websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import exception.UnauthorizedException;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocketmessages.Action;
import websocketmessages.Notification;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.logging.Logger;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler, Logger logger) throws ResponseException {
        try {
            logger.finer("Beginning WebSocketFacade");
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            logger.finer("Getting the Web Socket Container");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            logger.finer("Connecting to server");
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            logger.finer("Adding a message handler");
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Notification notification = new Gson().fromJson(message, Notification.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
        logger.finer("Finishing WebSocketFacade");
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    private void sendMessage(RemoteEndpoint remote, ErrorMessage error)
    {

    }

    private void connect(String username) throws UnauthorizedException
    {
        try
        {
            UserGameCommand.CommandType command = UserGameCommand.CommandType.CONNECT;
            String connectString = "username: \"" + username + "\", command: \"" + command + "\"";
            var action = new Action(Action.Type.ENTER, connectString);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException e) {
            throw new UnauthorizedException(500, "Error: " + e.getMessage());
        }
    }

    private String makeMove(Session session, String username, MakeMoveCommand command) throws UnauthorizedException
    {
        return null;
    }

    private String leaveGame(Session session, String username, UserGameCommand command) throws UnauthorizedException
    {
        return null;
    }

    private String resign(Session session, String username, UserGameCommand command) throws UnauthorizedException
    {
        return null;
    }

}
