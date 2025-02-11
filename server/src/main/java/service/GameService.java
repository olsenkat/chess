package service;

import chess.ChessGame;
import dataaccess.*;
import model.GameData;
import request_result.*;

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
        return null;
    }

    ListResult list(ListRequest r)
    {
        return null;
    }
}
