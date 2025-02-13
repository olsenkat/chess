package service;

import chess.ChessGame;
import dataaccess.*;
import exception.ResponseException;
import model.GameData;
import model.UserData;
import request_result.*;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class GameService
{
    private final AuthDAO auth;
    private final GameDAO games;

    public GameService(AuthDAO auth, GameDAO games)
    {
        this.auth = auth;
        this.games = games;
    }

    public CreateResult create(CreateRequest r) throws ResponseException
    {
        int gameID = games.createID();
        try
        {
            auth.getAuth(r.authToken());
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Invalid Authorization");
        }
        try
        {
            ChessGame newGame = new ChessGame();
            games.createGame(new GameData(gameID, null, null, r.gameName(), newGame));
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Unable to create game");
        }
        return new CreateResult(gameID);
    }

    public JoinResult join(JoinRequest r) throws ResponseException
    {
        GameData currentGame;
        String username;
        if
        (r.gameID()==null)
        {
            throw new ResponseException(400, "Error: Invalid ID");
        }
        int gameID = r.gameID();
        try
        {
            username = auth.getAuth(r.authToken()).username();
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Invalid Authorization");
        }
        try
        {
            currentGame = games.getGame(gameID);
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Invalid Game ID");
        }
        if (r.playerColor()==null)
        {
            throw new ResponseException(400, "Error: No color specified");
        }
        checkTeamColor(currentGame, r.playerColor());
        try {
            if (r.playerColor().toLowerCase(Locale.ROOT).equals("white")) {
                if (!Objects.equals(currentGame.whiteUsername(), null))
                {
                    throw new ResponseException(403, "Error: Team Color already taken");
                }
                games.updateGame(new GameData(r.gameID(), username, currentGame.blackUsername(), currentGame.gameName(), currentGame.game()));
            }
            else if (r.playerColor().toLowerCase(Locale.ROOT).equals("black"))
            {
                if(!Objects.equals(currentGame.blackUsername(), null))
                {
                    throw new ResponseException(403, "Error: Team Color already taken");
                }
                games.updateGame(new GameData(r.gameID(), currentGame.whiteUsername(),username, currentGame.gameName(), currentGame.game()));

            }
            else
            {
                throw new ResponseException(400, "Error: Invalid Color");
            }
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Unable to access game");
        }
        return new JoinResult();
    }

    public ListResult list(ListRequest r) throws ResponseException
    {
        try
        {
            auth.getAuth(r.authToken());
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Invalid Authorization");
        }
        try
        {
            return new ListResult(games.listGames());
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Invalid List Game Result");
        }

    }

    private void checkTeamColor (GameData data, String color) throws ResponseException
    {
        if (color.equalsIgnoreCase("white"))
        {
            if (!Objects.equals(data.whiteUsername(), null))
            {
                throw new ResponseException(403, "Error: Team Color already taken");
            }
        }
        else if (color.equalsIgnoreCase("black"))
        {
            if(!Objects.equals(data.blackUsername(), null))
            {
                throw new ResponseException(403, "Error: Team Color already taken");
            }
        }
        else
        {
            throw new ResponseException(400, "Error: Invalid Color");
        }
    }
}
