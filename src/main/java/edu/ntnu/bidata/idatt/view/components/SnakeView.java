package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.controller.GameController;
import edu.ntnu.bidata.idatt.model.entity.Board;
import java.util.logging.Logger;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Renders the snakes defined in the Board model.
 */
public final class SnakeView {

  private static final Logger logger = Logger.getLogger(SnakeView.class.getName());
  private static final int COLUMNS = 10;

  private SnakeView() {
  }

  /**
   * Draws the snakes defined in the board model.
   *
   * @param board          the board
   * @param boardGrid      the grid
   * @param overlayPane    the overlay
   * @param gameController the game controller
   */
  public static void drawSnakes(Board board, GridPane boardGrid, Pane overlayPane,
                                GameController gameController) {
    SnakeRenderer.drawSnakes(board, boardGrid, overlayPane, gameController);
  }

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