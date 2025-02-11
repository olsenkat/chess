package dataaccess;

import model.UserData;

interface UserDAO
{
    UserData getUser(String username);

    UserData createUser(UserData user);

    UserData clear();
}
