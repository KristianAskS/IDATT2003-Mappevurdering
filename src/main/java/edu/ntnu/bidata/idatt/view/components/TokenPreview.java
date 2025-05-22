package edu.ntnu.bidata.idatt.view.components;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

/**
 * <p>Class for creating a small preview node of a player’s token.</p>
 *
 * <p>If the {@link TokenView} contains an image path, an {@link ImageView} is created.
 * Otherwise, a shape (circle, triangle, or square) is drawn with the token’s color
 * and a black stroke.</p>
 *
 * @author Kristian Ask Selmer
 * @since 1.0
 */
public final class TokenPreview {
  private static final int COLOR_BOX_SIZE = 25;
  private static final int IMAGE_SIZE = 30;

  private TokenPreview() {
  }

  /**
   * Creates a visual preview of the {@link TokenView}.
   *
   * <p>If an image path is available, returns an {@link ImageView} sized to
   * {@code IMAGE_SIZE}. Otherwise, returns a filled shape
   * (circle, triangle, or square) sized to {@code COLOR_BOX_SIZE}.</p>
   *
   * @param token the {@link TokenView} containing color, shape, and optional image
   * @return a {@link Node} representing the token preview
   */
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
          Circle circle = new Circle(COLOR_BOX_SIZE / 2.0, tokenCol);
          circle.setStroke(Color.BLACK);
          tokenPreview = circle;
        }
        case "triangle" -> {
          Polygon triangle = new Polygon(
              COLOR_BOX_SIZE / 2.0, 0,
              COLOR_BOX_SIZE, COLOR_BOX_SIZE,
              0, COLOR_BOX_SIZE
          );
          triangle.setFill(tokenCol);
          triangle.setStroke(Color.BLACK);
          tokenPreview = triangle;
        }
        default -> {
          Rectangle rectangle = new Rectangle(COLOR_BOX_SIZE, COLOR_BOX_SIZE, tokenCol);
          rectangle.setStroke(Color.BLACK);
          tokenPreview = rectangle;
        }
      }
    }
    return tokenPreview;
  }
}
