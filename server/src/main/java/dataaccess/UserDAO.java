package dataaccess;

import model.UserData;

import javax.xml.crypto.Data;

interface UserDAO
{
    UserData getUser(String username) throws DataAccessException;

    UserData createUser(UserData user) throws DataAccessException;

    UserData clear();
}
