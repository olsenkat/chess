package dataaccess;

import model.UserData;

import java.util.Hashtable;

public class MemoryUserDAO  implements UserDAO
{
    private Hashtable<String, UserData> userData = new Hashtable<>();

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (userData.containsKey(username))
        {
            return userData.get(username);
        }
        throw new DataAccessException("Username is not found in the database.");
    }

    @Override
    public UserData createUser(UserData user) throws DataAccessException {
        if (userData.containsKey(user.username()))
        {
            throw new DataAccessException("Username is taken");
        }
        userData.put(user.username(), user);
        return userData.get(user.username());
    }

    @Override
    public boolean clear() {
        userData.clear();
        return true;
    }

}
