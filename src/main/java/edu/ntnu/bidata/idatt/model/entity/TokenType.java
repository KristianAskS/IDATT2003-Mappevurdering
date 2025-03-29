package edu.ntnu.bidata.idatt.model.entity;

import javafx.scene.paint.Color;

/**
 * Add images or figures later
 */
public enum TokenType {
  RED(Color.RED),
  GREEN(Color.GREEN),
  BLUE(Color.BLUE),
  YELLOW(Color.YELLOW),
  PINK(Color.PINK);
  private final Color color;

  TokenType(Color color) {
    this.color = color;
  }

  public Color getColor() {
    return color;
  }
}
