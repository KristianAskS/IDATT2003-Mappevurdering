package edu.ntnu.bidata.idatt.controller;

import edu.ntnu.bidata.idatt.controller.rules.LaddersRules;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;


  /** {@inheritDoc} */
public final class LaddersController extends GameController {
  private final LaddersRules rules;

  public LaddersController(Board board, int dice) {
    super(board, dice, new LaddersRules());
    this.rules = (LaddersRules) gameRules;
  }

  /** {@inheritDoc} */
  @Override
  public int[] tileToGridPosition(Tile tile, Board board) {
    int cols = 10;
    int rows = (int) Math.ceil(board.getTiles().size() / (double) cols);
    int id = tile.getTileId() - 1;
    int r = id / cols;
    int c = (r % 2 == 0) ? id % cols : cols - 1 - id % cols;
    return new int[] {rows - 1 - r, c};
  }
}