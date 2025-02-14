package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Hashtable;

public class MemoryGameDAO implements GameDAO{
    // Hashtable for database which holds all gameData.
    private final Hashtable<Integer, GameData> gameData = new Hashtable<>();
    private int currentID = 1; // ID to increment for games

    // Method to get game from the database
    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        Integer id = gameID;
        // If the database contains the key, access the game data and return it
        if (gameData.containsKey(id))
        {
            return gameData.get(id);
        }
        // If the database does not contain the key, raise an exception
        throw new DataAccessException("Game ID is not found in the database.");
    }

    // Method to list all games in the database
    @Override
    public ArrayList<GameData> listGames() {
        // Create an array list to return
        ArrayList<GameData> games = new ArrayList<>();
        // For each key in the database, add its value to the ArrayList
        for (Integer key : gameData.keySet())
        {
            games.add(gameData.get(key));
        }
        return games;
    }

    // Method to create a game and add it to the database
    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        // Access the game ID
        Integer gameID = game.gameID();

        // If the database already has this ID, raise an exception
        if (gameData.containsKey(gameID))
        {
            throw new DataAccessException("Game ID is already taken");

        }

        // Put the game into the database
        gameData.put(gameID, game);
        return gameData.get(gameID);
    }

    // Method updates the current game
    @Override
    public GameData updateGame(GameData game) throws DataAccessException {
        // Access the gameID
        Integer gameID = game.gameID();

        // If the gameID is not present in the database, throw exception
        if (!gameData.containsKey(gameID))
        {
            throw new DataAccessException("Game ID is invalid");
        }

        // Remove the old data, and put the game back into the database
        gameData.remove(gameID);
        gameData.put(gameID, game);
        return gameData.get(gameID);
    }

    // Method to clear out all data from database
    @Override
    public void clear() {
        gameData.clear();
    }

    // Method to increment gameID
    @Override
    public int createID()
    {
        return currentID++;
    }
}
