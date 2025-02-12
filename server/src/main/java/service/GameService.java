package service;

import chess.ChessGame;
import dataaccess.*;
import model.GameData;
import request_result.*;

import java.util.ArrayList;
import java.util.Objects;

public class GameService
{
    private final UserDAO users = new MemoryUserDAO();
    private final AuthDAO auth = new MemoryAuthDAO();
    private final GameDAO games = new MemoryGameDAO();
    public CreateResult create(CreateRequest r)
    {
        int gameID = games.createID();
        try
        {
            auth.getAuth(r.authToken());
        }
        catch (DataAccessException e)
        {
            return new CreateResult(null, "Error: Unable to access authorization");
        }
        try
        {
            ChessGame newGame = new ChessGame();
            games.createGame(new GameData(gameID, null, null, r.gameName(), newGame));
        }
        catch (DataAccessException e)
        {
            return new CreateResult(null, "Error: Unable to create Game");
        }
        return new CreateResult(gameID, null);
    }

    public JoinResult join(JoinRequest r)
    {
        GameData currentGame;
        int gameID = r.gameID();
        try
        {
            auth.getAuth(r.authToken());
        }
        catch (DataAccessException e)
        {
            return new JoinResult("Error: Unable to access authorization");
        }
        try
        {
            currentGame = games.getGame(gameID);
            if (!checkTeamColor(currentGame, r.playerColor()))
            {
                return new JoinResult("Error: Team Color is already taken");
            }
        }
        catch (DataAccessException e)
        {
            return new JoinResult("Error: Unable to access authorization");
        }

        return new JoinResult(null);
    }

    public ListResult list(ListRequest r)
    {
        try
        {
            auth.getAuth(r.authToken());
        }
        catch (DataAccessException e)
        {
            return new ListResult(null, "Error: Unable to access authorization");
        }
        try
        {
            return new ListResult(games.listGames(), null);
        }
        catch (DataAccessException e)
        {
            return new ListResult(null, "Error: Unable to list games");
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
