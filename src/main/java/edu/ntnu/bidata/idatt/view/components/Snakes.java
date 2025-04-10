package edu.ntnu.bidata.idatt.view.components;

import java.awt.Point;
import java.awt.Rectangle;
import javafx.scene.paint.Color;

public class Snakes {
  private final Rectangle topSnake;
  private final Rectangle bottomSnake;
  private Color color;

  public Snakes(Rectangle topSnake, Rectangle bottomSnake) {
    this.topSnake = topSnake;
    this.bottomSnake = bottomSnake;
  }

  public void drawSnake() {

  }

  public Point getTail() {
    Point point = new Point();
    point.setLocation(topSnake.getCenterX(), bottomSnake.getCenterY());
    return point;
  }

  public Point getHead() {
    Point point = new Point();
    point.setLocation(topSnake.getCenterX(), bottomSnake.getCenterY());
    return point;
  }
}
