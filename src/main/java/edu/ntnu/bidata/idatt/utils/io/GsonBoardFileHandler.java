package edu.ntnu.bidata.idatt.utils.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import edu.ntnu.bidata.idatt.model.Board;
import edu.ntnu.bidata.idatt.model.Tile;
import edu.ntnu.bidata.idatt.utils.exceptions.BoardParsingException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
      JsonElement boardJson = serializeBoardToJson(boards.getFirst());
      Gson gson = new GsonBuilder()
          .setPrettyPrinting()
          .create();
      String jsonString = gson.toJson(boardJson);
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
      return (List<Board>) deserializeJsonToBoard(jsonString);
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
        tileJson.addProperty("description", tile.getLandAction().getDescription());
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
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    if (jsonElement == null || !jsonElement.isJsonObject()) {
      return null;
    }
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    JsonArray jsonArray = jsonObject.getAsJsonArray("tiles");
    Board board = new Board();

    for (JsonElement tileJson : jsonArray) {
      JsonObject tileJsonObject = tileJson.getAsJsonObject();
      int tileId = tileJsonObject.get("tileId").getAsInt();
      Tile tile = new Tile(tileId);
      board.addTile(tile);
    }
    for (JsonElement tileJson : jsonArray) {
      JsonObject tileJsonObject = tileJson.getAsJsonObject();
      int tileId = tileJsonObject.get("tileId").getAsInt();
      Tile tile = board.getTileId(tileId);
      if (tileJsonObject.has("nextTileId")) {
        int nextTileId = tileJsonObject.get("nextTileId").getAsInt();
        Tile nextTile = board.getTileId(nextTileId);
        tile.setNextTile(nextTile);
      }
    }
    return board;
  }
}

