package edu.ntnu.bidata.idatt.view.components;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public final class TokenPreview {

  private static final int COLOR_BOX_SIZE = 25;
  private static final int IMAGE_SIZE = 30;

  private TokenPreview() {
  }

  public static Node create(TokenView token) {
    Node tokenPreview;
    String imgPath = token.getImagePath();
    String shapeStr = token.getTokenShape() == null
        ? "" : token.getTokenShape().toLowerCase();
    Color tokenCol = token.getTokenColor();
    if (imgPath != null && !imgPath.isBlank()) {
      tokenPreview = new ImageView(
          new Image(imgPath, IMAGE_SIZE, IMAGE_SIZE, true, true)
      );
    } else {
      switch (shapeStr) {
        case "circle" -> {
          Circle c = new Circle(COLOR_BOX_SIZE / 2.0, tokenCol);
          c.setStroke(Color.BLACK);
          tokenPreview = c;
        }
        case "triangle" -> {
          Polygon t = new Polygon(
              COLOR_BOX_SIZE / 2.0, 0,
              COLOR_BOX_SIZE, COLOR_BOX_SIZE,
              0, COLOR_BOX_SIZE
          );
          t.setFill(tokenCol);
          t.setStroke(Color.BLACK);
          tokenPreview = t;
        }
        default -> {
          Rectangle r = new Rectangle(COLOR_BOX_SIZE, COLOR_BOX_SIZE, tokenCol);
          r.setStroke(Color.BLACK);
          tokenPreview = r;
        }
      }


    }
    return tokenPreview;
  }
}

