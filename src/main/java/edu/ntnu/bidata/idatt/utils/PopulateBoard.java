package edu.ntnu.bidata.idatt.utils;

import edu.ntnu.bidata.idatt.model.Board;
import edu.ntnu.bidata.idatt.model.Tile;
import edu.ntnu.bidata.idatt.service.BoardService;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PopulateBoard {
  private static final String filePathLaddersAndSnakes = "data/games/laddersAndSnakes.json";
  private static final Logger logger = Logger.getLogger(PopulateBoard.class.getName());

  public static void main(String[] args) {
    int numberOfTiles = 90;
    Board board = new Board();

    for (int i = 1; i < numberOfTiles + 1; i++) {
      Tile tile = new Tile(i);
      board.addTile(tile);
    }

    for (int i = 1; i < numberOfTiles; i++) {
      Tile currentTile = board.getTileId(i);
      if (currentTile != null) {
        currentTile.setNextTileId(i +1);
      }
    }

    Tile lastTile = board.getTileId(numberOfTiles +1);
    if (lastTile != null) {
      lastTile.setNextTileId(-1);
    }
    BoardService boardService = new BoardService();
    boardService.setBoard(board);

    try {
      boardService.writeBoardToFile(filePathLaddersAndSnakes);
      logger.log(Level.INFO, "Board skrevet til fil: " + filePathLaddersAndSnakes);
      logger.log(Level.INFO, "Board er nå lagret som JSON i " + filePathLaddersAndSnakes);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
