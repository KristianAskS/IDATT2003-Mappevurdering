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

/**
 * <p>Handles serialization and deserialization of {@link Board} objects
 * to and from JSON files using Gson.</p>
 *
 * <p>Supports writing a list of boards as a JSON array using pretty printed</p>
 *
 * @author Tri Tac Le
 * @since 1.0
 */
public class GsonBoardFileHandler implements FileHandler<Board> {

  private final Logger logger = Logger.getLogger(GsonBoardFileHandler.class.getName());

  /**
   * Writes the given list of {@link Board} instances to the file.
   * <p>Boards are serialized into a JSON array with pretty printing.</p>
   *
   * @param boards   the list of boards to write
   * @param filePath the target JSON file path
   * @throws IllegalArgumentException if {@code boards} is empty or {@code filePath} is invalid
   * @throws IOException              if an I/O error occurs during writing
   * @throws BoardParsingException    if a JSON syntax error occurs during serialization
   */
  @Override
  public void writeToFile(List<Board> boards, String filePath) throws IOException {
    if (boards == null || boards.isEmpty() || filePath == null || filePath.isBlank()) {
      throw new IllegalArgumentException("Invalid board list or file path");
    }
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      JsonArray array = new JsonArray();
      for (Board b : boards) {
        array.add(serializeBoardToJson(b));
      }
      String json = gson.toJson(array);
      logger.log(Level.INFO, "Writing boards to file: {0}", filePath);
      writer.write(json);
    } catch (JsonSyntaxException e) {
      throw new BoardParsingException(filePath, e);
    }
  }

  /**
   * Reads one or more {@link Board} instances from the specified JSON file.
   * <p>Accepts either a JSON array or a single JSON object.</p>
   *
   * @param filePath the source JSON file path; must be non-null and non-blank
   * @return a list of deserialized {@link Board} objects
   * @throws IllegalArgumentException if {@code filePath} is invalid
   * @throws IOException              if an I/O error occurs during reading
   * @throws BoardParsingException    if the JSON format is invalid or cannot be parsed
   */
  @Override
  public List<Board> readFromFile(String filePath) throws IOException {
    if (filePath == null || filePath.isBlank()) {
      throw new IllegalArgumentException("Invalid file path");
    }
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      StringBuilder sb = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        sb.append(line);
      }
      logger.log(Level.INFO, "Reading boards from file: {0}", filePath);
      JsonElement root = JsonParser.parseString(sb.toString());
      List<Board> result = new ArrayList<>();
      if (root.isJsonArray()) {
        for (JsonElement elem : root.getAsJsonArray()) {
          result.add(deserializeJsonToBoard(elem.toString()));
        }
      } else if (root.isJsonObject()) {
        result.add(deserializeJsonToBoard(root.toString()));
      } else {
        throw new BoardParsingException(filePath, new JsonSyntaxException("Invalid JSON format"));
      }
      return result;
    } catch (JsonSyntaxException e) {
      throw new BoardParsingException(filePath, e);
    }
  }

  /**
   * Serializes a single {@link Board} into a {@link JsonElement}.
   *
   * @param board the board to serialize
   * @return a {@link JsonObject} representing the board, or null if {@code board} is null
   */
  public JsonElement serializeBoardToJson(Board board) {
    if (board == null) {
      return null;
    }
    JsonArray tilesJson = new JsonArray();
    for (Tile t : board.getTiles().values()) {
      JsonObject tileObj = new JsonObject();
      tileObj.addProperty("tileId", t.getTileId());
      if (t.getNextTile() != null) {
        tileObj.addProperty("nextTileId", t.getNextTileId());
      }
      if (t.getLandAction() != null) {
        tileObj.addProperty("landAction", t.getLandAction().getClass().getSimpleName());
        tileObj.addProperty("destinationTile", t.getLandAction().getDestinationTileId());
        tileObj.addProperty("description", t.getLandAction().description());
      }
      tilesJson.add(tileObj);
    }
    JsonObject boardObj = new JsonObject();
    boardObj.addProperty("name", board.getName());
    boardObj.addProperty("description", board.getDescription());
    boardObj.add("tiles", tilesJson);
    return boardObj;
  }

  /**
   * Deserializes a JSON string into a {@link Board} object.
   *
   * @param jsonString the JSON representation of a single board
   * @return the reconstructed {@link Board} instance
   * @throws IllegalArgumentException if an unknown action type is encountered
   */
  public Board deserializeJsonToBoard(String jsonString) {
    JsonObject obj = JsonParser.parseString(jsonString).getAsJsonObject();
    JsonArray tilesArray = obj.getAsJsonArray("tiles");
    Board board = new Board();
    for (JsonElement el : tilesArray) {
      int id = el.getAsJsonObject().get("tileId").getAsInt();
      board.addTile(new Tile(id));
    }
    for (JsonElement el : tilesArray) {
      JsonObject tileObj = el.getAsJsonObject();
      int id = tileObj.get("tileId").getAsInt();
      Tile tile = board.getTile(id);
      if (tileObj.has("nextTileId")) {
        tile.setNextTile(board.getTile(tileObj.get("nextTileId").getAsInt()));
      }
      if (tileObj.has("landAction")) {
        String action = tileObj.get("landAction").getAsString();
        int dest = tileObj.get("destinationTile").getAsInt();
        String desc = tileObj.has("description") ? tileObj.get("description").getAsString() : "";
        switch (action) {
          case "LadderAction" -> tile.setLandAction(new LadderAction(dest, desc));
          case "SnakeAction" -> tile.setLandAction(new SnakeAction(dest, desc));
          case "BackToStartAction" -> tile.setLandAction(new BackToStartAction(desc));
          case "SkipTurnAction" -> tile.setLandAction(new SkipTurnAction(1, desc));
          default -> throw new IllegalArgumentException("Unknown action: " + action);
        }
      }
    }
    board.setName(obj.get("name").getAsString());
    board.setDescription(obj.get("description").getAsString());
    return board;
  }
}
