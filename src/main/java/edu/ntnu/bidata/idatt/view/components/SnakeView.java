package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.controller.GameController;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Snake;
import edu.ntnu.bidata.idatt.model.logic.action.SnakeAction;
import java.util.logging.Logger;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public final class SnakeView {

  private static final Logger log = Logger.getLogger(SnakeView.class.getName());
  private static final int COLUMNS = 10;

  private SnakeView() {
  }

  public static void drawSnakes(Board board, GridPane boardGrid, Pane overlay,
                                GameController gc) {

    board.getTiles().values().stream()
        .filter(t -> t.getLandAction() instanceof SnakeAction)
        .forEach(start -> {
          SnakeAction sa = (SnakeAction) start.getLandAction();
          int startId = start.getTileId();
          int endId = sa.getDestinationTileId();
          if (!isValidSnake(startId, endId)) {
            return;
          }

          try {
            overlay.getChildren().addAll(
                new Snake(start, board.getTile(endId), board, boardGrid, gc).getSnakes()
            );
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }

          TileView startView = (TileView) boardGrid.lookup("#tile" + startId);
          TileView endView = (TileView) boardGrid.lookup("#tile" + endId);
          if (startView != null) {
            startView.setStyle("-fx-background-color:#A5D6A7;");
            startView.addTileActionViewLbl("start", Color.RED);
          }
          if (endView != null) {
            endView.setStyle("-fx-background-color:#EF9A9A;");
            endView.addTileActionViewLbl("end", Color.RED);
          }

        });
  }

  /**
   * head must be above tail and not on the same horizontal row.
   */
  public static boolean isValidSnake(int headId, int tailId) {
    if (headId <= tailId) {
      return false;
    }
    int headRow = (headId - 1) / COLUMNS;
    int tailRow = (tailId - 1) / COLUMNS;
    return headRow != tailRow;
  }
}