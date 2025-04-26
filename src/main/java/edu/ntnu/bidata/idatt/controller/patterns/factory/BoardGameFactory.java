package edu.ntnu.bidata.idatt.controller.patterns.factory;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.model.logic.action.LadderAction;
import edu.ntnu.bidata.idatt.model.logic.action.SnakeAction;
import edu.ntnu.bidata.idatt.utils.io.FileHandler;
import edu.ntnu.bidata.idatt.utils.io.GsonBoardFileHandler;
import edu.ntnu.bidata.idatt.view.components.LadderView;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public final class BoardGameFactory {
  private static final int COLUMNS = 10;

  private static final Logger logger = Logger.getLogger(BoardGameFactory.class.getName());

  private BoardGameFactory() {
  }

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

  public static void addRandomLadders(Board board, int count) {
    int maxTile = board.getTiles().size();
    int placed = 0;
    while (placed < count) {
      int startId = (int) (Math.random() * 88) + 1;
      int endId = startId + 1 + (int) (Math.random() * (88 - startId));

      if (LadderView.isValidLadder(startId, endId)) {
        Tile startTile = board.getTile(startId);
        if (startTile.getLandAction() == null) {
          startTile.setNextTile(board.getTile(endId));
          startTile.setLandAction(new LadderAction(endId,
              "Ladder from " + startId + " to " + endId));
          placed++;
          logger.log(Level.INFO, startTile.getLandAction().getDescription());
        }
      }
    }
  }

  /**
   * Manual ladder creation
   *
   * @param board
   * @param start
   * @param end
   */
  public static void createLadder(Board board, int start, int end) {
    if (!LadderView.isValidLadder(start, end)) {
      return;
    }
    Tile s = board.getTile(start), e = board.getTile(end);
    if (s != null && e != null) {
      s.setNextTile(e);
      s.setLandAction(new LadderAction(end,
          "Ladder from " + start + " to " + end));
    }
  }

  public static void createSnake(Board board, int start, int end) {
    if (start <= end) {
      return;
    }
    Tile s = board.getTile(start), e = board.getTile(end);
    if (s != null && e != null) {
      s.setNextTile(e);
      s.setLandAction(new SnakeAction(end,
          "Snake from " + start + " to " + end));
    }
  }


  public static Board createClassicBoard() {
    Board board = createBoardTiles("Classic Board", "90-tile board with 5 ladders", 90);
    addRandomLadders(board, 10);
    return board;
  }

  public static Board createSmallBoard() {
    return createBoardTiles("Small Board", "30-tile board", 30);
  }

  public static Board createBoardNoLaddersAndSnakes() {
    return createBoardTiles("Flat Board", "90-tile board without actions", 90);
  }

  public static List<Board> createBoardFromJSON(String filePath) throws IOException {
    FileHandler<Board> handler = new GsonBoardFileHandler();
    return handler.readFromFile(filePath);
  }
}
