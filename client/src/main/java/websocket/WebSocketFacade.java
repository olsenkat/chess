package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import exception.UnauthorizedException;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.logging.Logger;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;
    Logger logger;


    public WebSocketFacade(String url, NotificationHandler notificationHandler, Logger logger) throws ResponseException {
        try {
            this.logger = logger;
            this.logger.finer("Beginning WebSocketFacade");

            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);
//            this.session.setMaxIdleTimeout(60000000);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    switch (notification.getServerMessageType()) {
                        case NOTIFICATION -> {
                            notification = new Gson().fromJson(message, NotificationMessage.class);
                            notificationHandler.notify((NotificationMessage) notification);
                        }
                        case LOAD_GAME -> {
                            notification = new Gson().fromJson(message, LoadGameMessage.class);
                            NotificationMessage newMessage =
                                    new NotificationMessage(((LoadGameMessage) notification).getGame().toString());
                            notificationHandler.notify(newMessage);
                        }
                        case ERROR -> {
                            notification = new Gson().fromJson(message, ErrorMessage.class);
                            NotificationMessage newMessage =
                                    new NotificationMessage(((ErrorMessage) notification).getErrorMessage());
                            notificationHandler.notify(newMessage);
                        }
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException | IllegalStateException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String authToken, Integer gameID) throws UnauthorizedException
    {
        try
        {
            var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException | IllegalStateException e) {
            throw new UnauthorizedException(500, "Error: " + e.getMessage());
        }
    }

    public void makeMove(Session session, String authToken, Integer gameID, ChessMove move) throws UnauthorizedException
    {
        try
        {
            MakeMoveCommand action =
                    new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException | IllegalStateException e) {
            throw new UnauthorizedException(500, "Error: " + e.getMessage());
        }
    }

    public void leaveGame(Session session, String authToken, Integer gameID) throws UnauthorizedException
    {
        try
        {
            var action = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException | IllegalStateException e) {
            throw new UnauthorizedException(500, "Error: " + e.getMessage());
        }
    }

    public void resign(Session session, String authToken, Integer gameID) throws UnauthorizedException
    {
        try
        {
            var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException | IllegalStateException e) {
        throw new UnauthorizedException(500, "Error: " + e.getMessage());
        }
    }

}
