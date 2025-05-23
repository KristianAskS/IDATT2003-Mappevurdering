package edu.ntnu.bidata.idatt.controller;

import edu.ntnu.bidata.idatt.controller.rules.LaddersRules;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import javafx.scene.layout.GridPane;

/**
 * {@link GameController} for the Snakes & Ladders game.
 */
public final class LaddersController extends GameController {
  private final LaddersRules rules;

  /**
   * Creates a new LaddersController.
   *
   * @param board the {@link Board} model containing tiles, ladders, and snakes
   * @param dice  the number of dice to be used in each turn
   */
  public LaddersController(Board board, int dice) {
    super(board, dice, new LaddersRules());
    this.rules = (LaddersRules) gameRules;
  }

  /**
   * {@inheritDoc}
   * <p>
   * Translates a tile's position on the board to its corresponding {@link GridPane} coordinates
   *
   * @param tile  the tile
   * @param board the board containing that tile
   * @return an int[2] of {rowIndex, columnIndex} in the {@link GridPane}
   */
  @Override
  public int[] tileToGridPosition(Tile tile, Board board) {
    int cols = 10;
    int rows = (int) Math.ceil(board.getTiles().size() / (double) cols);
    int id = tile.getTileId() - 1;
    int r = id / cols;
    int c = (r % 2 == 0)
        ? id % cols
        : cols - 1 - (id % cols);
    return new int[] {rows - 1 - r, c};
  }
}
