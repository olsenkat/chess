package service;

import dataaccess.*;
import request_result.ClearResult;

public class ClearService
{
    private final UserDAO users = new MemoryUserDAO();
    private final AuthDAO auth = new MemoryAuthDAO();
    private final GameDAO games = new MemoryGameDAO();
    public ClearResult clear()
    {
        // Clear User Data
        users.clear();

        // Clear Auth Data
        auth.clear();

        // Clear Game Data
        games.clear();
        return new ClearResult();

    }
}
