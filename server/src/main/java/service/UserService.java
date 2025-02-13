package service;

import dataaccess.*;
import exception.ResponseException;
import request_result.*;
import model.UserData;
import model.AuthData;

import java.util.Objects;

public class UserService
{
    // Create instances of the database to access it
    private final UserDAO users;
    private final AuthDAO auth;

    // Create a constructor
    public UserService(UserDAO users, AuthDAO auth)
    {
        this.users = users;
        this.auth = auth;
    }

    // Register users
    public RegisterResult register(RegisterRequest r) throws ResponseException
    {
        // Create a new user with given data
        UserData new_user = new UserData(r.username(), r.password(), r.email());
        // Generate an Auth Token
        String token = AuthDAO.generateToken();

        // If any of the register fields are null, there is an error
        if (r.username()==null || r.password()==null || r.email()==null)
        {
            throw new ResponseException(400, "Error: Bad Request");
        }

        // Try creating the user
        try {
            users.createUser(new_user);
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(403, "Error: Forbidden Action");
        }

        // Try creating the Authorization
        try {
            auth.createAuth(new AuthData(token,r.username()));
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Unauthorized Access");
        }

        // Create the RegisterResult and return it
        return new RegisterResult(r.username(), token);
    }

    // Login user
    public LoginResult login(LoginRequest r) throws ResponseException
    {
        // Create user instance to access later in the method
        UserData user;
        // Generate an Auth Token
        String token = AuthDAO.generateToken();

        // Check that all the data is accounted for
        if (r.username()==null || r.password()==null)
        {
            throw new ResponseException(400, "Error: Bad Request");
        }

        // Try getting the user from the database
        try
        {
            user = users.getUser(r.username());
            // If the passwords don't match, raise exception
            if (!Objects.equals(user.password(), r.password()))
            {
                throw new ResponseException(401, "Error: Invalid User");
            }
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Invalid User");//            return new LoginResult(null, null, "Error: Unable to find User");
        }

        // Try creating the authorization
        try {
            auth.createAuth(new AuthData(token,r.username()));
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Unauthorized Access");//            return new LoginResult(null, null, "Error: Unable to create Auth");
        }

        // Create new LoginResult instance and return it
        return new LoginResult(r.username(), token);
    }

    // Logout User
    public LogoutResult logout(LogoutRequest r) throws ResponseException
    {
        // Create a current_auth variable to access later in the code
        AuthData current_auth;
        // Try getting the authorization
        try
        {
            current_auth = auth.getAuth(r.authToken());
            // Try deleting the authorization and returning the LogoutResult
            try
            {
                auth.deleteAuth(current_auth);
                return new LogoutResult();
            }
            // If the deletion doesn't work, raise a ResponseException
            catch (DataAccessException e)
            {
                throw new ResponseException(401, "Error: Invalid Authorization");
            }
        }
        // If the getAuth doesn't work, raise a ResponseException
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Unauthorized Access");
        }
    }
}
