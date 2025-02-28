package server;

import com.google.gson.Gson;
import exception.ErrorResponse;
import exception.ResponseException;
import model.GameData;
import model.UserData;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public UserData registerUser()
    {
        return null;
    }

    public UserData loginUser()
    {
        return null;
    }

    public UserData logoutUser()
    {
        return null;
    }

    public GameData createGame()
    {
        return null;
    }

    public ArrayList<GameData> listGames()
    {
        return null;
    }

    public GameData joinGame()
    {
        return null;
    }

    public void clear()
    {

    }


}
