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

/**
 * Visual representation of a ladder connecting two {@link Tile} instances on a Snakes and Ladders
 * board.
 */
public class Ladder {

  private static final Logger logger = Logger.getLogger(Ladder.class.getName());

  private static final double VISUAL_CORRECTION = -10;
  private static final double SIDE_OFFSET = 20;
  private static final double RUNG_SPACING = 15;

  private final List<Line> ladders = new ArrayList<>();

  /**
   * Constructs and immediately draws a ladder
   *
   * @param startTile the lower tile where the ladder begins
   * @param endTile   the upper tile where the ladder ends
   * @param board     logical board model (used for orientation correction)
   * @param boardGrid JavaFX grid that hosts the tile nodes
   */
  public Ladder(Tile startTile, Tile endTile, Board board, GridPane boardGrid) {
    drawLadder(boardGrid, startTile, endTile, board);
  }

  /**
   * Draws ladder
   */
  public void drawLadder(GridPane boardGrid, Tile startTile, Tile endTile, Board board) {
    int[] startPos = LadderView.tileToGridPosition(startTile, board);
    int[] endPos = LadderView.tileToGridPosition(endTile, board);

    logger.log(Level.INFO, () -> "Start row: " + startPos[0] + " Start column: " + startPos[1]);
    logger.log(Level.INFO, () -> "End row: " + endPos[0] + " End column: " + endPos[1]);

    Node startNode = BoardView.getTileNodeAt(boardGrid, startPos[0], startPos[1]);
    Node endNode = BoardView.getTileNodeAt(boardGrid, endPos[0], endPos[1]);

    logger.log(Level.INFO, () -> "Start tile: " + startNode + " End tile: " + endNode);

    Bounds startBounds = startNode.localToParent(startNode.getBoundsInLocal());
    Bounds endBounds = endNode.localToParent(endNode.getBoundsInLocal());

    logger.log(Level.INFO, () -> "Start bound: " + startBounds + " End bound: " + endBounds);

    double[] startCenter = getTileCenter(startBounds);
    double[] endCenter = getTileCenter(endBounds);

    double dx = endCenter[0] - startCenter[0];
    double dy = endCenter[1] - startCenter[1];

    logger.log(Level.INFO, () -> "(dx,dy): (" + (int) dx + "," + (int) dy + ")");

    double length = Math.hypot(dx, dy);
    logger.log(Level.INFO, () -> "length of (dx,dy): " + length);

    int rungCount = calculateRungCount(length);

    double[] perpUnit = computePerpendicularUnitVector(dx, dy);
    maybeFlipDirection(perpUnit, startPos[0], board);

    logger.log(Level.INFO,
        () -> "unitPerpendicularX: " + perpUnit[0] + " unitPerpendicularY: " + perpUnit[1]);

    double[][] sideCoords = computeSideRailCoordinates(startCenter, endCenter, perpUnit);
    addSideRail(sideCoords[0], sideCoords[1]);
    addSideRail(sideCoords[2], sideCoords[3]);
    addRungs(sideCoords, rungCount);
  }

  private double[] getTileCenter(Bounds bounds) {
    double x = bounds.getMinX() + bounds.getWidth() * 0.5 + VISUAL_CORRECTION;
    double y = bounds.getMinY() + bounds.getHeight() * 0.5;
    logger.log(Level.INFO, () -> "Center (x,y): (" + x + "," + y + ")");
    return new double[] {x, y};
  }

  private int calculateRungCount(double ladderLength) {
    return Math.max(1, (int) (ladderLength / RUNG_SPACING));
  }

  private double[] computePerpendicularUnitVector(double dx, double dy) {
    double length = Math.hypot(dx, dy);
    return new double[] {-dy / length, dx / length};
  }

  /**
   * Flips if the ladder is on an alternating board row.
   */
  private void maybeFlipDirection(double[] perpUnit, int startRow, Board board) {
    int totalRows = board.getTiles().size() / 10; // 10 rows foreløpig TODO: bytt ut med board API
    int rowFromBottom = totalRows - 1 - startRow;
    if (rowFromBottom % 2 == 1) {
      perpUnit[0] = -perpUnit[0];
      perpUnit[1] = -perpUnit[1];
    }
  }

  private double[][] computeSideRailCoordinates(double[] start, double[] end, double[] perpUnit) {
    double offX = perpUnit[0] * SIDE_OFFSET;
    double offY = perpUnit[1] * SIDE_OFFSET;

    double startLeftX = start[0] + offX;
    double startLeftY = start[1] + offY;
    double endLeftX = end[0] + offX;
    double endLeftY = end[1] + offY;

    double startRightX = start[0] - offX;
    double startRightY = start[1] - offY;
    double endRightX = end[0] - offX;
    double endRightY = end[1] - offY;

    logger.log(Level.INFO, () -> String.format("Left rail: (%.1f,%.1f)-(%.1f,%.1f)",
        startLeftX, startLeftY, endLeftX, endLeftY));
    logger.log(Level.INFO, () -> String.format("Right rail: (%.1f,%.1f)-(%.1f,%.1f)",
        startRightX, startRightY, endRightX, endRightY));

    return new double[][] {
        {startLeftX, startLeftY},
        {endLeftX, endLeftY},
        {startRightX, startRightY},
        {endRightX, endRightY}
    };
  }

  private void addSideRail(double[] start, double[] end) {
    addLine(start[0], start[1], end[0], end[1], 4, Color.BROWN);
  }

  private void addRungs(double[][] sideCoords, int rungCount) {
    double[] upperStart = sideCoords[0];
    double[] upperEnd = sideCoords[1];
    double[] lowerStart = sideCoords[2];
    double[] lowerEnd = sideCoords[3];

    for (int i = 1; i < rungCount; i++) {
      double t = i / (double) rungCount;
      double upperX = lerp(upperStart[0], upperEnd[0], t);
      double upperY = lerp(upperStart[1], upperEnd[1], t);
      double lowerX = lerp(lowerStart[0], lowerEnd[0], t);
      double lowerY = lerp(lowerStart[1], lowerEnd[1], t);

      Line rung = new Line(upperX, upperY, lowerX, lowerY);
      rung.setStroke(Color.SANDYBROWN);
      rung.setStrokeWidth(5);

      DropShadow ds = new DropShadow();
      ds.setRadius(10.0);
      ds.setOffsetX(10.0);
      ds.setOffsetY(10.0);
      ds.setColor(Color.color(0, 0, 0.6, 0.9));
      rung.setEffect(ds);

      ladders.add(rung);
    }
  }

  private void addLine(double startX, double startY, double endX, double endY, double width,
                       Color color) {
    Line line = new Line(startX, startY, endX, endY);
    line.setStroke(color);
    line.setStrokeWidth(width);
    ladders.add(line);
  }

  private double lerp(double a, double b, double t) {
    return a + t * (b - a);
  }

  /**
   * Returns an immutable view of all {@link Line} instances that form this ladder.
   */
  public List<Line> getLadders() {
    return List.copyOf(ladders);
  }
}
