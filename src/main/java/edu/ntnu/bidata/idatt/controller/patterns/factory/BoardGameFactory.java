package edu.ntnu.bidata.idatt.controller.patterns.factory;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.utils.io.FileHandler;
import edu.ntnu.bidata.idatt.utils.io.GsonBoardFileHandler;
import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

public class BoardGameFactory {

  public static Board createBoardTiles(String name, String description, int numbOfTiles) {
    Board board = new Board(name, description);

    IntStream.rangeClosed(1, numbOfTiles)
        .mapToObj(Tile::new)
        .forEach(board::addTile);

    IntStream.rangeClosed(1, numbOfTiles)
        .forEach(i -> {
          Tile currentTile = board.getTileId(i);
          Tile nextTile = board.getTileId(i + 1);
          if (currentTile != null && nextTile != null) {
            currentTile.setNextTile(nextTile);
          }
        });
    return board;
  }

  /**
   * Creates ladders and snakes
   * Overrides nextTile
   * If start > end -> snake
   * If end > start -> ladder
   */
  public static void snakesOrLadders(Board board, int start, int end) {
    Tile startTile = board.getTileId(start);
    Tile endTile = board.getTileId(end);
    if (startTile != null && endTile != null) {
      startTile.setNextTile(endTile);
    }
  }

  public static Board createClassicBoard() {
    int numbOfTiles = 90;
    String name = "Classic Board";
    String description = "Classic board with 90 tiles";

    Board board = createBoardTiles(name, description, numbOfTiles);
    snakesOrLadders(board, 7, 14);
    snakesOrLadders(board, 16, 27);
    snakesOrLadders(board, 31, 42);
    snakesOrLadders(board, 67, 74);
    return board;
  }

  public static Board createSmallBoard() {
    int numbOfTiles = 30;
    String name = "Small Board";
    String description = "Small board with 30 tiles";
    Board board = createBoardTiles(name, description, numbOfTiles);
    snakesOrLadders(board, 7, 14);
    snakesOrLadders(board, 16, 27);
    snakesOrLadders(board, 31, 42);
    snakesOrLadders(board, 67, 74);
    return board;
  }

  public static Board createBoardNoLaddersAndSnakes() {
    int numbOfTiles = 90;
    String name = "Classic Board with no actions";
    String description = "Classic board with 90 tiles and no ladders and snakes";
    return createBoardTiles(name, description, numbOfTiles);
  }

  public static List createBoardFromJSON(String filePath) throws IOException {
    FileHandler<Board> boardFileHandler = new GsonBoardFileHandler();
    return boardFileHandler.readFromFile(filePath);
  }
}
