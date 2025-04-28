package edu.ntnu.bidata.idatt.view.components;

import static edu.ntnu.bidata.idatt.view.components.TileView.TILE_SIZE;

import edu.ntnu.bidata.idatt.model.entity.Token;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

public class TokenView extends StackPane {

  private static final Logger logger = Logger.getLogger(TokenView.class.getName());
  private final Token token;


  public TokenView(Token token) {
    this.token = token;

    if (token.getImagePath() != null) {
      double size = TILE_SIZE * 0.4;

      Image image = new Image(token.getImagePath(), true);
      ImageView view = new ImageView(image);

      view.setPreserveRatio(true);
      view.setSmooth(true);
      view.setFitWidth(size);
      view.setFitHeight(size);

      Rectangle clip = new Rectangle(size, size);
      view.setClip(clip);

      getChildren().add(view);

    } else {

      switch (token.getShape().toLowerCase()) {
        case "circle" -> {
          Ellipse circle = new Ellipse(0.2 * TILE_SIZE, 0.2 * TILE_SIZE);
          circle.setFill(token.getColor());
          setStrokeHandler(circle);
          getChildren().add(circle);
        }
        case "square" -> {
          javafx.scene.shape.Rectangle square =
              new javafx.scene.shape.Rectangle(0.4 * TILE_SIZE, 0.4 * TILE_SIZE);
          square.setFill(token.getColor());
          square.setStroke(Color.BLACK);
          square.setStrokeWidth(3);
          getChildren().add(square);
        }
        case "triangle" -> {
          javafx.scene.shape.Polygon triangle = new javafx.scene.shape.Polygon();
          triangle.getPoints().addAll(
              0.0, -0.2 * TILE_SIZE,
              -0.2 * TILE_SIZE, 0.2 * TILE_SIZE,
              0.2 * TILE_SIZE, 0.2 * TILE_SIZE
          );
          triangle.setFill(token.getColor());
          triangle.setStroke(Color.BLACK);
          triangle.setStrokeWidth(3);
          getChildren().add(triangle);
        }
        default -> logger.warning("Unknown shape: " + token.getShape());
      }
    }

    logger.log(Level.INFO, "TokenView created with color {0} and shape {1}",
        new Object[]{token.getColor(), token.getShape()});
  }

  private static void setStrokeHandler(Ellipse token) {
    token.setStrokeType(StrokeType.CENTERED);
    token.setStroke(Color.BLACK);
    token.setStrokeWidth(3);
    token.setSmooth(true);
  }

  public Color getTokenColor() {
    return token.getColor();
  }

  public String getTokenShape() {
    return token.getShape();
  }

  public String getImagePath() {
    return token.getImagePath();
  }
}
