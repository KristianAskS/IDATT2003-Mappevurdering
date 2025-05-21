package edu.ntnu.bidata.idatt.controller.patterns.factory;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.model.logic.action.BackToStartAction;
import edu.ntnu.bidata.idatt.model.logic.action.LadderAction;
import edu.ntnu.bidata.idatt.model.logic.action.SkipTurnAction;
import edu.ntnu.bidata.idatt.model.logic.action.SnakeAction;
import edu.ntnu.bidata.idatt.view.components.LadderView;
import edu.ntnu.bidata.idatt.view.components.SnakeView;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

  private void addRandomLadders(Board board, int count) {
    Set<Integer> reserved = board.getTiles().values().stream()
        .filter(t -> t.getLandAction() != null)
        .flatMap(t -> Stream.of(t.getTileId(),
            ((t.getLandAction() instanceof LadderAction la)
                ? la.getDestinationTileId()
                : (t.getLandAction() instanceof SnakeAction sa)
                ? sa.getDestinationTileId()
                : null)))
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());

    int placed = 0;
    while (placed < count) {
      int startId = (int) (Math.random() * 88) + 1;
      int endId   = startId + 1 + (int) (Math.random() * (88 - startId));

      if (!LadderView.isValidLadder(startId, endId)
          || reserved.contains(startId)
          || reserved.contains(endId)) {
        continue;
      }

      Tile startTile = board.getTile(startId);
      startTile.setNextTile(board.getTile(endId));
      startTile.setLandAction(
          new LadderAction(endId, "Ladder from " + startId + " to " + endId));

      reserved.add(startId);
      reserved.add(endId);
      placed++;
    }
  }
  private void addRandomSkipTurns(Board board, int count) {
    Set<Integer> reserved = collectReserved(board);
    int placed = 0;
    while (placed < count) {
      int id = 2 + (int)(Math.random() * (board.getTiles().size() - 1));  // avoid tile 1
      if (reserved.contains(id)) continue;
      Tile t = board.getTile(id);
      t.setLandAction(new SkipTurnAction(1, "Skip Turn"));
      reserved.add(id);
      placed++;
      logger.log(Level.INFO, "Placed SkipTurn on tile " + id);
    }
  }

  private void addRandomBackToStart(Board board, int count) {
    Set<Integer> reserved = collectReserved(board);
    int placed = 0;
    while (placed < count) {
      int id = 2 + (int)(Math.random() * (board.getTiles().size() - 1));
      if (reserved.contains(id)) continue;
      Tile t = board.getTile(id);
      t.setLandAction(new BackToStartAction("Back To Start"));
      reserved.add(id);
      placed++;
      logger.log(Level.INFO, "Placed BackToStart on tile " + id);
    }
  }

  private Set<Integer> collectReserved(Board board) {
    return board.getTiles().values().stream()
        .filter(tile -> tile.getLandAction() != null)
        .flatMap(tile -> {
          Set<Integer> s = new HashSet<>();
          s.add(tile.getTileId());
          if (tile.getLandAction() instanceof LadderAction la) {
            s.add(la.getDestinationTileId());
          } else if (tile.getLandAction() instanceof SnakeAction sa) {
            s.add(sa.getDestinationTileId());
          }
          return s.stream();
        })
        .collect(Collectors.toSet());
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

      if (SnakeView.isValidSnake(head, tail)) {
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
    Tile s = board.getTile(start), e = board.getTile(end);
    if (s == null || e == null
        || s.getLandAction() != null
        || !LadderView.isValidLadder(start, end)) {
      return;
    }
    s.setNextTile(e);
    s.setLandAction(new LadderAction(end, "Ladder from " + start + " to " + end));
  }


  private void createSnake(Board board, int head, int tail) {
    Tile h = board.getTile(head), t = board.getTile(tail);
    if (h == null || t == null
        || h.getLandAction() != null
        || SnakeView.isValidSnake(head, tail)) {
      return;
    }
    h.setNextTile(t);
    h.setLandAction(new SnakeAction(tail, "Snake from " + head + " to " + tail));
  }


  public Board createClassicBoard() {
    Board board = createBoardTiles("Classic Board",
        "90‑tile board with ladders & snakes", 90);

    addRandomLadders(board, 4);
    addRandomSnakes(board, 4);

    addRandomSkipTurns(board, 10);
    addRandomBackToStart(board, 10);
    return board;
  }


  public Board createSmallBoard() {
    Board board = createBoardTiles("Small Board", "30‑tile board", 30);
    createSnake(board, 9, 2);
    createSnake(board, 26, 14);
    createLadder(board, 5, 19);
    createLadder(board, 12, 28);
    return board;
  }
}
