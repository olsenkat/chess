package service;

import dataaccess.*;
import request_result.*;
import model.UserData;
import model.AuthData;

public class UserService
{
    private final UserDAO users;
    private final AuthDAO auth;

    public UserService(UserDAO users, AuthDAO auth)
    {
        this.users = users;
        this.auth = auth;
    }

    public RegisterResult register(RegisterRequest r)
    {
        UserData new_user = new UserData(r.username(), r.password(), r.email());
        String token = AuthDAO.generateToken();
        try {
            users.createUser(new_user);
        }
        catch (DataAccessException e)
        {
            return new RegisterResult(null, null, "Error: Unable to create User");
        }
        try {
            auth.createAuth(new AuthData(r.username(),token));
        }
        catch (DataAccessException e)
        {
            return new RegisterResult(null, null, "Error: Unable to create Auth");
        }
        return new RegisterResult(r.username(), token, null);
    }

    public LoginResult login(LoginRequest r)
    {
        String token = AuthDAO.generateToken();
        try
        {
            users.getUser(r.username());
        }
        catch (DataAccessException e)
        {
            return new LoginResult(null, null, "Error: Unable to find User");
        }
        try {
            auth.createAuth(new AuthData(r.username(),token));
        }
        catch (DataAccessException e)
        {
            return new LoginResult(null, null, "Error: Unable to create Auth");
        }
        return new LoginResult(r.username(), token, null);
    }

    public LogoutResult logout(LogoutRequest r)
    {
        AuthData current_auth;
        try
        {
            current_auth = auth.getAuth(r.authToken());
            try
            {
                auth.deleteAuth(current_auth);
                return new LogoutResult(null);
            }
            catch (DataAccessException e)
            {
                return new LogoutResult("Error: Unable to delete the authorization");
            }
        }
        catch (DataAccessException e)
        {
            return new LogoutResult("Error: Invalid Authorization");
        }
    }
}
