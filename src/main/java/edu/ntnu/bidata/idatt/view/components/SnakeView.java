package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.controller.GameController;
import edu.ntnu.bidata.idatt.model.entity.Board;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import java.util.logging.Logger;

public final class SnakeView {

  private static final Logger logger = Logger.getLogger(SnakeView.class.getName());
  private static final int COLUMNS = 10;

  private SnakeView() {
  }

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