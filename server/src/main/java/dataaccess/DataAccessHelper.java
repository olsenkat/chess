package dataaccess;

import chess.ChessGame;
import exception.ResponseException;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DataAccessHelper {
    public static int executeUpdate(String statement, Object... params) throws ResponseException {
        String stringInUpdate = "auth";
        boolean returnString;
        returnString = statement.contains(stringInUpdate);
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                getAllParams(ps, params);
                ps.executeUpdate();
                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    if (!returnString)
                    {
                        return rs.getInt(1);
                    }
                    else
                    {
                        return 1;
                    }
                }

                return 0;
            }
        } catch (SQLException | DataAccessException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }


    }

    public static void configureDatabase(String[] createStatements) throws ResponseException
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

    public static void getAllParams(PreparedStatement ps, Object... params) throws SQLException
    {
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
    }
}
