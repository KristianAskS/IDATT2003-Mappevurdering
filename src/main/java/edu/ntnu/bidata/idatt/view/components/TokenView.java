package edu.ntnu.bidata.idatt.view.components;

import static edu.ntnu.bidata.idatt.view.components.TileView.TILE_SIZE;

import edu.ntnu.bidata.idatt.controller.patterns.observer.ConsoleBoardGameObserver;
import edu.ntnu.bidata.idatt.view.scenes.PlayerSelectionScene;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.StrokeType;

public class TokenView extends StackPane {
  private static final Logger logger = Logger.getLogger(ConsoleBoardGameObserver.class.getName());
  private final Color color;
  private final String shape;

  public TokenView() {
    this(PlayerSelectionScene.getColorPicker(), "Circle");
  }

  public TokenView(Color color, String shape) {
    this.color = color;
    this.shape = shape;

    switch (shape.toLowerCase()) {
      case "circle" -> {
        Ellipse circle = new Ellipse(0.2 * TILE_SIZE, 0.2 * TILE_SIZE);
        circle.setFill(color);
        setStrokeHandler(circle);
        getChildren().add(circle);
      }
      case "square" -> {
        javafx.scene.shape.Rectangle square =
            new javafx.scene.shape.Rectangle(0.4 * TILE_SIZE, 0.4 * TILE_SIZE);
        square.setFill(color);
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
        triangle.setFill(color);
        triangle.setStroke(Color.BLACK);
        triangle.setStrokeWidth(3);
        getChildren().add(triangle);
      }
    }

    logger.log(Level.INFO, "TokenView created with custom color and shape");
  }

  private static void setStrokeHandler(Ellipse token) {
    token.setStrokeType(StrokeType.CENTERED);
    token.setStroke(Color.BLACK);
    token.setStrokeWidth(3);
    token.setSmooth(true);
  }

  public Color getTokenColor() {
    return color;
  }

  public String getTokenShape() {
    return shape;
  }
}
