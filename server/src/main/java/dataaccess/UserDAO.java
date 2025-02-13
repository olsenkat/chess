package dataaccess;

import model.UserData;

import javax.xml.crypto.Data;

public interface UserDAO
{
    UserData getUser(String username) throws DataAccessException;

    UserData createUser(UserData user) throws DataAccessException;

    void clear();
}
