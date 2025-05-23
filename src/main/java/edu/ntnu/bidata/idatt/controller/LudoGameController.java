package edu.ntnu.bidata.idatt.controller;

import edu.ntnu.bidata.idatt.controller.rules.LudoRules;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Tile;

public class LudoGameController extends GameController {
  private final LudoRules rules;

  public LudoGameController(Board board, int numberOfDice) {
    super(board, numberOfDice, new LudoRules());
    this.rules = (LudoRules) gameRules;
  }


  @Override
  public int[] tileToGridPosition(Tile tile, Board board) {
    return new int[] {0, tile.getTileId() - 1};
  }

  @Override
  protected void applyLandAction(Player player, Tile tile, Runnable done) {
    rules.onLand(player, tile);
    done.run();
  }

  @Override
  protected boolean shouldFinish(Player player) {
    return player.getAmountOfSteps() >= 50;
  }

  @Override
  protected void afterTurnLogic(Player current) {
    if (!rules.isExtraTurn()) {
      advanceToNextPlayer();
    }
  }
}