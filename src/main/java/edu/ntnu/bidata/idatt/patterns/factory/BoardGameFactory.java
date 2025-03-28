package edu.ntnu.bidata.idatt.patterns.factory;

import edu.ntnu.bidata.idatt.model.Board;
import edu.ntnu.bidata.idatt.model.Tile;
import edu.ntnu.bidata.idatt.utils.io.BoardFileHandler;
import edu.ntnu.bidata.idatt.utils.io.GsonBoardFileHandler;
import java.io.IOException;

public class BoardGameFactory {

  public static Board createBoardTiles(int numbOfTiles) {
    Board board = new Board();

    for (int i = 1; i <= numbOfTiles; i++) {
      Tile tile = new Tile(i);
      board.addTile(tile);
    }

    for (int i = 1; i <= 10; i++) {
      Tile currentTile = board.getTileId(i);
      Tile nextTile = board.getTileId(i + 1);
      if (currentTile != null && nextTile != null) {
        currentTile.setNextTile(nextTile);
      }
    }
    return board;
  }

  /**
   * Ladders and snakes
   */
  public static void linkTiles(Board board, int start, int end) {
    Tile startTile = board.getTileId(start);
    Tile endTile = board.getTileId(end);
    if (startTile != null && endTile != null) {
      startTile.setNextTile(endTile);
    }
  }

  public static Board createClassicBoard() {
    int numbOfTiles = 90;
    Board board = createBoardTiles(numbOfTiles);
    linkTiles(board, 7, 14);
    linkTiles(board, 16, 27);
    linkTiles(board, 31, 42);
    linkTiles(board, 67, 74);
    return board;
  }

  public static Board createSmallBoard() {
    int numbOfTiles = 30;
    Board board = createBoardTiles(numbOfTiles);
    linkTiles(board, 7, 14);
    linkTiles(board, 16, 27);
    linkTiles(board, 31, 42);
    linkTiles(board, 67, 74);
    return board;
  }

  public static Board createBoardNoLaddersAndSnakes() {
    int numbOfTiles = 90;
    return createBoardTiles(numbOfTiles);
  }

  public static Board createBoardFromJSON(String filePath) throws IOException {
    BoardFileHandler boardFileHandler = new GsonBoardFileHandler();
    return boardFileHandler.readBoard(filePath);
  }
}
