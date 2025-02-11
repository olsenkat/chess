package dataaccess;

import model.GameData;

import java.util.ArrayList;

interface GameDAO
{
    GameData getGame(int gameID);

    ArrayList<GameData> listGames();

    GameData createGame(GameData game);

    GameData updateGame(GameData game);

    GameData clear();
}
