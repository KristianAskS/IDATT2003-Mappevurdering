package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Ladder {
  private static final Logger logger = Logger.getLogger(Ladder.class.getName());
  private final List<Line> lines = new ArrayList<>();

  public Ladder(Tile startTile, Tile endTile, Board board, GridPane boardGrid) {
    // Converts tile id to grid positions
    int[] startPos = SnakesAndLaddersView.tileToGridPosition(startTile, board);
    int[] endPos = SnakesAndLaddersView.tileToGridPosition(endTile, board);
    logger.log(Level.INFO, "Start pos: " + startPos + " End pos: " + endPos);

    Node startNode = BoardView.getTileNodeAt(boardGrid, startPos[0], startPos[1]);
    Node endNode = BoardView.getTileNodeAt(boardGrid, endPos[0], endPos[1]);
    logger.log(Level.INFO, "Start node: " + startNode + " End node: " + endNode);

    Bounds startBounds = startNode.localToParent(startNode.getBoundsInLocal());
    Bounds endBounds = endNode.localToParent(endNode.getBoundsInLocal());
    logger.log(Level.INFO, "Start bound: " + startBounds + " End bound: " + endBounds);

    double offset = 20;

    // Start and end coords
    double startX = startBounds.getMinX() + startBounds.getWidth() * 0.5;
    double startY = startBounds.getMinY() + startBounds.getHeight() * 0.5;
    logger.log(Level.INFO, "startX: " + startX + " startY: " + startY);
    double endX = endBounds.getMinX() + endBounds.getWidth() * 0.5;
    double endY = endBounds.getMinY() + endBounds.getHeight() * 0.5;
    logger.log(Level.INFO, "endX: " + endX + " endY: " + endY);
    // Vector points from start to end
    double dx = endX - startX;
    double dy = endY - startY;
    logger.log(Level.INFO, "(dx,dy): (" + dx + "," + dy + ")");

    // Length of vectors above using pytagoras
    double lengthPytagoras = Math.sqrt(dx * dx + dy * dy);
    logger.log(Level.INFO, "length of (dx,dy): " + lengthPytagoras);
    // Make a unit vector perpendicular to the ladders direction (pointing sideways)
    double unitPerpendicularX = dy / lengthPytagoras;
    double unitPerpendicularY = -dx / lengthPytagoras;
    logger.log(Level.INFO,
        "unitPerpendicularX: " + unitPerpendicularX + " unitPerpendicularY: " + unitPerpendicularY);

    // Draw parallell lines
    double startLeftX = startX + unitPerpendicularX * offset;
    double startLeftY = startY + unitPerpendicularY * offset;
    logger.log(Level.INFO, "startLeftX: " + startLeftX + " startLeftY: " + startLeftY);
    double endLeftX = endX + unitPerpendicularX * offset;
    double endLeftY = endY + unitPerpendicularY * offset;
    logger.log(Level.INFO, "endLeftX: " + endLeftX + " endLeftY: " + endLeftY);

    double startRightX = startX - unitPerpendicularX * offset;
    double startRightY = startY - unitPerpendicularY * offset;
    logger.log(Level.INFO, "startRightX: " + startRightX + " startRightY: " + startRightY);
    double endRightX = endX - unitPerpendicularX * offset;
    double endRightY = endY - unitPerpendicularY * offset;
    logger.log(Level.INFO, "endRightX: " + endRightX + " endRightY: " + endRightY);

    drawSideLine(startLeftX, startLeftY, endLeftX, endLeftY, Color.BROWN);
    drawSideLine(startRightX, startRightY, endRightX, endRightY, Color.BROWN);

    drawRungs(startLeftX, startLeftY, endLeftX, endLeftY,
        startRightX, startRightY, endRightX, endRightY);
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
      rung.setStroke(Color.SANDYBROWN);
      rung.setStrokeWidth(5);
      DropShadow dropShadow = new DropShadow();
      dropShadow.setRadius(10.0);
      dropShadow.setOffsetX(10.0);
      dropShadow.setOffsetY(10.0);
      dropShadow.setColor(Color.color(0, 0, 0.6, 0.9));
      rung.setEffect(dropShadow);
      lines.add(rung);
    }
  }

  public List<Line> getLines() {
    return lines;
  }
}
