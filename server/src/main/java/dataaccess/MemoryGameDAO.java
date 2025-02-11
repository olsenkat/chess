package dataaccess;

import model.GameData;

import java.util.ArrayList;
import java.util.Hashtable;

public class MemoryGameDAO implements GameDAO{
    private final Hashtable<Integer, GameData> gameData = new Hashtable<>();
    private int currentID = 0;
    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        Integer id = gameID;
        if (gameData.containsKey(id))
        {
            return gameData.get(id);
        }
        throw new DataAccessException("Game ID is not found in the database.");
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> games = new ArrayList<>();
        for (Integer key : gameData.keySet())
        {
            games.add(gameData.get(key));
        }
        return games;
    }

    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        Integer gameID = game.gameID();
        if (gameData.containsKey(gameID))
        {
            throw new DataAccessException("Game ID is already taken");

        }
        gameData.put(gameID, game);
        return gameData.get(gameID);
    }

    @Override
    public GameData updateGame(GameData game) throws DataAccessException {
        Integer gameID = game.gameID();
        if (!gameData.containsKey(gameID))
        {
            throw new DataAccessException("Game ID is invalid");
        }
        gameData.put(gameID, game);
        return gameData.get(gameID);
    }

    @Override
    public void clear() {
        gameData.clear();
    }

    @Override
    public int createID()
    {
        return currentID++;
    }
}
