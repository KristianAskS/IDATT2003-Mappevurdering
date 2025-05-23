package edu.ntnu.bidata.idatt.controller;

import edu.ntnu.bidata.idatt.controller.rules.LudoRules;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Tile;

/**
 * {@link GameController} implementation for Ludo.
 */
public class LudoGameController extends GameController {
  private final LudoRules rules;

  /**
   * Constructs a new Ludo controller.
   *
   * @param board        the board model containing 52 tiles
   * @param numberOfDice how many dice to roll each turn
   */
  public LudoGameController(Board board, int numberOfDice) {
    super(board, numberOfDice, new LudoRules());
    this.rules = (LudoRules) gameRules;
  }

  /**
   * {@inheritDoc}
   * Each tile’s column index is simply its ID minus one (in ludo).
   *
   * @param tile  the tile to locate
   * @param board the board containing the tile
   * @return an int array of {row, column}
   */
  @Override
  public int[] tileToGridPosition(Tile tile, Board board) {
    return new int[] {0, tile.getTileId() - 1};
  }

  /**
   * {@inheritDoc}
   * Delegates to {@link LudoRules#onLand(Player, Tile)} for captures and home entry logic,
   *
   * @param player the player who has landed
   * @param tile   the tile they landed on
   * @param done   callback to invoke once the rule has been applied
   */
  @Override
  protected void applyLandAction(Player player, Tile tile, Runnable done) {
    rules.onLand(player, tile);
    done.run();
  }

  /**
   * {@inheritDoc}
   * Check if the player has finished
   *
   * @param player the player to check
   * @return true if they’ve reached or passed their home stretch
   */
  @Override
  protected boolean shouldFinish(Player player) {
    return player.getAmountOfSteps() >= 50;
  }

  /**
   * {@inheritDoc}
   * In Ludo, rolling a six grants an extra turn
   *
   * @param current the player whose turn just ended
   */
  @Override
  protected void afterTurnLogic(Player current) {
    if (!rules.isExtraTurn()) {
      advanceToNextPlayer();
    }
  }
}
