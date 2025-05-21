package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.controller.GameController;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Ladder;
import edu.ntnu.bidata.idatt.model.logic.action.LadderAction;
import edu.ntnu.bidata.idatt.utils.exceptions.GameUIException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Renders the ladders defined in the Board model
 */
public class LadderView {

  private static final Logger logger = Logger.getLogger(LadderView.class.getName());
  private static final int COLUMNS = 10;

  private LadderView() {
  }

  public static void drawLadders(Board board, GridPane boardGridPane, Pane overlayPane,
                                 GameController gameController) {
    overlayPane.getChildren().clear();

    board.getTiles().values().stream()
        .filter(tile -> tile.getLandAction() instanceof LadderAction)
        .forEach(startTile -> {

          LadderAction ladder = (LadderAction) startTile.getLandAction();
          int startId = startTile.getTileId();
          int endId = ladder.getDestinationTileId();

          if (!isValidLadder(startId, endId)) {
            return;
          }

          TileView endView = (TileView) boardGridPane.lookup("#tile" + endId);
          try {
            overlayPane.getChildren().addAll(
                new Ladder(startTile, board.getTile(endId), board, boardGridPane,
                    gameController).getLadders());
          } catch (IOException e) {
            throw new GameUIException("Failed to draw ladder from tile " + startId + " to tile " + endId, e);
          }

          TileView startView = (TileView) boardGridPane.lookup("#tile" + startId);
          if (startView != null) {
            startView.setStyle("-fx-background-color: #A5D6A7;");
            startView.addTileActionViewLbl("start", Color.RED);
          }
          if (endView != null) {
            endView.setStyle("-fx-background-color: #EF9A9A;");
            endView.addTileActionViewLbl("end", Color.RED);
          }

          logger.log(Level.FINE, "Drew ladder from " + startId + " to " + endId);
        });
  }

  public static boolean isValidLadder(int startId, int endId) {
    if (endId <= startId) {
      return false;
    }
    int startRow = (startId - 1) / COLUMNS;
    int endRow = (endId - 1) / COLUMNS;
    return startRow != endRow;
  }
}
