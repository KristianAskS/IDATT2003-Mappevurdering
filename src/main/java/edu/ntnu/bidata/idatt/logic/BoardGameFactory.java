package edu.ntnu.bidata.idatt.logic;

import edu.ntnu.bidata.idatt.model.Board;
import edu.ntnu.bidata.idatt.model.Tile;
import edu.ntnu.bidata.idatt.utils.BoardFileHandler;
import edu.ntnu.bidata.idatt.utils.GsonBoardFileHandler;
import edu.ntnu.bidata.idatt.utils.PopulateBoard;
import java.io.IOException;

public class BoardGameFactory {
  public static Board createClassicBoard() {
    Board board = new Board();
    int numbOfTiles = 90;

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

  public static Board createBoardFromJSON(String filePath) throws IOException {
    BoardFileHandler boardFileHandler = new GsonBoardFileHandler();
    return boardFileHandler.readBoard(filePath);
  }
}
