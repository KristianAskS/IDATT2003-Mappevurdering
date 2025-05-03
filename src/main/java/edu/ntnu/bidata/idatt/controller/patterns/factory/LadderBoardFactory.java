package edu.ntnu.bidata.idatt.controller.patterns.factory;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.model.logic.action.LadderAction;
import edu.ntnu.bidata.idatt.model.logic.action.SnakeAction;
import edu.ntnu.bidata.idatt.utils.io.FileHandler;
import edu.ntnu.bidata.idatt.utils.io.GsonBoardFileHandler;
import edu.ntnu.bidata.idatt.view.components.LadderView;
import edu.ntnu.bidata.idatt.view.components.SnakeView;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LadderBoardFactory extends BoardFactory {

  private static final Logger logger = Logger.getLogger(LadderBoardFactory.class.getName());

  public LadderBoardFactory() {
  }

  @Override
  public Board createDefaultBoard() {
    Board board = createBoardTiles("Classic Board", "90-tile board", 90);
    addRandomLadders(board, 10);
    return board;
  }

  //TODO: make more ludo board variants
  private void addRandomLadders(Board board, int count) {
    int placed = 0;
    while (placed < count) {
      int startId = (int) (Math.random() * 88) + 1;
      int endId = startId + 1 + (int) (Math.random() * (88 - startId));

      if (LadderView.isValidLadder(startId, endId)) {
        Tile startTile = board.getTile(startId);
        if (startTile.getLandAction() == null) {
          startTile.setNextTile(board.getTile(endId));
          startTile.setLandAction(
              new LadderAction(endId, "Ladder from " + startId + " to " + endId));
          placed++;
          logger.log(Level.INFO, startTile.getLandAction().getDescription());
        }
      }
    }
  }

  private void addRandomSnakes(Board board, int count) {
    HashSet<Object> reserved = new HashSet<>();
    board.getTiles().values().forEach(t -> {
      if (t.getLandAction() instanceof LadderAction la) {
        reserved.add(t.getTileId());
        reserved.add(la.getDestinationTileId());
      } else if (t.getLandAction() instanceof SnakeAction sa) {
        reserved.add(t.getTileId());
        reserved.add(sa.getDestinationTileId());
      }
    });

    int placed = 0;
    while (placed < count) {

      int head = 2 + (int) (Math.random() * 88);
      int tail = 1 + (int) (Math.random() * (head - 1));

      if (reserved.contains(head) || reserved.contains(tail)) {
        continue;
      }

      if (!SnakeView.isValidSnake(head, tail)) {
        continue;
      }

      Tile headTile = board.getTile(head);
      Tile tailTile = board.getTile(tail);

      if (headTile == null || tailTile == null) {
        continue;
      }

      headTile.setNextTile(tailTile);
      headTile.setLandAction(
          new SnakeAction(tail, "Snake from " + head + " to " + tail));

      reserved.add(head);
      reserved.add(tail);
      placed++;

      logger.log(Level.INFO, headTile.getLandAction().getDescription());
    }
  }

  private void createLadder(Board board, int start, int end) {
    if (!LadderView.isValidLadder(start, end)) {
      return;
    }
    Tile s = board.getTile(start), e = board.getTile(end);
    if (s != null && e != null) {
      s.setNextTile(e);
      s.setLandAction(new LadderAction(end, "Ladder from " + start + " to " + end));
    }
  }

  private void createSnake(Board board, int start, int end) {
    if (start <= end) {
      return;
    }
    Tile s = board.getTile(start), e = board.getTile(end);
    if (s != null && e != null) {
      s.setNextTile(e);
      s.setLandAction(new SnakeAction(end, "Snake from " + start + " to " + end));
    }
  }

  public Board createClassicBoard() {
    Board board = createBoardTiles("Classic Board", "90‑tile board with 10 ladders", 90);
    addRandomLadders(board, 5);
    addRandomSnakes(board, 5);
    return board;
  }

  public Board createSmallBoard() {
    return createBoardTiles("Small Board", "30‑tile board", 30);
  }

  public Board createBoardNoLaddersAndSnakes() {
    return createBoardTiles("Flat Board", "90‑tile board without actions", 90);
  }

  public List<Board> createBoardFromJSON(String filePath) throws IOException {
    FileHandler<Board> handler = new GsonBoardFileHandler();
    return handler.readFromFile(filePath);
  }
}
