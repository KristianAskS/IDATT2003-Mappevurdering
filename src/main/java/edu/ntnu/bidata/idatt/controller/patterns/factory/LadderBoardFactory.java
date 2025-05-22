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
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>Factory that builds different game boards and populated them with actions</p>
 * <p>Extends {@link BoardFactory}</p>
 *
 * @author Tri Tac Le
 * @version 1.1
 * @since 1.0
 */
public class LadderBoardFactory extends BoardFactory {

  private static final Logger logger = Logger.getLogger(LadderBoardFactory.class.getName());

  /**
   * <p>Default constructor for the board factory.</p>
   */
  public LadderBoardFactory() {
  }

  /**
   * <p>Creates the default "CLASSIC" board with a fixed size and a set number of ladders.</p>
   *
   * @return a {@link Board} named "CLASSIC" with 90 tiles and 10 randomly placed ladders
   */
  @Override
  public Board createDefaultBoard() {
    Board board = createBoardTiles("CLASSIC", "90-tile board", 90);
    addRandomLadders(board, 10);
    return board;
  }

  /**
   * <p>Adds a specified number of ladders at random start and end tiles on the board.</p>
   * <p>Ensures no tile or position is already used by another action
   * and that the ladder positions are valid.</p>
   *
   * @param board the {@link Board} to modify
   * @param count the number of ladders to place
   */
  private void addRandomLadders(Board board, int count) {
    Set<Integer> reserved = board.getTiles().values().stream()
        .filter(t -> t.getLandAction() != null)
        .flatMap(t -> Stream.of(
            t.getTileId(),
            t.getLandAction() instanceof LadderAction la
                ? la.getDestinationTileId()
                : t.getLandAction() instanceof SnakeAction sa
                ? sa.getDestinationTileId()
                : null))
        .collect(Collectors.toSet());

    int placed = 0;
    while (placed < count) {
      int startId = (int) (Math.random() * 88) + 1;
      int endId = startId + 1 + (int) (Math.random() * (88 - startId));

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

  /**
   * <p>Adds a specified number of skip turn actions at random tiles.</p>
   * <p>Ensures no tile is already used by another action.</p>
   *
   * @param board the {@link Board} to modify
   * @param count the number of skip turn actions to place
   */
  private void addRandomSkipTurns(Board board, int count) {
    Set<Integer> reserved = retrieveTileWithAction(board);
    int placed = 0;
    while (placed < count) {
      int id = 2 + (int) (Math.random() * (board.getTiles().size() - 2));
      if (reserved.contains(id)) {
        continue;
      }
      Tile t = board.getTile(id);
      t.setLandAction(new SkipTurnAction(1, "Skip Turn"));
      reserved.add(id);
      placed++;
      logger.log(Level.INFO, "Placed SkipTurn on tile " + id);
    }
  }

  /**
   * <p>Adds a specified number of back to start actions at random tiles.</p>
   * <p>Ensures no tile is already used by another action.</p>
   *
   * @param board the {@link Board} to modify
   * @param count the number of back-to-start actions to place
   */
  private void addRandomBackToStart(Board board, int count) {
    Set<Integer> reserved = retrieveTileWithAction(board);
    int placed = 0;
    while (placed < count) {
      int id = 2 + (int) (Math.random() * (board.getTiles().size() - 2));
      if (reserved.contains(id)) {
        continue;
      }
      Tile t = board.getTile(id);
      t.setLandAction(new BackToStartAction("Back To Start"));
      reserved.add(id);
      placed++;
      logger.log(Level.INFO, "Placed BackToStart on tile " + id);
    }
  }

  /**
   * <p>Retrieve ids of tiles already used by any land action.</p>
   * <p>Also includes both start and end ids for ladders and snakes.</p>
   *
   * @param board the {@link Board} to check
   * @return a {@link Set} of tile ids that cannot be used for new actions
   */
  private Set<Integer> retrieveTileWithAction(Board board) {
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

  /**
   * <p>Adds a specified number of snakes at random head and tail positions.</p>
   *
   * @param board the {@link Board} to modify
   * @param count the number of snakes to place
   */
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

      logger.log(Level.INFO, headTile.getLandAction().description());
    }
  }

  /**
   * <p>Creates a ladder between two specified tiles if valid.</p>
   * <p>Does not create if tiles are null, already have an action,
   * or if ladder positions are invalid.</p>
   *
   * @param board the {@link Board} to modify
   * @param start the starting tile id for the ladder
   * @param end   the destination tile id for the ladder
   */
  @SuppressWarnings("unsued for no")
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

  /**
   * <p>Creates a snake between two specified tiles if valid.</p>
   * <p>Does not create if tiles are null, already have an action,
   * or if snake positions are invalid.</p>
   *
   * @param board the {@link Board} to modify
   * @param head  the head tile ID for the snake
   * @param tail  the tail tile ID for the snake
   */
  @SuppressWarnings("unsued for no")
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

  /**
   * <p>Creates a classic board with 90 tiles</p>
   *
   * @return a {@link Board} configured as a classic game board
   */
  public Board createClassicBoard() {
    Board board = createBoardTiles("CLASSIC",
        "90‑tile board with obstacles", 90);

    addRandomLadders(board, 6);
    addRandomSnakes(board, 4);
    addRandomSkipTurns(board, 3);
    addRandomBackToStart(board, 3);
    return board;
  }

  /**
   * <p>Creates a chaos board with a higher density of obstacles:</p>
   * <ul>
   *   <li>7 ladders</li>
   *   <li>6 snakes</li>
   *   <li>10 skip-turn actions</li>
   *   <li>10 back-to-start actions</li>
   * </ul>
   *
   * @return a {@link Board} configured as a chaos game board
   */
  public Board createChaosBoard() {
    Board board = createBoardTiles("CHAOS BOARD", "100‑tile board with all obstacles!", 100);
    addRandomLadders(board, 7);
    addRandomSnakes(board, 6);
    addRandomSkipTurns(board, 10);
    addRandomBackToStart(board, 10);
    return board;
  }
}
