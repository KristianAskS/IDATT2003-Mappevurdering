package edu.ntnu.bidata.idatt.view.components;

import static edu.ntnu.bidata.idatt.view.components.TileView.TILE_SIZE_LADDER;
import static edu.ntnu.bidata.idatt.view.components.TileView.TILE_SIZE_LUDO;

import edu.ntnu.bidata.idatt.model.entity.Token;
import edu.ntnu.bidata.idatt.view.scenes.GameSelectionScene;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;

/**
 * <p>Visual component that renders a {@link Token} in the game UI.</p>
 *
 * <p>If the token has an associated image path, an {@link ImageView} is shown;
 * otherwise a shape (circle, square, or triangle) is drawn.
 * The tokens size adapts to the selected game
 * (“LUDO” uses {@code TILE_SIZE_LUDO}, otherwise {@code TILE_SIZE_LADDER}).</p>
 *
 * @author Tri Tac Le
 * @since 1.0
 */
public class TokenView extends StackPane {

  private static final Logger logger = Logger.getLogger(TokenView.class.getName());

  private static final double SIZE = (
      "LUDO".equalsIgnoreCase(String.valueOf(GameSelectionScene.getSelectedGame())) ?
          TILE_SIZE_LUDO : TILE_SIZE_LADDER) * 0.4;
  private static final double HALF_SIZE = SIZE / 2.0;
  private static final double TRI_OFFSET = HALF_SIZE;
  private final Token token;

  /**
   * Constructs a TokenView for the given token.
   * <p>Renders either an image or a colored shape.</p>
   *
   * @param token the {@link Token} to visualize
   */
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
          Polygon triangle = new Polygon(
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

  /**
   * Configures stroke styling on the ellipse.
   *
   * @param token the {@link Ellipse} to style
   */
  private static void setStrokeHandler(Ellipse token) {
    token.setStrokeType(StrokeType.CENTERED);
    token.setStroke(Color.BLACK);
    token.setStrokeWidth(3);
    token.setSmooth(true);
  }

  /**
   * Returns the token’s display color.
   *
   * @return the {@link Color} of the token
   */
  public Color getTokenColor() {
    return token.getColor();
  }

  /**
   * Returns the token’s shape.
   *
   * @return the shape string
   */
  public String getTokenShape() {
    return token.getShape();
  }

  /**
   * Returns the token’s image path
   *
   * @return the image path, or null if the token is shape‑based
   */
  public String getImagePath() {
    return token.getImagePath();
  }

  /**
   * Returns the {@link Token} instance
   *
   * @return the token
   */
  public Token getToken() {
    return token;
  }
}
