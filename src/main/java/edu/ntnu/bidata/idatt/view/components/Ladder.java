package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Ladder {

  private final List<Line> lines = new ArrayList<>();

  public Ladder(Tile startTile, Tile endTile, Board board, GridPane boardGrid) {
    int[] startPos = tileToGridPosition(startTile, board);
    int[] endPos = tileToGridPosition(endTile, board);

    Node startNode = BoardView.getTileNodeAt(boardGrid, startPos[0], startPos[1]);
    Node endNode = BoardView.getTileNodeAt(boardGrid, endPos[0], endPos[1]);

    Bounds startBounds = startNode.localToParent(startNode.getBoundsInLocal());
    Bounds endBounds = endNode.localToParent(endNode.getBoundsInLocal());


    double upperStartX = startBounds.getMinX() + startBounds.getWidth() * 0.50;
    double upperStartY = startBounds.getMinY() + startBounds.getHeight() * 0.50;
    double upperEndX = endBounds.getMinX() + endBounds.getWidth() * 0.50;
    double upperEndY = endBounds.getMinY() + endBounds.getHeight() * 0.50;
    System.out.println(
        "Upper: " + upperStartX + " " + upperStartY + " " + upperEndX + " " + upperEndY);

    double lowerStartX = startBounds.getMinX() + startBounds.getWidth() * 0.50;
    double lowerStartY = startBounds.getMinY() + startBounds.getHeight() * 0.15;
    double lowerEndX = endBounds.getMinX() + endBounds.getWidth() * 0.50;
    double lowerEndY = endBounds.getMinY() + endBounds.getHeight() * 0.15;
    System.out.println(
        "lower: " + lowerStartX + " " + lowerStartX + " " + lowerEndX + " " + lowerEndY);
    drawSideLine(upperStartX, upperStartY, upperEndX, upperEndY, Color.BROWN);
    drawSideLine(lowerStartX, lowerStartY, lowerEndX, lowerEndY, Color.RED);
    drawRungs(upperStartX, upperStartY, upperEndX, upperEndY,
        lowerStartX, lowerStartY, lowerEndX, lowerEndY);
  }

  private void drawSideLine(double startX, double startY, double endX, double endY, Color color) {
    Line line = new Line(startX, startY, endX, endY);
    line.setStroke(color);
    line.setStrokeWidth(4);
    lines.add(line);
  }

  private void drawRungs(double upperStartX, double upperStartY, double upperEndX, double upperEndY,
                         double lowerStartX, double lowerStartY, double lowerEndX,
                         double lowerEndY) {
    int rungCount = 7;
    for (int i = 1; i < rungCount; i++) {
      double t = i / (double) rungCount;

      double upperX = upperStartX + t * (upperEndX - upperStartX);
      double upperY = upperStartY + t * (upperEndY - upperStartY);

      double lowerX = lowerStartX + t * (lowerEndX - lowerStartX);
      double lowerY = lowerStartY + t * (lowerEndY - lowerStartY);

      Line rung = new Line(upperX, upperY, lowerX, lowerY);
      rung.setStroke(Color.BLUEVIOLET);
      rung.setStrokeWidth(5);
      lines.add(rung);
    }
  }

  public List<Line> getLines() {
    return lines;
  }

  private int[] tileToGridPosition(Tile tile, Board board) {
    int totalTiles = board.getTiles().size();
    int tileId = tile.getTileId();
    int columns = 10;
    int rows = (int) Math.ceil(totalTiles / (double) columns);

    int row = (tileId - 1) / columns;
    int col = (tileId - 1) % columns;
    if (row % 2 == 1) {
      col = columns - col - 1;
    }

    row = rows - 1 - row;
    return new int[] {row, col};
  }
}
