package dataaccess;

import model.UserData;

import java.util.Hashtable;

public class MemoryUserDAO  implements UserDAO
{
    // Database Hashtable to hold userData
    private final Hashtable<String, UserData> userData = new Hashtable<>();

    // Method intended to retrieve user from database
    @Override
    public UserData getUser(String username) throws DataAccessException {
        // If the database contains the username, return the data retrieved
        if (userData.containsKey(username))
        {
            return userData.get(username);
        }
        // Else: throw an exception
        throw new DataAccessException("Username is not found in the database.");
    }

    // Add the new user to the database
    @Override
    public UserData createUser(UserData user) throws DataAccessException {
        // If the database already has this user, raise exception
        if (userData.containsKey(user.username()))
        {
            throw new DataAccessException("Username is taken");
        }

        // Put the username into the database
        userData.put(user.username(), user);
        return userData.get(user.username());
    }

    // Clear the database
    @Override
    public void clear() {
        userData.clear();
    }

}
