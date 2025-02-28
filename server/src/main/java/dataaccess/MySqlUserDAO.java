package dataaccess;

import com.google.gson.Gson;
import exception.ResponseException;
import model.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlUserDAO implements UserDAO{

    public MySqlUserDAO() throws ResponseException
    {
        DataAccessHelper.configureDatabase(createStatements);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email, json FROM user WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readUser(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        throw new DataAccessException("Username is not found in the database.");
    }

    @Override
    public UserData createUser(UserData user) throws DataAccessException {
        var statement = "INSERT INTO user (username, password, email, json) VALUES (?, ?, ?, ?)";
        var json = new Gson().toJson(user);
        try
        {
            DataAccessHelper.executeUpdate(statement, user.username(), user.password(), user.email(), json);

        } catch (ResponseException e)
        {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
        return new UserData(user.username(), user.password(), user.email());
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE user";
        try {
            DataAccessHelper.executeUpdate(statement);
        } catch (ResponseException e)
        {
            throw new DataAccessException(String.format("Unable to delete data: %s", e.getMessage()));
        }
    }

    private UserData readUser(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        return new Gson().fromJson(json, UserData.class);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              `username` varchar(256) NOT NULL,
              `password` varchar(256) NOT NULL,
              `email` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
