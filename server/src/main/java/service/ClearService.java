package service;

import dataaccess.*;
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
