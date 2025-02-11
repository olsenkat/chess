package dataaccess;

import model.AuthData;

interface AuthDAO
{
    AuthData getAuth(AuthData auth) throws DataAccessException;

    AuthData createAuth(AuthData auth) throws DataAccessException;

    AuthData deleteAuth(AuthData auth) throws DataAccessException;

    AuthData clear();
}
