package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.controller.GameController;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.model.logic.action.SnakeAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.StrokeLineCap;

public class SnakeRenderer {

  private static final Logger logger = Logger.getLogger(SnakeRenderer.class.getName());
  private static final Random random = new Random();

  private static final double SNAKE_BODY_STROKE_WIDTH = 14;
  private static final double SNAKE_AMPLITUDE = 20;
  private static final double SNAKE_HEAD_RADIUS = 14;
  private static final double SNAKE_EYE_OFFSET_X = 4;
  private static final double SNAKE_EYE_OFFSET_Y = 3;
  private static final double SNAKE_TONGUE_STROKE_WIDTH = 2;
  private static final int SNAKE_SEGMENT_LENGTH = 40;


  private SnakeRenderer() {
  }

  public static void drawSnakes(Board board, GridPane boardGrid, Pane overlayPane,
                                GameController gameController) {
    board.getTiles().values().stream()
        .filter(
            tile -> tile.getLandAction() instanceof SnakeAction)
        .forEach(startTile -> {
          SnakeAction snakeAction =
              (SnakeAction) startTile.getLandAction();
          int startId = startTile.getTileId();
          int endId = snakeAction.getDestinationTileId();

          if (isValidSnake(startId, endId)) {
            logger.warning("Invalid snake defined from " + startId + " to " + endId);
            return;
          }

          Tile endTile = board.getTile(endId);
          if (endTile == null) {
            logger.severe(
                "Snake destination tile " + endId + " not found for start tile " + startId);
            return;
          }

          double[] headCenter = BoardViewUtils.getTileCenter(boardGrid, gameController, board,
              startTile);
          double[] tailCenter = BoardViewUtils.getTileCenter(boardGrid, gameController, board,
              endTile);

          List<Node> snakeComponents = createSnakeVisual(headCenter, tailCenter);
          overlayPane.getChildren().addAll(snakeComponents);

          TileView startView = (TileView) boardGrid.lookup("#tile" + startId);
          TileView endView = (TileView) boardGrid.lookup("#tile" + endId);
          if (startView != null) {
            startView.setStyle("-fx-background-color:red;");
          }
          if (endView != null) {
            endView.setStyle("-fx-background-color:#FF474D;");
          }

          logger.log(java.util.logging.Level.FINE, "Drew snake from " + startId + " to " + endId);
        });
  }

  private static List<Node> createSnakeVisual(double[] headCenter, double[] tailCenter) {
    List<Node> snakeComponents = new ArrayList<>();
    Color bodyColor = getRandomSnakeColor();

    Path body = new Path();
    body.setStroke(bodyColor);
    body.setStrokeWidth(SNAKE_BODY_STROKE_WIDTH);
    body.setFill(null);
    body.setStrokeLineCap(StrokeLineCap.ROUND);
    body.setEffect(new DropShadow(6, Color.rgb(0, 0, 0, 0.3)));

    double totalX = tailCenter[0] - headCenter[0];
    double totalY = tailCenter[1] - headCenter[1];
    double length = Math.hypot(totalX, totalY);
    int segments = (int) (length / SNAKE_SEGMENT_LENGTH);
    if (segments == 0) {
      segments = 1;
    }
    double unitX = totalX / segments;
    double unitY = totalY / segments;
    double angle = Math.atan2(totalY, totalX);

    body.getElements().add(new MoveTo(headCenter[0], headCenter[1]));
    for (int i = 1; i <= segments; i++) {
      double prevX = headCenter[0] + unitX * (i - 1);
      double prevY = headCenter[1] + unitY * (i - 1);
      double nextX = headCenter[0] + unitX * i;
      double nextY = headCenter[1] + unitY * i;
      double offsetX = -SNAKE_AMPLITUDE * Math.sin(angle) * ((i % 2 == 0) ? 1 : -1);
      double offsetY = SNAKE_AMPLITUDE * Math.cos(angle) * ((i % 2 == 0) ? 1 : -1);
      double ctrlX = (prevX + nextX) / 2 + offsetX;
      double ctrlY = (prevY + nextY) / 2 + offsetY;
      body.getElements().add(new QuadCurveTo(ctrlX, ctrlY, nextX, nextY));
    }
    snakeComponents.add(body);

    Circle head = new Circle(headCenter[0], headCenter[1], SNAKE_HEAD_RADIUS, bodyColor);
    head.setEffect(new DropShadow(4, Color.rgb(0, 0, 0, 0.4)));
    snakeComponents.add(head);

    snakeComponents.add(
        new Circle(headCenter[0] - SNAKE_EYE_OFFSET_X, headCenter[1] - SNAKE_EYE_OFFSET_Y, 2,
            Color.WHITE));
    snakeComponents.add(
        new Circle(headCenter[0] + SNAKE_EYE_OFFSET_X, headCenter[1] - SNAKE_EYE_OFFSET_Y, 2,
            Color.WHITE));
    snakeComponents.add(
        new Circle(headCenter[0] - SNAKE_EYE_OFFSET_X, headCenter[1] - SNAKE_EYE_OFFSET_Y, 1,
            Color.BLACK));
    snakeComponents.add(
        new Circle(headCenter[0] + SNAKE_EYE_OFFSET_X, headCenter[1] - SNAKE_EYE_OFFSET_Y, 1,
            Color.BLACK));

    Polyline tongue = new Polyline(
        headCenter[0], headCenter[1] - SNAKE_HEAD_RADIUS,
        headCenter[0] - 3, headCenter[1] - SNAKE_HEAD_RADIUS - 8,
        headCenter[0], headCenter[1] - SNAKE_HEAD_RADIUS - 4,
        headCenter[0] + 3, headCenter[1] - SNAKE_HEAD_RADIUS - 8,
        headCenter[0], headCenter[1] - SNAKE_HEAD_RADIUS
    );
    tongue.setStroke(Color.RED);
    tongue.setStrokeWidth(SNAKE_TONGUE_STROKE_WIDTH);
    snakeComponents.add(tongue);

    return snakeComponents;
  }

  private static Color getRandomSnakeColor() {
    Color[] colors = {
        Color.DARKGREEN, Color.FORESTGREEN, Color.OLIVEDRAB, Color.SADDLEBROWN,
        Color.BLACK, Color.SEAGREEN, Color.DARKOLIVEGREEN, Color.DARKSLATEGRAY,
        Color.MAROON, Color.INDIGO, Color.TEAL, Color.NAVY,
        Color.DARKRED, Color.DARKBLUE, Color.DARKMAGENTA, Color.MEDIUMSEAGREEN,
        Color.LIMEGREEN, Color.DEEPPINK, Color.GOLD, Color.CORNFLOWERBLUE,
        Color.DARKORANGE, Color.SIENNA, Color.DARKCYAN, Color.PURPLE
    };
    return colors[random.nextInt(colors.length)];
  }

  public static boolean isValidSnake(int headId, int tailId) {
    if (headId <= tailId) {
      return true;
    }
    int headRow = (headId - 1) / 10;
    int tailRow = (tailId - 1) / 10;
    return headRow == tailRow;
  }
}