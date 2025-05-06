package edu.ntnu.bidata.idatt.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Token Tests")
class TokenTest {

  @Nested
  @DisplayName("Positive Tests")
  class PositiveTests {

    @Test
    @DisplayName("getColor returns the initialized color")
    void getColorReturnsInitializedColor() {
      Color expected = Color.RED;
      Token token = new Token(expected, "circle");
      Color actual = token.getColor();
      assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getShape returns the initialized shape")
    void getShapeReturnsInitializedShape() {
      String expected = "square";
      Token token = new Token(Color.BLUE, expected);

      String actual = token.getShape();

      assertEquals(expected, actual);
    }

    @Test
    @DisplayName("getImagePath returns the provided imagePath")
    void getImagePathReturnsProvidedImagePath() {
      String expected = "/images/token.png";
      Token token = new Token(Color.GREEN, "triangle", expected);

      String actual = token.getImagePath();

      assertEquals(expected, actual);
    }

    @Test
    @DisplayName("static factory token(color, shape) sets imagePath to null")
    void staticFactoryWithoutImagePathSetsNull() {
      Token token = Token.token(Color.BLACK, "hexagon");

      String imagePath = token.getImagePath();

      assertNull(imagePath);
      assertEquals("hexagon", token.getShape());
    }

    @Test
    @DisplayName("static factory token(color, shape, imagePath) returns full constructor behavior")
    void staticFactoryWithImagePathMatchesConstructor() {
      String path = "path/to/img.jpg";
      Token token = Token.token(Color.PURPLE, "circle", path);

      assertEquals(path, token.getImagePath());
      assertEquals("circle", token.getShape());
      assertEquals(Color.PURPLE, token.getColor());
    }
  }

  @Nested
  @DisplayName("Negative Tests")
  class NegativeTests {

    @Test
    @DisplayName("constructor handles null shape by converting to empty string")
    void constructorNullShapeConvertsToEmptyString() {
      Token token = new Token(Color.AQUA, null, "unused");

      String actualShape = token.getShape();

      assertEquals("", actualShape);
    }

    @Test
    @DisplayName("shape is empty string when static factory passed null shape")
    void staticFactoryNullShapeYieldsEmptyString() {
      Token token = Token.token(Color.BEIGE, null);

      String actualShape = token.getShape();

      assertEquals("", actualShape);
    }

    @Test
    @DisplayName("imagePath remains null when using two-arg constructor")
    void imagePathRemainsNullWithTwoConstructor() {
      Token token = new Token(Color.CYAN, "circle");

      String imagePath = token.getImagePath();

      assertNull(imagePath);
    }
  }
}
