package request_result;

import model.GameData;

import java.util.ArrayList;

public record ListResult  (ArrayList<GameData> games, String message)
{ }
