package dataaccess;

import model.AuthData;

public class MySqlAuthDAO implements AuthDAO{
    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData createAuth(AuthData auth) throws DataAccessException {
        return null;
    }

    @Override
    public boolean deleteAuth(AuthData auth) throws DataAccessException {
        return false;
    }

    @Override
    public void clear() {

    }
}
