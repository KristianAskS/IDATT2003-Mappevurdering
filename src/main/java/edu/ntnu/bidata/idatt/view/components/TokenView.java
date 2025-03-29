package edu.ntnu.bidata.idatt.view.components;

import static edu.ntnu.bidata.idatt.view.components.TileView.TILE_SIZE;

import edu.ntnu.bidata.idatt.entity.TokenType;
import edu.ntnu.bidata.idatt.patterns.observer.ConsoleBoardGameObserver;
import java.util.logging.Logger;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class TokenView extends StackPane {
  private static final Logger logger = Logger.getLogger(ConsoleBoardGameObserver.class.getName());
  private TokenType tokenType;
  private double oldXCord, oldYCord;
  private double newXCord, newYCord;

  public TokenView(TokenType tokenType, int x, int y) {
    relocate(x * TILE_SIZE, y * TILE_SIZE);

    Ellipse token = new Ellipse(0.2 * TILE_SIZE, 0.2 * TILE_SIZE);
    token.setFill(tokenType.getColor());
    token.setStroke(Color.BLACK);
    token.setStrokeWidth(0.03);
    token.setTranslateX((TILE_SIZE - 0.2 * TILE_SIZE * 2) / 2);
    token.setTranslateY((TILE_SIZE - 0.2 * TILE_SIZE * 2) / 2);

    Ellipse ellipse = new Ellipse(0.2 * TILE_SIZE, 0.2 * TILE_SIZE);
    ellipse.setFill(tokenType.getColor());
    ellipse.setStroke(Color.PINK);
    ellipse.setStrokeWidth(0.03);
    token.setTranslateX((TILE_SIZE - 0.2 * TILE_SIZE * 2) / 2);
    token.setTranslateY((TILE_SIZE - 0.2 * TILE_SIZE * 2) / 2);

    getChildren().addAll(token, ellipse);

    logger.info("TokenView created");
  }
}
