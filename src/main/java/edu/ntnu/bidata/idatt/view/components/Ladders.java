package edu.ntnu.bidata.idatt.view.components;

import java.awt.Point;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Ladders {
  private Rectangle topLadder, bottomLadder;
  private Color color;

  public Ladders(Rectangle topLadder, Rectangle bottomLadder) {
    this.topLadder = topLadder;
    this.bottomLadder = bottomLadder;
  }

  public void drawLadder() {
    Line line = new Line();
    line.setStartX(200);
    line.setStartY(200);
    line.setEndX(500);
    line.setEndY(200);
    line.setStrokeWidth(5);
    line.setStroke(Color.RED);
    line.setOpacity(0.5);
    line.setRotate(45);
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
