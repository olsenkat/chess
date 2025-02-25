package service;

import chess.ChessGame;
import dataaccess.*;
import exception.ResponseException;
import model.GameData;
import requestresult.*;

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

        // Ensure the game name is not null
        checkGameName(r.gameName());

        // Ensure the auth token is not null
        checkAuthToken(r.authToken());

        // Try getting the authorization
        getAuth(r.authToken());

        // Try creating the chess game
        try
        {
            ChessGame newGame = new ChessGame();
            GameData game = games.createGame(new GameData(gameID, null, null, r.gameName(), newGame));
            gameID = game.gameID();
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
        // If the gameID is null, it is invalid, raise an exception
        checkGameID(r.gameID());

        // If player color is null, raise exception
        checkPlayerColor(r.playerColor());

        // If authorization is null, raise an exception
        checkAuthToken(r.authToken());

        // Set the gameID
        int gameID = r.gameID();

        // Try getting the authorization
        String username = getAuth(r.authToken());

        // Try getting the game from the data access class
        GameData currentGame = getGame(gameID);

        // Try to check if the desired player color is taken
        try {
            // If the desired player color is white:
            boolean whiteColorNotTaken = checkWhiteUsername(r.playerColor(), currentGame);
            boolean blackColorNotTaken = checkBlackUsername(r.playerColor(), currentGame);
            if (r.playerColor().equalsIgnoreCase("white") && whiteColorNotTaken)
            {
                games.updateGame(new GameData(r.gameID(), username, currentGame.blackUsername(), currentGame.gameName(), currentGame.game()));
            }
            else if (r.playerColor().equalsIgnoreCase("black") && blackColorNotTaken)
            {
                games.updateGame(new GameData(r.gameID(), currentGame.whiteUsername(),username, currentGame.gameName(), currentGame.game()));
            }
            else
            {
                throw new ResponseException(401, "Error: Something is wrong with the team colors");
            }
        }
        // If we caught any exceptions in this from the data access classes, raise a 401 exception
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Unable to access game");
        }
        return new JoinResult();
    }

    // List Games
    public ListResult list(ListRequest r) throws ResponseException
    {
        // Check if Auth Token is null
        checkAuthToken(r.authToken());

        // Try getting the authorization from the database
        getAuth(r.authToken());

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

    // Check if the Game ID is null
    private void checkGameID(Integer r) throws ResponseException
    {
        if (r == null)
        {
            throw new ResponseException(400, "Error: Invalid ID");
        }
    }

    // Check if the Game Name is null
    private void checkGameName(String name) throws ResponseException
    {
        if (name==null)
        {
            throw new ResponseException(401, "Error: Game name is null");
        }
    }

    // Check if the auth token is null
    private void checkAuthToken(String authToken) throws ResponseException
    {
        if (authToken == null)
        {
            throw new ResponseException(401, "Error: AuthToken is null");
        }
    }

    // Check if the data access class can get the authorization
    private String getAuth(String authToken) throws ResponseException
    {
        try
        {
            return auth.getAuth(authToken).username();
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Invalid Authorization");
        }
    }

    // Return the game given a gameID
    private GameData getGame(int gameID) throws ResponseException
    {
        try
        {
            return games.getGame(gameID);
        }
        catch (DataAccessException e)
        {
            throw new ResponseException(401, "Error: Invalid Game ID");
        }
    }

    // Check if the player color is null or invalid
    private void checkPlayerColor(String color) throws ResponseException
    {
        if (color == null)
        {
            throw new ResponseException(400, "Error: Player Color is null");
        }
        else if (!color.equalsIgnoreCase("white") && !color.equalsIgnoreCase("black"))
        {
            throw new ResponseException(400, "Error: Player Color is invalid");
        }
    }

    // Check if the white username is taken
    private boolean checkWhiteUsername (String rPlayerColor, GameData currentGame) throws ResponseException
    {
        if (rPlayerColor.equalsIgnoreCase("white")) {
            // If the white username does not equal to null, the color is already taken, raise exception
            if (!Objects.equals(currentGame.whiteUsername(), null))
            {
                throw new ResponseException(403, "Error: White Team Color already taken");
            }

            return true;
        }
        return false;
    }

    // Check if the black username is taken
    private boolean checkBlackUsername (String rPlayerColor, GameData currentGame) throws ResponseException
    {
        if (rPlayerColor.equalsIgnoreCase("black")) {
            // If the black username does not equal to null, the color is already taken, raise exception
            if (!Objects.equals(currentGame.blackUsername(), null))
            {
                throw new ResponseException(403, "Error: Black Team Color already taken");
            }

            return true;
        }
        return false;
    }
}
