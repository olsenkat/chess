package service;

import chess.ChessGame;
import dataaccess.*;
import exception.ResponseException;
import model.GameData;
import request_result.*;

import java.util.ArrayList;
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
        int gameID = r.gameID();
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
            currentGame = games.getGame(gameID);
            if (!checkTeamColor(currentGame, r.playerColor()))
            {
                throw new ResponseException(401, "Error: Invalid Team Color");
            }
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Invalid Game ID");
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

    private boolean checkTeamColor (GameData data, String color)
    {
        if (color.equalsIgnoreCase("white"))
        {
            return Objects.equals(data.whiteUsername(), "");
        }
        else if (color.equalsIgnoreCase("black"))
        {
            return Objects.equals(data.blackUsername(), "");
        }
        return false;
    }
}
