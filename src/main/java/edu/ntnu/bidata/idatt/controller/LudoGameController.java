package edu.ntnu.bidata.idatt.controller;

import edu.ntnu.bidata.idatt.controller.rules.LudoRules;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.view.scenes.BoardGameScene;
import java.io.IOException;

public class LudoGameController extends GameController {
  private final LudoRules ludoRules;

  public LudoGameController(BoardGameScene scene,
                            Board board,
                            int dice) throws IOException {
    super(scene, board, dice, new LudoRules());
    this.ludoRules = (LudoRules) super.gameRules;
  }

  @Override
  protected void applyLandAction(Player player, Tile tile, Runnable done) {
    ludoRules.onLand(player, tile);
    done.run();
  }

  @Override
  public int[] tileToGridPosition(Tile tile, Board board) {
    return new int[] {0, tile.getTileId() - 1};
  }

  @Override
  protected void afterTurnLogic(Player current) {
    if (!ludoRules.isExtraTurn()) {
      advanceToNextPlayer();
    }
  }
}
