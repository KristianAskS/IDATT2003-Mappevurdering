package edu.ntnu.bidata.idatt.model.entity;

import edu.ntnu.bidata.idatt.controller.BoardGameController;
import edu.ntnu.bidata.idatt.view.components.BoardView;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 * Visual representation of a ladder connecting two {@link Tile} instances on a Snakes and Ladders board.
 */
public class Ladder {
  public static final double VISUAL_CORRECTION = -10;
  private static final Logger logger = Logger.getLogger(Ladder.class.getName());
  private static final double SIDE_OFFSET = 20;
  private static final double RUNG_SPACING = 15;

  private final List<Line> ladders = new ArrayList<>();

  public Ladder(Tile startTile, Tile endTile, Board board, GridPane boardGrid) {
    drawLadder(boardGrid, startTile, endTile, board);
  }

  public static double[] getTileCenter(Bounds bounds) {
    double x = bounds.getMinX() + bounds.getWidth() * 0.5 + VISUAL_CORRECTION;
    double y = bounds.getMinY() + bounds.getHeight() * 0.5;
    logger.info(() -> "Center (x,y): (" + x + "," + y + ")");
    return new double[] {x, y};
  }

  private void drawLadder(GridPane boardGrid, Tile startTile, Tile endTile, Board board) {
    // Get the position of tile relative to the whole scene
    int[] startPos = BoardGameController.tileToGridPosition(startTile, board);
    int[] endPos = BoardGameController.tileToGridPosition(endTile, board);
    logTilePositions(startPos, endPos);

    Node startNode = BoardView.getTileNodeAt(boardGrid, startPos[0], startPos[1]);
    Node endNode = BoardView.getTileNodeAt(boardGrid, endPos[0], endPos[1]);

    logNodes(startNode, endNode);

    Bounds startBounds = startNode.localToParent(startNode.getBoundsInLocal());
    Bounds endBounds = endNode.localToParent(endNode.getBoundsInLocal());

    logBounds(startBounds, endBounds);

    double[] startCenter = getTileCenter(startBounds);
    double[] endCenter = getTileCenter(endBounds);

    // Calculating the ladder pos
    double dx = endCenter[0] - startCenter[0];
    double dy = endCenter[1] - startCenter[1];
    double length = Math.hypot(dx, dy);

    logger.info(() -> "(dx,dy): (" + (int) dx + "," + (int) dy + ")");
    logger.info(() -> "length of (dx,dy): " + length);

    int rungCount = calculateRungCount(length);
    double[] perpUnit = computePerpendicularUnitVector(dx, dy);
    maybeFlipDirection(perpUnit, startPos[0], board);

    logPerpendicular(perpUnit);

    double[][] sideCoords = computeSideRailCoordinates(startCenter, endCenter, perpUnit);
    addSideRail(sideCoords[0], sideCoords[1]);
    addSideRail(sideCoords[2], sideCoords[3]);
    addRungs(sideCoords, rungCount);
  }

  private void logTilePositions(int[] start, int[] end) {
    logger.info(() -> "Start row: " + start[0] + " column: " + start[1]);
    logger.info(() -> "End row: " + end[0] + " column: " + end[1]);
  }

  private void logNodes(Node start, Node end) {
    logger.info(() -> "Start tile: " + start + " End tile: " + end);
  }

  private void logBounds(Bounds start, Bounds end) {
    logger.info(() -> "Start bound: " + start + " End bound: " + end);
  }

  private void logPerpendicular(double[] perp) {
    logger.info(() -> "unitPerpendicularX: " + perp[0] + " unitPerpendicularY: " + perp[1]);
  }

  private int calculateRungCount(double length) {
    return Math.max(1, (int) (length / RUNG_SPACING));
  }

  private double[] computePerpendicularUnitVector(double dx, double dy) {
    double length = Math.hypot(dx, dy);
    return new double[] {-dy / length, dx / length};
  }

  private void maybeFlipDirection(double[] perp, int startRow, Board board) {
    int totalRows = board.getTiles().size() / 10; // TODO: Replace hardcoded value
    int rowFromBottom = totalRows - 1 - startRow;
    if (rowFromBottom % 2 == 1) {
      perp[0] = -perp[0];
      perp[1] = -perp[1];
    }
  }

  private double[][] computeSideRailCoordinates(double[] start, double[] end, double[] perpUnit) {
    double offX = perpUnit[0] * SIDE_OFFSET;
    double offY = perpUnit[1] * SIDE_OFFSET;

    return new double[][] {
        {start[0] + offX, start[1] + offY},
        {end[0] + offX, end[1] + offY},
        {start[0] - offX, start[1] - offY},
        {end[0] - offX, end[1] - offY}
    };
  }

  private void addSideRail(double[] start, double[] end) {
    addLine(start[0], start[1], end[0], end[1], 4, Color.BROWN);
  }

  private void addRungs(double[][] coords, int count) {
    for (int i = 1; i < count; i++) {
      double t = i / (double) count;
      double upperX = lerp(coords[0][0], coords[1][0], t);
      double upperY = lerp(coords[0][1], coords[1][1], t);
      double lowerX = lerp(coords[2][0], coords[3][0], t);
      double lowerY = lerp(coords[2][1], coords[3][1], t);

      Line rung = new Line(upperX, upperY, lowerX, lowerY);
      rung.setStroke(Color.SANDYBROWN);
      rung.setStrokeWidth(5);
      rung.setEffect(createShadow());
      ladders.add(rung);
    }
  }

  private DropShadow createShadow() {
    DropShadow ds = new DropShadow();
    ds.setRadius(10.0);
    ds.setOffsetX(10.0);
    ds.setOffsetY(10.0);
    ds.setColor(Color.color(0, 0, 0.6, 0.9));
    return ds;
  }

  private void addLine(double x1, double y1, double x2, double y2, double width, Color color) {
    Line line = new Line(x1, y1, x2, y2);
    line.setStroke(color);
    line.setStrokeWidth(width);
    ladders.add(line);
  }

  private double lerp(double a, double b, double t) {
    return a + t * (b - a);
  }

  public List<Line> getLadders() {
    return List.copyOf(ladders);
  }
}
