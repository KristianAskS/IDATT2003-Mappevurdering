package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.controller.GameController;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.model.logic.action.LadderAction;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;


public class LadderRenderer {

  private static final Logger logger = Logger.getLogger(LadderRenderer.class.getName());
  private static final double SIDE_OFFSET = 20;
  private static final double RUNG_SPACING = 15;
  private static final int LADDERS_BOARD_COLUMNS = 10;

  private LadderRenderer() {
  }

  public static void drawLadders(Board board, GridPane boardGrid, Pane overlayPane,
                                 GameController gameController) {

    board.getTiles().values().stream()
        .filter(
            tile -> tile.getLandAction() instanceof LadderAction)
        .forEach(startTile -> {
          LadderAction ladderAction =
              (LadderAction) startTile.getLandAction();
          int startId = startTile.getTileId();
          int endId = ladderAction.getDestinationTileId();

          if (!isValidLadder(startId, endId)) {
            logger.warning("Invalid ladder defined from " + startId + " to " + endId);
            return;
          }

          Tile endTile = board.getTile(endId);
          if (endTile == null) {
            logger.severe(
                "Ladder destination tile " + endId + " not found for start tile " + startId);
            return;
          }

          double[] startCenter = BoardViewUtils.getTileCenter(boardGrid, gameController, board,
              startTile);
          double[] endCenter = BoardViewUtils.getTileCenter(boardGrid, gameController, board,
              endTile);

          List<Node> ladderComponents = createLadderVisual(startCenter, endCenter, startTile,
              board);
          overlayPane.getChildren().addAll(ladderComponents);

          TileView startView = (TileView) boardGrid.lookup("#tile" + startId);
          TileView endView = (TileView) boardGrid.lookup("#tile" + endId);
          if (startView != null) {
            startView.setStyle("-fx-background-color: green;");
          }
          if (endView != null) {
            endView.setStyle("-fx-background-color: #90EE90;");
          }

          logger.log(java.util.logging.Level.FINE, "Drew ladder from " + startId + " to " + endId);
        });
  }

  private static List<Node> createLadderVisual(double[] startCenter, double[] endCenter,
                                               Tile startTile, Board board) {
    List<Node> ladderComponents = new ArrayList<>();

    double dx = endCenter[0] - startCenter[0];
    double dy = endCenter[1] - startCenter[1];
    double length = Math.hypot(dx, dy);

    int rungCount = calculateRungCount(length);
    double[] perpUnit = calculatePerpendicularUnitVector(dx, dy);

    int startRow = (startTile.getTileId() - 1) / LADDERS_BOARD_COLUMNS;
    flipDirection(perpUnit, startRow, board);

    double[][] sideCoords = calculateSideRailCoordinates(startCenter, endCenter, perpUnit);
    addSideRail(ladderComponents, sideCoords[0], sideCoords[1]);
    addSideRail(ladderComponents, sideCoords[2], sideCoords[3]);
    addRungs(ladderComponents, sideCoords, rungCount);

    return ladderComponents;
  }

  public static boolean isValidLadder(int startId, int endId) {
    if (endId <= startId) {
      return false;
    }
    int startRow = (startId - 1) / LADDERS_BOARD_COLUMNS;
    int endRow = (endId - 1) / LADDERS_BOARD_COLUMNS;
    return startRow != endRow;
  }

  private static int calculateRungCount(double length) {
    return Math.max(1, (int) (length / RUNG_SPACING));
  }

  private static double[] calculatePerpendicularUnitVector(double dx, double dy) {
    double length = Math.hypot(dx, dy);
    if (length == 0) {
      return new double[] {0, 0};
    }
    return new double[] {-dy / length, dx / length};
  }

  private static void flipDirection(double[] perp, int startRow, Board board) {
    int totalTiles = board.getTiles().size();
    int totalRows = (int) Math.ceil(totalTiles / (double) LADDERS_BOARD_COLUMNS);
    int rowFromBottom = totalRows - 1 - startRow;
    if (rowFromBottom % 2 == 1) {
      perp[0] = -perp[0];
      perp[1] = -perp[1];
    }
  }

  private static double[][] calculateSideRailCoordinates(double[] start, double[] end,
                                                         double[] perpUnit) {
    double offX = perpUnit[0] * SIDE_OFFSET;
    double offY = perpUnit[1] * SIDE_OFFSET;

    return new double[][] {
        {start[0] + offX, start[1] + offY},
        {end[0] + offX, end[1] + offY},
        {start[0] - offX, start[1] - offY},
        {end[0] - offX, end[1] - offY}
    };
  }

  private static void addSideRail(List<Node> components, double[] start, double[] end) {
    addLine(components, start[0], start[1], end[0], end[1], 4, Color.BROWN);
  }

  private static void addRungs(List<Node> components, double[][] coords, int count) {
    for (int i = 1; i < count; i++) {
      double t = i / (double) count;
      double upperX = linearInterpolation(coords[0][0], coords[1][0], t);
      double upperY = linearInterpolation(coords[0][1], coords[1][1], t);
      double lowerX = linearInterpolation(coords[2][0], coords[3][0], t);
      double lowerY = linearInterpolation(coords[2][1], coords[3][1], t);

      Line rung = new Line(upperX, upperY, lowerX, lowerY);
      rung.setStroke(Color.SANDYBROWN);
      rung.setStrokeWidth(5);
      rung.setEffect(createShadow());
      components.add(rung);
    }
  }

  private static DropShadow createShadow() {
    DropShadow ds = new DropShadow();
    ds.setRadius(10.0);
    ds.setOffsetX(10.0);
    ds.setOffsetY(10.0);
    ds.setColor(Color.color(0, 0, 0.6, 0.9));
    return ds;
  }

  private static void addLine(List<Node> components, double x1, double y1, double x2, double y2,
                              double width, Color color) {
    Line line = new Line(x1, y1, x2, y2);
    line.setStroke(color);
    line.setStrokeWidth(width);
    components.add(line);
  }

  private static double linearInterpolation(double a, double b, double t) {
    return a + t * (b - a);
  }
}