package edu.ntnu.bidata.idatt.model.entity;

import static edu.ntnu.bidata.idatt.model.entity.Ladder.VISUAL_CORRECTION;

import edu.ntnu.bidata.idatt.controller.GameController;
import edu.ntnu.bidata.idatt.view.components.BaseBoardView;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.QuadCurveTo;
import javafx.scene.shape.StrokeLineCap;

public class Snake {

  private final List<Node> snakes = new ArrayList<>();
  private final Random random = new Random();

  public Snake(Tile startTile, Tile endTile, Board board, GridPane grid,
               GameController controller) {

    double[] headCenter = getTileCenter(grid, controller, board, startTile);
    double[] tailCenter = getTileCenter(grid, controller, board, endTile);

    Path body = new Path();
    Color bodyColor = getRandomSnakeColor();
    body.setStroke(bodyColor);
    body.setStrokeWidth(14);
    body.setFill(null);
    body.setStrokeLineCap(StrokeLineCap.ROUND);
    body.setEffect(new DropShadow(6, Color.rgb(0, 0, 0, 0.3)));

    double totalX = tailCenter[0] - headCenter[0];
    double totalY = tailCenter[1] - headCenter[1];
    double length = Math.hypot(totalX, totalY);
    int segments = (int) (length / 40);
    double unitX = totalX / segments;
    double unitY = totalY / segments;
    double angle = Math.atan2(totalY, totalX);
    double amplitude = 20;

    body.getElements().add(new MoveTo(headCenter[0], headCenter[1]));

    for (int i = 1; i <= segments; i++) {
      double prevX = headCenter[0] + unitX * (i - 1);
      double prevY = headCenter[1] + unitY * (i - 1);
      double nextX = headCenter[0] + unitX * i;
      double nextY = headCenter[1] + unitY * i;
      double offsetX = -amplitude * Math.sin(angle) * ((i % 2 == 0) ? 1 : -1);
      double offsetY = amplitude * Math.cos(angle) * ((i % 2 == 0) ? 1 : -1);
      double ctrlX = (prevX + nextX) / 2 + offsetX;
      double ctrlY = (prevY + nextY) / 2 + offsetY;
      body.getElements().add(new QuadCurveTo(ctrlX, ctrlY, nextX, nextY));

      double patternAngle = Math.atan2(nextY - prevY, nextX - prevX);
      double patternLength = 8;
      double dx = Math.cos(patternAngle) * patternLength / 2;
      double dy = Math.sin(patternAngle) * patternLength / 2;
      // TODO: add pattern maybe
    }

    snakes.add(body);

    double headR = 14;
    Circle head = new Circle(headCenter[0], headCenter[1], headR, bodyColor);
    head.setEffect(new DropShadow(4, Color.rgb(0, 0, 0, 0.4)));
    snakes.add(head);

    double eyeOffsetX = 4;
    double eyeOffsetY = 3;
    snakes.add(new Circle(headCenter[0] - eyeOffsetX, headCenter[1] - eyeOffsetY, 2, Color.WHITE));
    snakes.add(new Circle(headCenter[0] + eyeOffsetX, headCenter[1] - eyeOffsetY, 2, Color.WHITE));
    snakes.add(new Circle(headCenter[0] - eyeOffsetX, headCenter[1] - eyeOffsetY, 1, Color.BLACK));
    snakes.add(new Circle(headCenter[0] + eyeOffsetX, headCenter[1] - eyeOffsetY, 1, Color.BLACK));

    Polyline tongue = new Polyline(
        headCenter[0], headCenter[1] - headR,
        headCenter[0] - 3, headCenter[1] - headR - 8,
        headCenter[0], headCenter[1] - headR - 4,
        headCenter[0] + 3, headCenter[1] - headR - 8,
        headCenter[0], headCenter[1] - headR
    );
    tongue.setStroke(Color.RED);
    tongue.setStrokeWidth(2);
    snakes.add(tongue);
  }

  private static double[] getTileCenter(GridPane grid, GameController gameController, Board board,
                                        Tile tile) {
    int[] rowCol = gameController.tileToGridPosition(tile, board);
    Bounds bounds = Objects.requireNonNull(BaseBoardView.getTileNodeAt(grid, rowCol[0], rowCol[1]))
        .getBoundsInParent();
    double x = bounds.getMinX() + bounds.getWidth() * 0.5 + VISUAL_CORRECTION;
    double y = bounds.getMinY() + bounds.getHeight() * 0.5;
    return new double[] {x, y};
  }

  private Color getRandomSnakeColor() {
    Color[] colors = {
        Color.DARKGREEN,
        Color.FORESTGREEN,
        Color.OLIVEDRAB,
        Color.SADDLEBROWN,
        Color.BLACK,
        Color.SEAGREEN,
        Color.DARKOLIVEGREEN,
        Color.DARKSLATEGRAY,
        Color.MAROON,
        Color.INDIGO,
        Color.TEAL,
        Color.NAVY,
        Color.DARKRED,
        Color.DARKBLUE,
        Color.DARKMAGENTA,
        Color.MEDIUMSEAGREEN,
        Color.LIMEGREEN,
        Color.DEEPPINK,
        Color.GOLD,
        Color.CORNFLOWERBLUE,
        Color.DARKORANGE,
        Color.SIENNA,
        Color.DARKCYAN,
        Color.PURPLE
    };
    return colors[random.nextInt(colors.length)];
  }

  public List<Node> getSnakes() {
    return List.copyOf(snakes);
  }
}