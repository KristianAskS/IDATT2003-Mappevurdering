package edu.ntnu.bidata.idatt.view.components;

import static edu.ntnu.bidata.idatt.view.components.TileView.TILE_SIZE;

import edu.ntnu.bidata.idatt.entity.TokenType;
import edu.ntnu.bidata.idatt.patterns.observer.ConsoleBoardGameObserver;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.StrokeType;

public class TokenView extends StackPane {
  private static final Logger logger = Logger.getLogger(ConsoleBoardGameObserver.class.getName());
  private TokenType tokenType;
  private double oldXCord, oldYCord;
  private double newXCord, newYCord;

  public TokenView(TokenType tokenType, int x, int y) {
    relocate(x * TILE_SIZE, y * TILE_SIZE);

    Ellipse token = new Ellipse(0.2 * TILE_SIZE, 0.2 * TILE_SIZE);
    token.setFill(tokenType.getColor());
    TokenView.setStrokeHandler(token);
    getChildren().addAll(token);

    logger.log(Level.INFO,"TokenView created");
  }

  private static void setStrokeHandler(Ellipse token) {
    token.setStrokeType(StrokeType.CENTERED);
    token.setStroke(Color.BLACK);
    token.setStrokeWidth(3);
    token.setSmooth(true);
  }
}
