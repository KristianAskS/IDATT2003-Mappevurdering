package edu.ntnu.bidata.idatt.utils;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.model.logic.action.BackToStartAction;
import edu.ntnu.bidata.idatt.model.logic.action.LadderAction;
import edu.ntnu.bidata.idatt.model.logic.action.SkipTurnAction;
import edu.ntnu.bidata.idatt.model.logic.action.SnakeAction;
import edu.ntnu.bidata.idatt.model.logic.action.TileAction;
import edu.ntnu.bidata.idatt.utils.io.GsonBoardFileHandler;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * <p>Utility for generating predefined {@link Board} configurations
 * and serializing each to a JSON file using {@link GsonBoardFileHandler}.</p>
 *
 * @author Tri Tac Le
 * @since 1.0
 */
public class GenerateBoardJson {

  /**
   * <p>Main entry point. Generates a list of boards and writes each one
   * to a JSON file under the "./data/games" directory.</p>
   * Chatgpt helped with the string manipulation
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    String outputDir = "./data/games";
    List<Board> boards = List.of(
        generateLadderRush(),
        generateSkipAndBack(),
        generateFunBoard()
    );

    GsonBoardFileHandler handler = new GsonBoardFileHandler();
    for (Board board : boards) {
      String fileName = board.getName().toLowerCase()
          .replaceAll("[^a-z0-9]+", "-")
          .replaceAll("(^-|-$)", "")
          + ".json";
      Path outPath = Paths.get(outputDir, fileName);
      try {
        handler.writeToFile(Collections.singletonList(board), outPath.toString());
        System.out.println("Wrote board '" + board.getName() + "' to " + outPath);
      } catch (IOException e) {
        System.err.println("Failed to write " + outPath + ": " + e.getMessage());
      }
    }
  }

  /**
   * Creates a 60-tile board named "LADDER RUSH" with 15 randomly placed ladders.
   *
   * @return the generated {@link Board}
   */
  private static Board generateLadderRush() {
    Board board = new Board();
    board.setName("LADDER RUSH");
    board.setDescription("60-tile – 15 ladders, NO obstacles!");
    int tileCount = 60;
    makeTiles(board, tileCount);

    int ladderCount = 15;
    Random random = new Random();
    Map<Integer, Integer> ladders = new HashMap<>();
    Set<Integer> starts = new HashSet<>();

    while (ladders.size() < ladderCount) {
      int start = random.nextInt(tileCount - 2) + 1;
      int end = random.nextInt(tileCount - start) + start + 1;
      if (starts.add(start)) {
        ladders.put(start, end);
      }
    }
    applyLadders(board, ladders);
    return board;
  }

  /**
   * Creates a classic 100-tile Snakes And Ladders board with fixed snakes and ladders.
   *
   * @return the generated {@link Board}
   */
  @SuppressWarnings("Unused")
  private static Board generateClassicSnakesAndLadders() {
    Board board = new Board();
    board.setName("Classic Snakes & Ladders");
    board.setDescription("100-tile – traditional snakes and ladders");
    makeTiles(board, 100);
    Map<Integer, Integer> ladders = Map.of(
        4, 14, 9, 31, 20, 38, 28, 84, 40, 59,
        51, 67, 63, 81, 71, 91
    );
    Map<Integer, Integer> snakes = Map.of(
        17, 7, 54, 34, 62, 19, 64, 60, 87, 24,
        93, 73, 95, 75, 98, 79
    );
    applyLadders(board, ladders);
    applySnakes(board, snakes);
    return board;
  }

  /**
   * Creates a 100-tile board with actions; back-to-start, skip-turn,
   * ladders and snakes
   *
   * @return the generated {@link Board}
   */
  private static Board generateFunBoard() {
    Board board = new Board();
    board.setName("MORE CHAOS");
    board.setDescription("100-tile - with even more chaos!!!!!!");
    int size = 100;
    makeTiles(board, size);
    Random rnd = new Random();

    for (int i = 1; i <= size; i++) {
      Tile tile = board.getTile(i);
      if (i % 4 == 0) {
        tile.setLandAction(new BackToStartAction("Back to start"));
      } else if (i % 3 == 0) {
        tile.setLandAction(new SkipTurnAction(1, "Skip turn"));
      } else if (rnd.nextBoolean()) {
        int dest = rnd.nextInt(size - i) + i + 1;
        tile.setLandAction(new LadderAction(dest, "Ladder " + i + " to " + dest));
      } else {
        int dest = rnd.nextInt(i);
        tile.setLandAction(new SnakeAction(dest, "Snake " + i + " to " + dest));
      }
    }
    return board;
  }

  /**
   * Creates a 73-tile board named "SKIP & BACK" with skip-turn every 4th tile
   * and occasional back-to-start actions.
   *
   * @return the generated {@link Board}
   */
  private static Board generateSkipAndBack() {
    Board board = new Board();
    board.setName("SKIP & BACK");
    board.setDescription("100-tile – mix of skip-turn and back-to-start");
    int totalTiles = 73;
    makeTiles(board, totalTiles);
    for (int i = 5; i <= totalTiles; i += 4) {
      board.getTile(i)
          .setLandAction(new SkipTurnAction(1, "Skip one turn on tile " + i));
      int backTile = i - (new Random().nextInt(3) + 1);
      board.getTile(backTile)
          .setLandAction(new BackToStartAction("Back to start on tile " + backTile));
    }
    return board;
  }

  /**
   * Creates a board with a specified number of tiles, and random ladders and snakes.
   *
   * @param tileCount   total number of tiles to generate
   * @param ladderCount number of ladders
   * @param snakeCount  number of snakes
   * @return the generated {@link Board}
   */
  @SuppressWarnings("Unused")
  private static Board generateRandomBoard(int tileCount, int ladderCount, int snakeCount) {
    Board board = new Board();
    board.setName("Random Board");
    board.setDescription(String.format(
        "%d-tile – %d ladders, %d snakes randomly placed",
        tileCount, ladderCount, snakeCount
    ));
    makeTiles(board, tileCount);
    Random rnd = new Random();
    Set<Integer> used = new HashSet<>();
    for (int i = 0; i < ladderCount; i++) {
      int start;
      do {
        start = rnd.nextInt(tileCount - 1) + 1;
      } while (!used.add(start));
      int end = rnd.nextInt(tileCount - start) + start + 1;
      applyAction(board, start, new LadderAction(end,
          "Ladder from " + start + " to " + end));
    }
    for (int i = 0; i < snakeCount; i++) {
      int start;
      do {
        start = rnd.nextInt(tileCount - 1) + 2;
      } while (!used.add(start));
      int end = rnd.nextInt(start - 1) + 1;
      applyAction(board, start, new SnakeAction(end,
          "Snake from " + start + " to " + end));
    }
    return board;
  }

  /**
   * Adds tiles numbered 1 through {@code count} to the {@link Board},
   * linking each tile.
   *
   * @param board the {@link Board} to populate
   * @param count the number of tiles to generate
   */
  private static void makeTiles(Board board, int count) {
    for (int i = 1; i <= count; i++) {
      board.addTile(new Tile(i));
      if (i > 1) {
        board.getTile(i - 1).setNextTile(board.getTile(i));
      }
    }
  }

  /**
   * Applies ladder actions based on the provided map of start to end positions.
   *
   * @param board   the {@link Board} to modify
   * @param ladders a map where keys are start tile ids and values are end tile ids
   */
  private static void applyLadders(Board board, Map<Integer, Integer> ladders) {
    ladders.forEach((start, end) ->
        applyAction(board, start,
            new LadderAction(end, String.format("Ladder %d to %d", start, end)))
    );
  }

  /**
   * Applies snake actions based on the provided map of head to tail positions.
   *
   * @param board  the {@link Board} to modify
   * @param snakes a map where keys are snake head ids and values are tail ids
   */
  private static void applySnakes(Board board, Map<Integer, Integer> snakes) {
    snakes.forEach((start, end) ->
        applyAction(board, start,
            new SnakeAction(end, String.format("Snake %d to %d", start, end)))
    );
  }

  /**
   * Sets the specified {@code action} on the tile with the given id.
   *
   * @param board  the {@link Board} containing the tiles
   * @param tileId the id of the tile to modify
   * @param action the action to apply; must be an instance of a {@link TileAction}
   */
  private static void applyAction(Board board, int tileId, Object action) {
    Tile tile = board.getTile(tileId);
    if (tile == null) {
      return;
    }
    if (action instanceof LadderAction) {
      tile.setLandAction((LadderAction) action);
    } else if (action instanceof SnakeAction) {
      tile.setLandAction((SnakeAction) action);
    } else if (action instanceof SkipTurnAction) {
      tile.setLandAction((SkipTurnAction) action);
    } else if (action instanceof BackToStartAction) {
      tile.setLandAction((BackToStartAction) action);
    }
  }
}
