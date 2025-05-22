package edu.ntnu.bidata.idatt.utils.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.model.logic.action.BackToStartAction;
import edu.ntnu.bidata.idatt.model.logic.action.LadderAction;
import edu.ntnu.bidata.idatt.model.logic.action.SkipTurnAction;
import edu.ntnu.bidata.idatt.model.logic.action.SnakeAction;
import edu.ntnu.bidata.idatt.utils.exceptions.BoardParsingException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class GsonBoardFileHandler implements FileHandler<Board> {

  Logger logger = Logger.getLogger(GsonBoardFileHandler.class.getName());

  @Override
  public void writeToFile(List<Board> boards, String filePath) throws IOException {
    if (boards == null || boards.isEmpty() || filePath == null || filePath.isBlank()) {
      throw new IllegalArgumentException("Invalid board or file path");
    }
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))) {
      Gson gson = new GsonBuilder()
          .setPrettyPrinting()
          .create();
      JsonArray boardsArray = new JsonArray();
      boards.forEach(board -> {
        boardsArray.add(serializeBoardToJson(board));
      });
      String jsonString = gson.toJson(boardsArray);
      logger.log(Level.INFO, "Writing board to file: " + filePath);
      bufferedWriter.write(jsonString);
    } catch (JsonSyntaxException error) {
      throw new BoardParsingException(filePath, error);
    }
  }

  @Override
  public List<Board> readFromFile(String filePath) throws IOException {
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
      StringBuilder stringBuilder = new StringBuilder();
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        stringBuilder.append(line);
      }
      String jsonString = stringBuilder.toString();
      logger.log(Level.INFO, "Reading board from file: " + filePath);

      JsonElement jsonElement = JsonParser.parseString(jsonString);
      List<Board> boards = new ArrayList<>();
      if (jsonElement.isJsonArray()) {
        JsonArray boardsArray = jsonElement.getAsJsonArray();
        boardsArray.forEach(board -> {
          boards.add(deserializeJsonToBoard(board.toString()));
        });
      } else if (jsonElement.isJsonObject()) {
        boards.add(deserializeJsonToBoard(jsonElement.toString()));
      } else {
        throw new BoardParsingException(filePath, new JsonSyntaxException("Invalid JSON format"));
      }
      return boards;
    } catch (JsonSyntaxException error) {
      throw new BoardParsingException(filePath, error);
    }
  }

  public JsonElement serializeBoardToJson(Board board) {
    if (board == null) {
      return null;
    }
    JsonArray jsonArray = new JsonArray();

    board.getTiles().forEach((tileId, tile) -> {
      JsonObject tileJson = new JsonObject();
      tileJson.addProperty("tileId", tile.getTileId());
      if (tile.getNextTile() != null) {
        tileJson.addProperty("nextTileId", tile.getNextTileId());
      }

      if (tile.getLandAction() != null) {
        tileJson.addProperty("landAction", tile.getLandAction().getClass().getName());
        tileJson.addProperty("destination tile", tile.getLandAction().getDestinationTileId());
        tileJson.addProperty("description", tile.getLandAction().description());
      }
      jsonArray.add(tileJson);
    });

    JsonObject boardJson = new JsonObject();
    boardJson.addProperty("name", board.getName());
    boardJson.addProperty("description", board.getDescription());
    boardJson.add("tiles", jsonArray);
    return boardJson;
  }

  public Board deserializeJsonToBoard(String jsonString) {
    JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
    JsonArray tilesJsonArray = jsonObject.getAsJsonArray("tiles");
    Board board = new Board();

    for (JsonElement jsonElement : tilesJsonArray) {
      int id = jsonElement.getAsJsonObject().get("tileId").getAsInt();
      board.addTile(new Tile(id));
    }

    for (JsonElement jsonElement2 : tilesJsonArray) {
      JsonObject jsonObject2 = jsonElement2.getAsJsonObject();
      int tileId = jsonObject2.get("tileId").getAsInt();
      Tile tile = board.getTile(tileId);

      if (jsonObject2.has("nextTileId")) {
        tile.setNextTile(board.getTile(jsonObject2.get("nextTileId").getAsInt()));
      }

      if (jsonObject2.has("landAction")) {
        String actionClass = jsonObject2.get("landAction").getAsString();
        int dest = jsonObject2.get("destination tile").getAsInt();
        String desc = jsonObject2.has("description")
            ? jsonObject2.get("description").getAsString() : "";

        String actionName = actionClass.substring(actionClass.lastIndexOf('.') + 1);
        switch (actionName) {
          case "LadderAction":
            tile.setLandAction(new LadderAction(dest, desc));
            break;
          case "SnakeAction":
            tile.setLandAction(new SnakeAction(dest, desc));
            break;
          case "BackToStartAction":
            tile.setLandAction(new BackToStartAction(desc));
            break;
          case "SkipTurnAction":
            tile.setLandAction(new SkipTurnAction(1, desc));
            break;
          default:
            throw new IllegalArgumentException("Unknown action (error): " + actionName);
        }
      }
    }

    board.setName(jsonObject.get("name").getAsString());
    board.setDescription(jsonObject.get("description").getAsString());
    return board;
  }
}

