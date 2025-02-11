package service;

import dataaccess.*;
import request_result.*;
import model.UserData;
import model.AuthData;

public class UserService
{
    private final UserDAO users = new MemoryUserDAO();
    private final AuthDAO auth = new MemoryAuthDAO();
    RegisterResult register(RegisterRequest r)
    {
        UserData new_user = new UserData(r.username(), r.password(), r.email());
        String token = AuthDAO.generateToken();
        try {
            users.createUser(new_user);
        }
        catch (DataAccessException e)
        {
            return new RegisterResult(null, null, "Error: User Creation Error");
        }
        try {
            auth.createAuth(new AuthData(r.username(),token));
        }
        catch (DataAccessException e)
        {
            return new RegisterResult(null, null, "Error: Auth Creation Error");
        }
        return new RegisterResult(r.username(), token, null);
    }

    LoginResult login(LoginRequest r)
    {
        String token = AuthDAO.generateToken();
        try
        {
            users.getUser(r.username());
        }
        catch (DataAccessException e)
        {
            return new LoginResult(null, null, "Error: User Login Error");
        }
        try {
            auth.createAuth(new AuthData(r.username(),token));
        }
        catch (DataAccessException e)
        {
            return new LoginResult(null, null, "Error: Auth Creation Error");
        }
        return new LoginResult(r.username(), token, null);
    }

    LogoutResult logout(LogoutRequest r)
    {
        return null;
    }
}
