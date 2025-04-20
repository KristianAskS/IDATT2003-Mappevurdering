package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;

public class Snakes {

  public Snakes(Tile startTile, Tile endTile, Board board, GridPane boardGrid) {
    int[] startPos = SnakesAndLaddersView.tileToGridPosition(startTile, board);
    int[] endPos = SnakesAndLaddersView.tileToGridPosition(endTile, board);

    Node startNode = BoardView.getTileNodeAt(boardGrid, startPos[0], startPos[1]);
    Node endNode = BoardView.getTileNodeAt(boardGrid, endPos[0], endPos[1]);

    Bounds startBounds = startNode.localToParent(startNode.getBoundsInLocal());
    Bounds endBounds = endNode.localToParent(endNode.getBoundsInLocal());

    double upperStartX = startBounds.getMinX() + startBounds.getWidth() * 0.50;
    double upperStartY = startBounds.getMinY() + startBounds.getHeight() * 0.50;
    double upperEndX = endBounds.getMinX() + endBounds.getWidth() * 0.50;
    double upperEndY = endBounds.getMinY() + endBounds.getHeight() * 0.50;

    drawSideLine(upperStartX, upperStartY, upperEndX, upperEndY, Color.BROWN);
  }

  private void drawSideLine(double startX, double startY, double endX, double endY, Color color) {
    CubicCurve cubicCurve = new CubicCurve();
    cubicCurve.setStroke(color);
    cubicCurve.setStrokeWidth(10);
  }
}
