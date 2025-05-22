package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.controller.GameController;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Snake;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.model.logic.action.SnakeAction;
import edu.ntnu.bidata.idatt.utils.exceptions.GameUIException;
import java.util.logging.Logger;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public final class SnakeView {

  private static final Logger logger = Logger.getLogger(SnakeView.class.getName());
  private static final int COLUMNS = 10;

  private SnakeView() {
  }

  public static void drawSnakes(Board board, GridPane boardGrid, Pane overlay,
                                GameController gameController) {

    board.getTiles().values().stream()
        .filter(tile -> tile.getLandAction() instanceof SnakeAction)
        .forEach(start -> {
          SnakeAction snakeAction = (SnakeAction) start.getLandAction();
          int startId = start.getTileId();
          int endId = snakeAction.getDestinationTileId();
          if (!isValidSnake(startId, endId)) {
            return;
          }

          try {
            Tile tailTile = board.getTile(endId);
            if (tailTile == null) {
              logger.warning("Skipping snake with invalid tail: " + endId);
              return;
            }
            overlay.getChildren().addAll(
                new Snake(start, tailTile, board, boardGrid, gameController).getSnakes()
            );
            overlay.getChildren().addAll(
                new Snake(start, board.getTile(endId), board, boardGrid, gameController).getSnakes()
            );
          } catch (Exception exception) {
            throw new GameUIException(
                "Failed to draw snake from tile " + startId + " to tile " + endId, exception);
          }

          TileView startView = (TileView) boardGrid.lookup("#tile" + startId);
          TileView endView = (TileView) boardGrid.lookup("#tile" + endId);
          if (startView != null) {
            startView.setStyle("-fx-background-color:red;");
          }
          if (endView != null) {
            endView.setStyle("-fx-background-color:#FF474D;");
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
    if (tailId < 1) {
      return false;
    }
    int headRow = (headId - 1) / COLUMNS;
    int tailRow = (tailId - 1) / COLUMNS;
    return headRow != tailRow;
  }
}