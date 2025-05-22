package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.controller.GameController;
import edu.ntnu.bidata.idatt.model.entity.Board;
import java.util.logging.Logger;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Renders the ladders defined in the Board model
 */
public final class LadderView {

  private static final Logger logger = Logger.getLogger(LadderView.class.getName());
  private static final int COLUMNS = 10;

  private LadderView() {
  }

  public static void drawLadders(Board board, GridPane boardGridPane, Pane overlayPane,
                                 GameController gameController) {
    LadderRenderer.drawLadders(board, boardGridPane, overlayPane, gameController);
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