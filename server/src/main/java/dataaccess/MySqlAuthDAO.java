package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.AuthData;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlAuthDAO implements AuthDAO{

    public MySqlAuthDAO() throws ResponseException
    {
        configureDatabase();
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
            executeUpdate(statement, auth.authToken(),
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
            executeUpdate(statement, auth.authToken());
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
            executeUpdate(statement);
        } catch (ResponseException e)
        {
            throw new DataAccessException(String.format("Unable to delete data: %s", e.getMessage()));
        }
    }

    private AuthData readAuth(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        return new Gson().fromJson(json, AuthData.class);
    }

    private String executeUpdate(String statement, Object... params) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    switch (param) {
                        case String p -> ps.setString(i + 1, p);
                        case Integer p -> ps.setInt(i + 1, p);
                        case ChessGame p -> ps.setString(i + 1, p.toString());
                        case null -> ps.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getString(1);
                }

                return "0";
            }
        } catch (SQLException | DataAccessException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
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

    private void configureDatabase() throws ResponseException
    {
        try
        {
            DatabaseManager.createDatabase();
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(500, String.format("Unable to create database: %s", e.getMessage()));
        }

        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException | DataAccessException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
