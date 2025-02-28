package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlAuthDAO implements AuthDAO{

    public MySqlAuthDAO() throws ResponseException
    {
        DataAccessHelper.configureDatabase(createStatements);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken, username, json FROM auth WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return readAuth(rs);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        throw new DataAccessException("Auth Token is not found in the database.");
    }

    @Override
    public AuthData createAuth(AuthData auth) throws DataAccessException {
        int id;
        var statement = """
                        INSERT INTO auth
                        (authToken, username, json)
                        VALUES (?, ?, ?)
                        """;
        var json = new Gson().toJson(auth);
        try
        {
            DataAccessHelper.executeUpdate(statement, auth.authToken(),
                    auth.username(), json);
        }
        catch (ResponseException e)
        {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }

        return new AuthData(auth.authToken(),
                auth.username());
    }

    @Override
    public boolean deleteAuth(AuthData auth) throws DataAccessException {
        var statement = "DELETE FROM auth WHERE authToken=?";
        getAuth(auth.authToken());
        if (auth.authToken()==null)
        {
            throw new DataAccessException("Auth Token is null");
        }
        try {
            DataAccessHelper.executeUpdate(statement, auth.authToken());
        } catch (ResponseException e)
        {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
        return true;
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE auth";
        try {
            DataAccessHelper.executeUpdate(statement);
        } catch (ResponseException e)
        {
            throw new DataAccessException(String.format("Unable to delete data: %s", e.getMessage()));
        }
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        return new Gson().fromJson(json, AuthData.class);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  auth (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`authToken`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
