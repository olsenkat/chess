package dataaccess;

import model.AuthData;

interface AuthDAO
{
    AuthData getAuth(String authToken) throws DataAccessException;

    AuthData createAuth(AuthData auth) throws DataAccessException;

    boolean deleteAuth(AuthData auth) throws DataAccessException;

    void clear();
}
