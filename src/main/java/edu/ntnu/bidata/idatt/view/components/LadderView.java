package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.controller.GameController;
import edu.ntnu.bidata.idatt.model.entity.Board;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Renders the ladders defined in the Board model
 */
public final class LadderView {

  private static final int COLUMNS = 10;

  private LadderView() {
  }

  /**
   * Draws the ladders defined in the board model.
   *
   * @param board          the board model
   * @param boardGridPane  the grid pane to draw on
   * @param overlayPane    the pane to draw overlays on
   * @param gameController the game controller
   */
  public static void drawLadders(Board board, GridPane boardGridPane, Pane overlayPane,
                                 GameController gameController) {
    LadderRenderer.drawLadders(board, boardGridPane, overlayPane, gameController);
  }

  /**
   * Checks if the given start and end tile ids form a valid ladder.
   *
   * @param startId the start tile id
   * @param endId   the end tile id
   * @return true if the ladder is valid, false otherwise
   */
  public static boolean isValidLadder(int startId, int endId) {
    if (endId <= startId) {
      return false;
    }
    int startRow = (startId - 1) / COLUMNS;
    int endRow = (endId - 1) / COLUMNS;
    return startRow != endRow;
  }
}