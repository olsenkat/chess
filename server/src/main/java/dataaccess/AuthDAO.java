package dataaccess;

import model.AuthData;

import java.util.UUID;

public interface AuthDAO
{
    AuthData getAuth(String authToken) throws DataAccessException;

    AuthData createAuth(AuthData auth) throws DataAccessException;

    boolean deleteAuth(AuthData auth) throws DataAccessException;

    void clear() throws DataAccessException;

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
}
