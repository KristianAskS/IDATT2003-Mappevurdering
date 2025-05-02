package edu.ntnu.bidata.idatt.model.entity;

import static edu.ntnu.bidata.idatt.model.entity.Ladder.VISUAL_CORRECTION;

import edu.ntnu.bidata.idatt.controller.GameController;
import edu.ntnu.bidata.idatt.view.components.BaseBoardView;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Snake {

  private static final Logger log = Logger.getLogger(Snake.class.getName());

  private final List<Node> snakes = new ArrayList<>();

  public Snake(Tile startTile, Tile endTile, Board board, GridPane grid,
      GameController controller) {

    double[] headC = tileCenter(grid, controller, board, startTile);
    double[] tailC = tileCenter(grid, controller, board, endTile);

    double dx = tailC[0] - headC[0];
    double dy = tailC[1] - headC[1];
    double len = Math.hypot(dx, dy);
    double ux = dx / len;
    double uy = dy / len;
    double px = -uy;
    double py = ux;

    Line body = new Line(headC[0], headC[1], tailC[0], tailC[1]);
    body.setStroke(Color.DARKGREEN);
    body.setStrokeWidth(12);
    body.setEffect(new DropShadow(6, Color.web("#00000066")));
    snakes.add(body);

    double headR = 15;
    Circle head = new Circle(headC[0], headC[1], headR, Color.DARKGREEN);
    head.setStrokeWidth(0);
    snakes.add(head);

    double eyeR = 3;
    double back = headR * 0.35;
    double side = headR * 0.45;

    double lx = headC[0] - ux * back + px * side;
    double ly = headC[1] - uy * back + py * side;
    double rx = headC[0] - ux * back - px * side;
    double ry = headC[1] - uy * back - py * side;

    snakes.add(new Circle(lx, ly, eyeR, Color.BLACK));
    snakes.add(new Circle(rx, ry, eyeR, Color.BLACK));

  }

  private static double[] tileCenter(GridPane grid, GameController gc, Board board, Tile tile) {
    int[] rc = gc.tileToGridPosition(tile, board);
    Bounds b = BaseBoardView.getTileNodeAt(grid, rc[0], rc[1]).getBoundsInParent();
    double x = b.getMinX() + b.getWidth() * 0.5 + VISUAL_CORRECTION;
    double y = b.getMinY() + b.getHeight() * 0.5;
    return new double[]{x, y};
  }

  public List<Node> getSnakes() {
    return List.copyOf(snakes);
  }
}