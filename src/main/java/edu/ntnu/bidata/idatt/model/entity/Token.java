package edu.ntnu.bidata.idatt.model.entity;

import javafx.scene.paint.Color;

public class Token {
  private final Color color;
  private final String shape;

  public Token(Color color, String shape) {
    this.color = color;
    this.shape = shape;
  }

  public static Token token(Color color, String shape) {
    return new Token(color, shape);
  }

  public Color getColor() {
    return color;
  }

  public String getShape() {
    return shape;
  }

}
