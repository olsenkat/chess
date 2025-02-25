package service;

import dataaccess.*;
import exception.ResponseException;
import org.mindrot.jbcrypt.BCrypt;
import requestresult.*;
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
        UserData newUser = new UserData(r.username(), r.password(), r.email());

        // Generate an Auth Token
        String token = AuthDAO.generateToken();

        // If any of the register fields are null, there is an error
        if (r.username()==null || r.password()==null || r.email()==null)
        {
            throw new ResponseException(400, "Error: Bad Request");
        }

        // Try creating the user
        createUser(newUser);

        // Try creating the Authorization
        createAuth(token, r.username());

        // Create the RegisterResult and return it
        return new RegisterResult(r.username(), token);
    }

    // Login user
    public LoginResult login(LoginRequest r) throws ResponseException
    {
        // Generate an Auth Token
        String token = AuthDAO.generateToken();

        // Check that all the data is accounted for
        if (r.username()==null || r.password()==null)
        {
            throw new ResponseException(400, "Error: Bad Request");
        }

        // Try getting the user from the database
        getUserFromDatabase(r.username(), r.password());

        // Try creating the authorization
        createAuth(token, r.username());

        // Create new LoginResult instance and return it
        return new LoginResult(r.username(), token);
    }

    // Logout User
    public LogoutResult logout(LogoutRequest r) throws ResponseException
    {
        // Check that all the data is accounted for
        if (r.authToken()==null)
        {
            throw new ResponseException(400, "Error: Bad Request");
        }

        // Try getting the authorization and logging out
        return getAuthAndLogout(r.authToken());
    }

    // Create the authorization
    private void createAuth(String token, String username) throws ResponseException
    {
        // Try creating the authorization
        try {
            auth.createAuth(new AuthData(token,username));
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Unauthorized Access");
        }
    }


    // Function to test creating the user and sending a response exception if invalid
    private void createUser(UserData newUser) throws ResponseException
    {
        // Try creating the user
        try {
            var encryptedPassword = encryptUserPassword(newUser.password());
            users.createUser(new UserData(newUser.username(), encryptedPassword, newUser.email()));
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(403, "Error: Forbidden Action");
        }
    }

//    // Checks to see if the passwords match
//    private void checkPasswordSame(String userPassword, String givenPassword) throws ResponseException
//    {
//        // If the passwords don't match, raise exception
//        if (!Objects.equals(userPassword, givenPassword))
//        {
//            throw new ResponseException(401, "Error: Invalid User");
//        }
//    }

    // Checks to see if the user can be taken from the database. Makes sure the passwords match.
    private void getUserFromDatabase(String username, String password) throws ResponseException
    {
        // Create user instance to access later in the method
        UserData user;

        // Try getting the user from the database
        try
        {
            user = users.getUser(username);
            verifyUser(password, user.password());
//            checkPasswordSame(user.password(), password);
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Invalid User");
        }
    }

    // Ensures the auth is valid. Returns the result of the deleteAuth function
    private LogoutResult getAuthAndLogout(String authToken) throws ResponseException
    {
        // Create a currentAuth variable to access later in the code
        AuthData currentAuth;

        // Try to get authorization and delete it
        try
        {
            currentAuth = auth.getAuth(authToken);

            // Try deleting the authorization and returning the LogoutResult
            return deleteAuth(currentAuth);

        }
        // If the getAuth doesn't work, raise a ResponseException
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Unauthorized Access");
        }
    }

    // Ensures that the current authorization can be deleted. Returns a new LogoutResult object
    private LogoutResult deleteAuth(AuthData currentAuthauth) throws ResponseException
    {
        try
        {
            auth.deleteAuth(currentAuthauth);
            return new LogoutResult();
        }
        // If the deletion doesn't work, raise a ResponseException
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Invalid Authorization");
        }
    }

    String encryptUserPassword(String clearTextPassword) {
        return BCrypt.hashpw(clearTextPassword, BCrypt.gensalt());
    }

    void verifyUser(String clearTextPassword, String hashedPassword) throws ResponseException {
        // read the previously hashed password from the database
        if (!BCrypt.checkpw(clearTextPassword, hashedPassword))
        {
            throw new ResponseException(401, "Error: Invalid User");
        }
    }
}
