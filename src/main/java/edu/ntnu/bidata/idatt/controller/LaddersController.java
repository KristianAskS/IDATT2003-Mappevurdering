// src/main/java/edu/ntnu/bidata/idatt/controller/SnakesAndLaddersController.java

package edu.ntnu.bidata.idatt.controller;

import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEvent;
import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEventType;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.view.scenes.BoardGameScene;
import java.io.IOException;

public final class LaddersController extends GameController {

  public LaddersController(BoardGameScene scene,
                           Board board,
                           int dice) throws IOException {
    super(scene, board, dice);
  }

  @Override
  public int[] tileToGridPosition(Tile t, Board b) {
    int cols = 10;
    int rows = (int) Math.ceil(b.getTiles().size() / (double) cols);
    int id = t.getTileId() - 1;
    int r = id / cols,
        c = r % 2 == 0 ? id % cols : cols - 1 - id % cols;
    return new int[] {rows - 1 - r, c};
  }

  @Override
  protected void applyPostLandAction(Player player, Tile landed, Runnable onDone) {
    if (landed.getLandAction() == null) {
      onDone.run();
      return;
    }

    int destinationTileId = landed.getLandAction().getDestinationTileId();
    landed.getLandAction().perform(player);

    animateLadderMovement(player, landed.getTileId(), destinationTileId, () -> {
      boardGameScene.onEvent(new BoardGameEvent(BoardGameEventType.PLAYER_LADDER_ACTION,
          player, landed, board.getTile(destinationTileId)));
      onDone.run();
    });
  }
}
