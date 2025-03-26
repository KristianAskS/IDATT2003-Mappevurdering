package edu.ntnu.bidata.idatt.utils;

import edu.ntnu.bidata.idatt.model.Board;
import edu.ntnu.bidata.idatt.model.Tile;
import java.util.logging.Logger;

/**
 * Populate board.
 * @author Trile
 */
public class PopulateBoard {
  private static final Logger logger = Logger.getLogger(PopulateBoard.class.getName());

  /**
   * Creates a Board with tile IDs from 1 to numberOfTiles,
   * and sets nextTileId for each tile (except the last one).
   *
   * @param numberOfTiles how many tiles to create
   * @return a populated Board
   */
  public static Board createBoard(int numberOfTiles) {
    Board board = new Board();
    for (int i = 1; i <= numberOfTiles; i++) {
      Tile tile = new Tile(i);
      board.addTile(tile);
    }
    for (int i = 1; i < numberOfTiles; i++) {
      Tile currentTile = board.getTileId(i);
      if (currentTile != null) {
        currentTile.setNextTileId(i + 1);
      }
    }
    Tile lastTile = board.getTileId(numberOfTiles);
    if (lastTile != null) {
      lastTile.setNextTileId(-1);
    }
    logger.info("Board created in memory with " + numberOfTiles + " tiles.");
    return board;
  }
}
