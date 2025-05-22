package edu.ntnu.bidata.idatt.model.entity;

import javafx.scene.paint.Color;

/**
 * <p>Represents a visual token used by a player on the game board.</p>
 * <p>Encapsulates the token’s color, shape, and
 * an optional image path.</p>
 *
 * @author Tri Tac Le, Kristian Ask Selmer
 * @since 1.0
 */
public class Token {
  private final Color color;
  private final String shape;
  private final String imagePath;

  /**
   * Constructs a token with the color and shape.
   * The image path is set to {@code null}.
   *
   * @param color the {@link Color} of the token
   * @param shape the shape of the token
   */
  public Token(Color color, String shape) {
    this.color = color;
    this.shape = shape;
    this.imagePath = null;
  }

  /**
   * Constructs a token with the color, shape, and image path.
   *
   * @param color     the {@link Color} of the token
   * @param shape     the shape of the token; if {@code null}, defaults to empty string
   * @param imagePath the path to the token’s image resource; may be {@code null}
   */
  public Token(Color color, String shape, String imagePath) {
    this.color = color;
    this.shape = (shape == null ? "" : shape);
    this.imagePath = imagePath;
  }

  /**
   * Static factory method to create a token with color and shape.
   *
   * @param color the {@link Color} of the token
   * @param shape the shape identifier of the token
   * @return a new {@link Token} instance with no image path
   */
  public static Token token(Color color, String shape) {
    return new Token(color, shape, null);
  }

  /**
   * Static factory method to create a token with color, shape, and image path.
   *
   * @param color     the {@link Color} of the token
   * @param shape     the shape identifier of the token
   * @param imagePath the path to the token’s image resource
   * @return a new {@link Token} instance
   */
  public static Token token(Color color, String shape, String imagePath) {
    return new Token(color, shape, imagePath);
  }

  /**
   * Returns the token’s display color.
   *
   * @return the {@link Color} of this token
   */
  public Color getColor() {
    return color;
  }

  /**
   * Returns the token’s shape identifier.
   *
   * @return the shape of this token
   */
  public String getShape() {
    return shape;
  }

  /**
   * Returns the token’s image path.
   *
   * @return the image path, or {@code null} if none is set
   */
  public String getImagePath() {
    return imagePath;
  }
}
