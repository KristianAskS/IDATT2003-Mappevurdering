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
  private static final double SIZE = TILE_SIZE * 0.4;
  private static final double HALF_SIZE = SIZE / 2.0;
  private static final double TRI_OFFSET = HALF_SIZE;
  private final Token token;

  public TokenView(Token token) {
    this.token = token;

    if (token.getImagePath() != null) {
      ImageView view = new ImageView(new Image(token.getImagePath(), true));
      view.setPreserveRatio(true);
      view.setSmooth(true);
      view.setFitWidth(SIZE);
      view.setFitHeight(SIZE);
      view.setClip(new Rectangle(SIZE, SIZE));
      getChildren().add(view);

    } else {
      switch (token.getShape().toLowerCase()) {
        case "circle" -> {
          Ellipse circle = new Ellipse(HALF_SIZE, HALF_SIZE);
          circle.setFill(token.getColor());
          setStrokeHandler(circle);
          getChildren().add(circle);
        }
        case "square" -> {
          Rectangle square = new Rectangle(SIZE, SIZE);
          square.setFill(token.getColor());
          square.setStroke(Color.BLACK);
          square.setStrokeWidth(3);
          getChildren().add(square);
        }
        case "triangle" -> {
          javafx.scene.shape.Polygon triangle = new javafx.scene.shape.Polygon(
              0.0, -TRI_OFFSET,
              -TRI_OFFSET, TRI_OFFSET,
              TRI_OFFSET, TRI_OFFSET
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
        new Object[] {token.getColor(), token.getShape()});
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
