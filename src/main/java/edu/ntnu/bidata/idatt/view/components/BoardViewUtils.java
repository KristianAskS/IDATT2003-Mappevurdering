package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.controller.GameController;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public class BoardViewUtils {

  public static final double VISUAL_CORRECTION = -10;

  private BoardViewUtils() {
  }

  public static double[] getTileCenter(GridPane grid, GameController gameController, Board board,
      Tile tile) {
    int[] rowCol = gameController.tileToGridPosition(tile, board);
    Node tileNode = BaseBoardView.getTileNodeAt(grid, rowCol[0], rowCol[1]);
    if (tileNode == null) {
      return new double[]{0, 0};
    }
    Bounds bounds = tileNode.getBoundsInParent();
    double x = bounds.getMinX() + bounds.getWidth() * 0.5 + VISUAL_CORRECTION;
    double y = bounds.getMinY() + bounds.getHeight() * 0.5;
    return new double[]{x, y};
  }
}