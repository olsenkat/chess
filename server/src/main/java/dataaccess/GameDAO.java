package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO
{
    GameData getGame(int gameID) throws DataAccessException;

    ArrayList<GameData> listGames() throws DataAccessException;

    GameData createGame(GameData game) throws DataAccessException;

    GameData updateGame(GameData game) throws DataAccessException;

    void clear();

    default int createID()
    {
        return 0;
    }
}
