package service;

import chess.ChessGame;
import dataaccess.*;
import exception.ResponseException;
import model.GameData;
import request_result.*;

import java.util.Locale;
import java.util.Objects;

public class GameService
{
    // Create objects to hold auth data and game data
    private final AuthDAO auth;
    private final GameDAO games;

    // Added constructor to set above variables
    public GameService(AuthDAO auth, GameDAO games)
    {
        this.auth = auth;
        this.games = games;
    }

    // Create a game
    public CreateResult create(CreateRequest r) throws ResponseException
    {
        // Create new game ID
        int gameID = games.createID();

        // Try getting the authorization
        try
        {
            auth.getAuth(r.authToken());
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Invalid Authorization");
        }

        // Try creating the chess game
        try
        {
            ChessGame newGame = new ChessGame();
            games.createGame(new GameData(gameID, null, null, r.gameName(), newGame));
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Unable to create game");
        }

        // return the CreateResult, which contains the gameID
        return new CreateResult(gameID);
    }

    // Join a game
    public JoinResult join(JoinRequest r) throws ResponseException
    {
        // Set variables for use later in the code
        GameData currentGame;
        String username;
        // If the gameID is null, it is invalid, raise an exception
        if (r.gameID()==null)
        {
            throw new ResponseException(400, "Error: Invalid ID");
        }

        // Set the gameID
        int gameID = r.gameID();

        // Try getting the authorization
        try
        {
            username = auth.getAuth(r.authToken()).username();
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Invalid Authorization");
        }

        // Try getting the game from the data access class
        try
        {
            currentGame = games.getGame(gameID);
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Invalid Game ID");
        }

        // Check the player color is not null
        if (r.playerColor()==null)
        {
            throw new ResponseException(400, "Error: No color specified");
        }

        // Try to check if the desired player color is taken
        try {
            // If the desired player color is white:
            if (r.playerColor().toLowerCase(Locale.ROOT).equals("white")) {
                // If the white username does not equal to null, the color is already taken, raise exception
                if (!Objects.equals(currentGame.whiteUsername(), null))
                {
                    throw new ResponseException(403, "Error: Team Color already taken");
                }
                // Else update the game with the new white username
                games.updateGame(new GameData(r.gameID(), username, currentGame.blackUsername(), currentGame.gameName(), currentGame.game()));
            }
            // If the desired player color is black:
            else if (r.playerColor().toLowerCase(Locale.ROOT).equals("black"))
            {
                // If the black username != null, the color is taken, raise exception
                if(!Objects.equals(currentGame.blackUsername(), null))
                {
                    throw new ResponseException(403, "Error: Team Color already taken");
                }
                // Else update the game with the new black username
                games.updateGame(new GameData(r.gameID(), currentGame.whiteUsername(),username, currentGame.gameName(), currentGame.game()));

            }
            // If the color is not white or black, there is an issue
            else
            {
                throw new ResponseException(400, "Error: Invalid Color");
            }
        }
        // If we caught any exceptions in this block, raise a 401 exception
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Unable to access game");
        }
        return new JoinResult();
    }

    // List Games
    public ListResult list(ListRequest r) throws ResponseException
    {
        // Try getting the authorization from the database
        try
        {
            auth.getAuth(r.authToken());
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Invalid Authorization");
        }

        // Try getting all game data from the database
        try
        {
            // Return a new ListResult object with the list of all games
            return new ListResult(games.listGames());
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Invalid List Game Result");
        }

    }
}
