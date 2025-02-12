package service;

import chess.ChessGame;
import dataaccess.*;
import model.GameData;
import request_result.*;

import java.util.Objects;

public class GameService
{
    private final UserDAO users = new MemoryUserDAO();
    private final AuthDAO auth = new MemoryAuthDAO();
    private final GameDAO games = new MemoryGameDAO();
    CreateResult create(CreateRequest r)
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

    JoinResult join(JoinRequest r)
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

    ListResult list(ListRequest r)
    {
        return null;
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
