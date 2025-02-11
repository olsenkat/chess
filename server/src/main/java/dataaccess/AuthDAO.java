package dataaccess;

import model.AuthData;

interface AuthDAO
{
    AuthData getAuth(AuthData auth);

    AuthData createAuth(AuthData auth);

    AuthData deleteAuth(AuthData auth);

    AuthData clear();
}
