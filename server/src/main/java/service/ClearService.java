package service;

import dataaccess.*;
import exception.ResponseException;
import requestresult.ClearResult;

public class ClearService
{
    private final UserDAO users;
    private final AuthDAO auth;
    private final GameDAO games;

    // Create constructor
    public ClearService(UserDAO users, AuthDAO auth, GameDAO games)
    {
        this.users = users;
        this.auth = auth;
        this.games = games;
    }

    public ClearResult clear() throws ResponseException
    {
        try {// Clear User Data
            users.clear();

            // Clear Auth Data
            auth.clear();

            // Clear Game Data
            games.clear();
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(500, (String.format("unable to clear database: %s", e.getMessage())));
        }
        return new ClearResult();
    }
}
