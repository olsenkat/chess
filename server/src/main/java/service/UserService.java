package service;

import dataaccess.*;
import exception.ResponseException;
import request_result.*;
import model.UserData;
import model.AuthData;

import java.util.Objects;

public class UserService
{
    private final UserDAO users;
    private final AuthDAO auth;

    public UserService(UserDAO users, AuthDAO auth)
    {
        this.users = users;
        this.auth = auth;
    }

    public RegisterResult register(RegisterRequest r) throws ResponseException
    {
        UserData new_user = new UserData(r.username(), r.password(), r.email());
        String token = AuthDAO.generateToken();
        try {
            users.createUser(new_user);
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(403, "Error: Forbidden Action");
        }
        try {
            auth.createAuth(new AuthData(token,r.username()));
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Unauthorized Access");
        }
        return new RegisterResult(r.username(), token);
    }

    public LoginResult login(LoginRequest r) throws ResponseException
    {
        UserData user;
        String token = AuthDAO.generateToken();
        // Is all the data accounted for?

        try
        {
            user = users.getUser(r.username());
            if (!Objects.equals(user.password(), r.password()))
            {
                throw new ResponseException(401, "Error: Invalid User");
            }
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Invalid User");//            return new LoginResult(null, null, "Error: Unable to find User");
        }
        try {
            auth.createAuth(new AuthData(token,r.username()));
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Unauthorized Access");//            return new LoginResult(null, null, "Error: Unable to create Auth");
        }
        return new LoginResult(r.username(), token);
    }

    public LogoutResult logout(LogoutRequest r) throws ResponseException
    {
        AuthData current_auth;
        try
        {
            current_auth = auth.getAuth(r.authToken());
            try
            {
                auth.deleteAuth(current_auth);
                return new LogoutResult();
            }
            catch (DataAccessException e)
            {
                throw new ResponseException(401, "Error: Invalid Authorization");
            }
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Unauthorized Access");
        }
    }
}
