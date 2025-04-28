package edu.ntnu.bidata.idatt.model.entity;

import javafx.scene.paint.Color;

public class Token {
  private final Color color;
  private final String shape;
  private final String imagePath;

  /**
   * Constructor for a token with a color and shape
   * @param color
   * @param shape
   */
  public Token(Color color, String shape) {
    this.color = color;
    this.shape = shape;
    this.imagePath = null;
  }

  public Token(Color color, String shape, String imagePath) {
    this.color = color;
    this.shape = shape == null ? "" : shape;
    this.imagePath = imagePath;
  }

  public static Token token(Color color, String shape) {
    return new Token(color, shape, null);
  }

  public static Token token(Color color, String shape, String imagePath) {
    return new Token(color, shape, imagePath);
  }

  public Color getColor() {
    return color;
  }

  public String getShape() {
    return shape;
  }

  public String getImagePath() {
    return imagePath;
  }

}
