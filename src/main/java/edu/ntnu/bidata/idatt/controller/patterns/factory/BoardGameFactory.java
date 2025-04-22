package edu.ntnu.bidata.idatt.controller.patterns.factory;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.model.logic.action.LadderAction;
import edu.ntnu.bidata.idatt.model.logic.action.SnakeAction;
import edu.ntnu.bidata.idatt.utils.io.FileHandler;
import edu.ntnu.bidata.idatt.utils.io.GsonBoardFileHandler;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Factory class to create different types of board setups for the game.
 */
public class BoardGameFactory {

  /**
   * Creates a board with the given name, description, and tile count.
   * Automatically links each tile to the next (except the last one).
   */
  public static Board createBoardTiles(String name, String description, int numberOfTiles) {
    Board board = new Board(name, description);

    IntStream.rangeClosed(1, numberOfTiles)
        .mapToObj(Tile::new)
        .forEach(board::addTile);

    IntStream.rangeClosed(1, numberOfTiles - 1)
        .forEach(i -> {
          Tile current = board.getTile(i);
          Tile next = board.getTile(i + 1);
          if (current != null && next != null) {
            current.setNextTile(next);
          }
        });

    return board;
  }

  /**
   * Creates a snake or ladder from start to end.
   */
  public static void createLadder(Board board, int start, int end) {
    Tile startTile = board.getTile(start);
    Tile endTile = board.getTile(end);
    if (startTile != null && endTile != null) {
      startTile.setNextTile(endTile);
      startTile.setLandAction(new LadderAction(end, "Ladder from " + start + " to " + end));
    }
  }

  /**
   * Not used yet
   */
  public static void createSnake(Board board, int start, int end) {
    Tile startTile = board.getTile(start);
    Tile endTile = board.getTile(end);
    if (startTile != null && endTile != null) {
      startTile.setNextTile(endTile);
      startTile.setLandAction(new SnakeAction(end, "Snake from " + start + " to " + end));
    }
  }

  /**
   * Builds the classic board with predefined ladders.
   */
  public static Board createClassicBoard() {
    Board board = createBoardTiles("Classic Board", "Classic board with 90 tiles", 90);
    createLadder(board, 7, 14);
    createLadder(board, 16, 27);
    createLadder(board, 31, 42);
    createLadder(board, 67, 74);
    return board;
  }

  /**
   * Builds a small board (for testing or shorter games).
   */
  public static Board createSmallBoard() {
    Board board = createBoardTiles("Small Board", "Small board with 30 tiles", 30);
    return board;
  }

  /**
   * Builds a standard board without any ladders/snakes.
   */
  public static Board createBoardNoLaddersAndSnakes() {
    return createBoardTiles("Classic No Actions", "Board with 90 tiles, no ladders or snakes", 90);
  }

  /**
   * Loads boards from a JSON file.
   *
   * @param filePath path to the JSON file
   * @return List of boards
   * @throws IOException if reading fails
   */
  public static List<Board> createBoardFromJSON(String filePath) throws IOException {
    FileHandler<Board> boardFileHandler = new GsonBoardFileHandler();
    return boardFileHandler.readFromFile(filePath);
  }
}
