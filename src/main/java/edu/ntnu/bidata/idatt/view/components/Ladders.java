package edu.ntnu.bidata.idatt.view.components;

import java.awt.Point;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Ladders {
  private Rectangle topLadder, bottomLadder;
  private Color color;

  public Ladders(Rectangle topLadder, Rectangle bottomLadder) {
    this.topLadder = topLadder;
    this.bottomLadder = bottomLadder;
  }

  public void drawLadder() {

  }

  private Point getTopLadder() {
    Point point = new Point();
    point.setLocation(topLadder.getX(), topLadder.getY());
    return point;
  }

  private Point getBottomLadder() {
    Point point = new Point();
    point.setLocation(bottomLadder.getX(), bottomLadder.getY());
    return point;
  }
}
