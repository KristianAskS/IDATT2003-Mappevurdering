package edu.ntnu.bidata.idatt.utils;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.model.logic.action.BackToStartAction;
import edu.ntnu.bidata.idatt.model.logic.action.LadderAction;
import edu.ntnu.bidata.idatt.model.logic.action.SkipTurnAction;
import edu.ntnu.bidata.idatt.model.logic.action.SnakeAction;
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
 * Generates different predefined board types and serializes each to its own JSON file.
 */
public class GenerateBoardJson {

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

  private static Board generateLadderRush() {
    Board board = new Board();
    board.setName("LADDER RUSH");
    board.setDescription("60‑tile – 15 ladders, NO obstacles!");
    int tileCount = 60;
    makeTiles(board, tileCount);

    int ladderCount = 15;
    Random random = new Random();
    Map<Integer, Integer> ladders = new HashMap<>();
    Set<Integer> starts = new HashSet<>();

    while (ladders.size() < ladderCount) {
      int start = random.nextInt(58) + 1;
      int end = random.nextInt(tileCount - start) + start + 1;
      if (starts.add(start)) {
        ladders.put(start, end);
      }
    }
    ladders = Collections.unmodifiableMap(ladders);
    applyLadders(board, ladders);
    return board;
  }

  private static Board generateClassicSnakesAndLadders() {
    Board board = new Board();
    board.setName("Classic Snakes & Ladders");
    board.setDescription("100‑tile – traditional snakes and ladders");
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

  private static Board generateFunBoard() {
    Board board = new Board();
    board.setName("MORE CHAOS");
    board.setDescription("100‑tile - with even more chaos!!!!!!");
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

  private static Board generateSkipAndBack() {
    Board board = new Board();
    board.setName("SKIP & BACK");
    board.setDescription("100‑tile – mix of skip-turn and back-to-start");
    int totalTiles = 73;
    makeTiles(board, totalTiles);
    for (int i = 5; i <= totalTiles; i += 4) {
      board.getTile(i)
          .setLandAction(new SkipTurnAction(1, "Skip one turn on tile " + i));

      board.getTile(i - (int) (Math.random() * 3) - 1)
          .setLandAction(new BackToStartAction("Back to start on tile 30!"));
    }

    return board;
  }

  private static Board generateRandomBoard(int tileCount, int ladderCount, int snakeCount) {
    Board board = new Board();
    board.setName("Random Board");
    board.setDescription(String.format(
        "%d‑tile – %d ladders, %d snakes randomly placed",
        tileCount, ladderCount, snakeCount
    ));
    makeTiles(board, tileCount);
    Random rnd = new Random();
    Set<Integer> used = new HashSet<>();
    for (int i = 0; i < ladderCount; i++) {
      int start;
      do {
        start = rnd.nextInt(tileCount - 1) + 1;
      } while (used.contains(start));
      int end = rnd.nextInt(tileCount - start) + start + 1;
      applyAction(board, start, new LadderAction(end,
          "Ladder from " + start + " to " + end));
      used.add(start);
    }
    for (int i = 0; i < snakeCount; i++) {
      int start;
      do {
        start = rnd.nextInt(tileCount - 1) + 2;
      } while (used.contains(start));
      int end = rnd.nextInt(start - 1) + 1;
      applyAction(board, start, new SnakeAction(end,
          "Snake from " + start + " to " + end));
      used.add(start);
    }
    return board;
  }

  private static void makeTiles(Board board, int count) {
    for (int i = 1; i <= count; i++) {
      board.addTile(new Tile(i));
      if (i > 1) {
        board.getTile(i - 1).setNextTile(board.getTile(i));
      }
    }
  }

  private static void applyLadders(Board board, Map<Integer, Integer> ladders) {
    ladders.forEach((start, end) ->
        applyAction(board, start,
            new LadderAction(end, String.format("Ladder %d to %d", start, end)))
    );
  }

  private static void applySnakes(Board board, Map<Integer, Integer> snakes) {
    snakes.forEach((start, end) ->
        applyAction(board, start,
            new SnakeAction(end, String.format("Snake %d to %d", start, end)))
    );
  }

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
