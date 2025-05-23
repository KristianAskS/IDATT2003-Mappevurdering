package edu.ntnu.bidata.idatt.controller;

import edu.ntnu.bidata.idatt.controller.rules.LudoRules;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.view.scenes.BoardGameScene;
import java.io.IOException;


/**
 * Game controller for Ludo games.
 */
public class LudoGameController extends GameController {
  private final LudoRules rules;

  /**
   * Constructs a new LudoGameController.
   *
   * @param scene the scene to which the game UI will be added
   * @param board the board model
   * @param dice the number of dice to use
   * @throws IOException if an I/O error occurs
   */
  public LudoGameController(BoardGameScene scene, Board board, int dice) throws IOException {
    super(scene, board, dice, new LudoRules());
    this.rules = (LudoRules) gameRules;
  }

  /** {@inheritDoc} */
  @Override
  public int[] tileToGridPosition(Tile tile, Board board) {
    return new int[] {0, tile.getTileId() - 1};
  }

  /** {@inheritDoc} */
  @Override
  protected void applyLandAction(Player player, Tile tile, Runnable done) {
    rules.onLand(player, tile);
    done.run();
  }

  /** {@inheritDoc} */
  @Override
  protected boolean shouldFinish(Player player) {
    return player.getAmountOfSteps() >= 50;
  }

  /** {@inheritDoc} */
  @Override
  protected void afterTurnLogic(Player current) {
    if (!rules.isExtraTurn()) {
      advanceToNextPlayer();
    }
  }
}