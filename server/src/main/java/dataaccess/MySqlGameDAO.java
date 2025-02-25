package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;

import java.sql.*;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class MySqlGameDAO implements GameDAO{

    public MySqlGameDAO() throws ResponseException
    {
        configureDatabase();
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        var conn = DatabaseManager.getConnection();
        var statement = """
                        SELECT gameID, whiteUsername, blackUsername,
                        gameName, game FROM game WHERE gameID = ?
                        """;
        try (var ps = conn.prepareStatement(statement))
        {
            ps.setInt(1, gameID);
            try (var rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    return readGame(rs);
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = """
                        SELECT gameID, whiteUsername, blackUsername,
                        gameName, game FROM game
                        """;
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(readGame(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return result;
    }

    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        int id;
        var statement = """
                        INSERT INTO game
                        (whiteUsername, blackUsername, gameName, game)
                        VALUES (?, ?, ?, ?)
                        """;
        var json = new Gson().toJson(game);
        try
        {
            id = executeUpdate(statement, game.whiteUsername(),
                    game.blackUsername(), game.gameName(), json);
        }
        catch (ResponseException e)
        {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }

        return new GameData(id, game.whiteUsername(), game.blackUsername(),
                game.gameName(), game.game());
    }

    @Override
    public GameData updateGame(GameData game) throws DataAccessException {
        int id;
        var statement = """
                        UPDATE game
                        SET whiteUsername = ?,
                            blackUsername = ?,
                            gameName = ?,
                            game = ?)
                        WHERE gameID = ?
                        """;
        var json = new Gson().toJson(game);
        try
        {
            id = executeUpdate(statement, game.whiteUsername(),
                    game.blackUsername(), game.gameName(), json,
                    game.gameID());
        }
        catch (ResponseException e)
        {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }

        return new GameData(id, game.whiteUsername(), game.blackUsername(),
                game.gameName(), game.game());
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE game";
        try {
            executeUpdate(statement);
        } catch (ResponseException e)
        {
            throw new DataAccessException(String.format("Unable to delete data: %s", e.getMessage()));
        }
    }

    @Override
    public int createID() {
        return GameDAO.super.createID();
    }

    private GameData readGame(ResultSet rs) throws SQLException {
        var json = rs.getString("json");
        return new Gson().fromJson(json, GameData.class);
    }

    private int executeUpdate(String statement, Object... params) throws ResponseException {
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
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException | DataAccessException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  game (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) NOT NULL,
              `blackUsername` varchar(256) NOT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`)
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
