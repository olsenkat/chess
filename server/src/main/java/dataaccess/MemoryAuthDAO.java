package dataaccess;

import model.AuthData;

import java.util.Hashtable;

public class MemoryAuthDAO implements AuthDAO
{
    private final Hashtable<String, AuthData> authData = new Hashtable<>();
    @Override
    public AuthData getAuth(String authToken) throws DataAccessException
    {
        if (!authData.containsKey(authToken))
        {
            throw new DataAccessException("Auth Token is not found in the database.");
        }
        return authData.get(authToken);
    }

    @Override
    public AuthData createAuth(AuthData auth) throws DataAccessException {
        if (authData.containsKey(auth.authToken()))
        {
            throw new DataAccessException("Auth Token is already in the database.");
        }
        authData.put(auth.authToken(), auth);
        return authData.get(auth.authToken());
    }

    @Override
    public boolean deleteAuth(AuthData auth) throws DataAccessException {
        if (!authData.containsKey(auth.authToken()))
        {
            throw new DataAccessException("Auth Token is not found in the database.");
        }
        authData.remove(auth.authToken());
        return true;
    }

    @Override
    public void clear() {
        authData.clear();
    }
}
