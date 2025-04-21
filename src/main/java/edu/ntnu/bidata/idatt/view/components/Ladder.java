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
  private final List<Line> ladders = new ArrayList<>();

  public Ladder(Tile startTile, Tile endTile, Board board, GridPane boardGrid) {
    // Converts tile id to grid positions
    drawLadder(boardGrid, startTile, endTile, board);
  }

  public void drawLadder(GridPane boardGrid, Tile startTile, Tile endTile, Board board) {
    int[] startPos = LadderView.tileToGridPosition(startTile, board);
    int[] endPos = LadderView.tileToGridPosition(endTile, board);
    logger.log(Level.INFO, "Start row: " + startPos[0] + " Start column: " + startPos[1]);
    logger.log(Level.INFO, "End row: " + endPos[0] + " End column: " + endPos[1]);

    Node startNode = BoardView.getTileNodeAt(boardGrid, startPos[0], startPos[1]);
    Node endNode = BoardView.getTileNodeAt(boardGrid, endPos[0], endPos[1]);
    logger.log(Level.INFO, "Start tile: " + startNode + " End tile: " + endNode);

    Bounds startBounds = startNode.localToParent(startNode.getBoundsInLocal());
    Bounds endBounds = endNode.localToParent(endNode.getBoundsInLocal());
    logger.log(Level.INFO, "Start bound: " + startBounds + " End bound: " + endBounds);

    double visualCorrection = -10; // Because the dropshadow on TileView
    // Start and end coords, center line
    double startX = startBounds.getMinX() + startBounds.getWidth() * 0.5 + visualCorrection;
    double startY = startBounds.getMinY() + startBounds.getHeight() * 0.5;
    logger.log(Level.INFO, "startX: " + startX + " startY: " + startY);
    double endX = endBounds.getMinX() + endBounds.getWidth() * 0.5 + visualCorrection;
    double endY = endBounds.getMinY() + endBounds.getHeight() * 0.5;
    logger.log(Level.INFO, "endX: " + endX + " endY: " + endY);

    // Vector points from start to end
    double dx = endX - startX;
    double dy = endY - startY;
    logger.log(Level.INFO, "(dx,dy): (" + (int) dx + "," + (int) dy + ")");

    // Length of vectors above using pytagoras
    double lengthPytagoras = Math.sqrt(dx * dx + dy * dy);
    logger.log(Level.INFO, "length of (dx,dy): " + lengthPytagoras);

    int rungCount = (int) lengthPytagoras / 15;

    // Make a unit vector perpendicular to the ladders direction (pointing sideways)
    double unitPerpendicularX = -dy / lengthPytagoras;
    double unitPerpendicularY = dx / lengthPytagoras;

    int row = startPos[0];
    int totalRows = board.getTiles().size() / 10; // 10 rows foreløpig
    int rowFromBottom = totalRows - 1 - row;
    if (rowFromBottom % 2 == 1) {
      unitPerpendicularX = -unitPerpendicularX;
      unitPerpendicularY = -unitPerpendicularY;
    }

    logger.log(Level.INFO,
        "unitPerpendicularX: " + unitPerpendicularX + " unitPerpendicularY: " + unitPerpendicularY);
    //--------------------------------------------
    // Draw parallell lines
    double offset = 20;

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
        startRightX, startRightY, endRightX, endRightY, rungCount);
  }

  private void drawSideLine(double startX, double startY, double endX, double endY, Color color) {
    Line line = new Line(startX, startY, endX, endY);
    line.setStroke(color);
    line.setStrokeWidth(4);
    ladders.add(line);
  }

  private void drawRungs(double upperStartX, double upperStartY, double upperEndX, double upperEndY,
                         double lowerStartX, double lowerStartY, double lowerEndX,
                         double lowerEndY, int rungCount) {

    logger.log(Level.INFO, "");

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
      ladders.add(rung);
    }
  }

  public List<Line> getLadders() {
    return ladders;
  }
}
