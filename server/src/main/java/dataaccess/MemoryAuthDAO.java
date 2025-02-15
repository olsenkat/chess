package dataaccess;

import model.AuthData;

import java.util.Hashtable;

public class MemoryAuthDAO implements AuthDAO
{
    // Make database list of authorizations
    private final Hashtable<String, AuthData> authData = new Hashtable<>();

    // Get Authorization from memory
    @Override
    public AuthData getAuth(String authToken) throws DataAccessException
    {
        // Make sure the auth token is found in the database
        if (!authData.containsKey(authToken))
        {
            throw new DataAccessException("Auth Token is not found in the database.");
        }
        return authData.get(authToken);
    }

    // Create authorization
    @Override
    public AuthData createAuth(AuthData auth) throws DataAccessException {
        // If the username is in the database, raise exception
        if (authData.containsKey(auth.authToken()))
        {
            throw new DataAccessException("Auth Token is already in the database.");
        }

        /* The following would be a check if we wanted Users to not be able to log in more than once
        for (AuthData data: authData.values())
        {
            if (Objects.equals(data.username(), auth.username()))
            {
                throw new DataAccessException("User is already logged in");
            }
        }
        */

        // Put the token in the database
        authData.put(auth.authToken(), auth);
        return authData.get(auth.authToken());
    }

    @Override
    public boolean deleteAuth(AuthData auth) throws DataAccessException {
        // See if the auth token is found in the database
        if (!authData.containsKey(auth.authToken()))
        {
            throw new DataAccessException("Auth Token is not found in the database.");
        }
        // remove the auth token from the database
        authData.remove(auth.authToken());
        return true;
    }

    // Clears all entries
    @Override
    public void clear() {
        authData.clear();
    }
}
