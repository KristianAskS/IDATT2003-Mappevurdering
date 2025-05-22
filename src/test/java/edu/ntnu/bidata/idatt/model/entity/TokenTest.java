package edu.ntnu.bidata.idatt.model.entity;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Token Entity Tests")
class TokenTest {
  private Color red;
  private Color blue;
  private String shapeCircle;
  private String imagePath;

  @BeforeAll
  void initAll() {
    red = Color.RED;
    blue = Color.BLUE;
    shapeCircle = "circle";
    imagePath = "/images/token.png";
  }

  @Nested
  @DisplayName("Positive Tests")
  class PositiveTests {
    private Token tokenNoImage;
    private Token tokenWithImage;

    @BeforeEach
    void setUp() {
      tokenNoImage = new Token(red, shapeCircle);
      tokenWithImage = new Token(blue, shapeCircle, imagePath);
    }

    @Test
    @DisplayName("getColor returns the initialized color")
    void getColorReturnsInitializedColor() {
      Color actual = tokenNoImage.getColor();
      assertEquals(red, actual, "getColor() should return the color passed to constructor");
    }

    @Test
    @DisplayName("getShape returns the initialized shape")
    void getShapeReturnsInitializedShape() {
      String actual = tokenNoImage.getShape();
      assertEquals(shapeCircle, actual, "getShape() should return the shape passed to constructor");
    }

    @Test
    @DisplayName("getImagePath returns null when not provided")
    void getImagePathReturnsNullWhenNotProvided() {
      String actual = tokenNoImage.getImagePath();
      assertNull(actual, "getImagePath() should be null when constructed without imagePath");
    }

    @Test
    @DisplayName("getImagePath returns the provided imagePath")
    void getImagePathReturnsProvidedImagePath() {
      String actual = tokenWithImage.getImagePath();
      assertEquals(imagePath, actual,
          "getImagePath() should return the imagePath passed to constructor");
    }

    @Test
    @DisplayName("static factory token(color, shape) sets imagePath to null and correct fields")
    void staticFactoryWithoutImagePathSetsNull() {
      Token factoryToken = Token.token(blue, "hexagon");
      assertAll("factory without image",
          () -> assertEquals(blue, factoryToken.getColor(), "color"),
          () -> assertEquals("hexagon", factoryToken.getShape(), "shape"),
          () -> assertNull(factoryToken.getImagePath(), "imagePath")
      );
    }

    @Test
    @DisplayName("static factory token(color, shape, imagePath) matches full constructor")
    void staticFactoryWithImagePathMatchesConstructor() {
      Token factoryToken = Token.token(red, "triangle", imagePath);
      assertAll("factory with image",
          () -> assertEquals(red, factoryToken.getColor(), "color"),
          () -> assertEquals("triangle", factoryToken.getShape(), "shape"),
          () -> assertEquals(imagePath, factoryToken.getImagePath(), "imagePath")
      );
    }
  }

  @Nested
  @DisplayName("Negative & Edge Cases")
  class NegativeTests {

    @Test
    @DisplayName("constructor converts null shape to empty string")
    void constructorNullShapeConvertsToEmptyString() {
      Token token = new Token(Color.GREEN, null, "/unused");
      assertEquals("", token.getShape(), "null shape should become empty string");
      assertEquals("/unused", token.getImagePath(), "imagePath preserved");
    }

    @Test
    @DisplayName("static factory with null shape yields empty string")
    void staticFactoryNullShapeYieldsEmptyString() {
      Token factoryToken = Token.token(Color.BLACK, null);
      assertEquals("", factoryToken.getShape(), "null shape in factory should become empty string");
      assertNull(factoryToken.getImagePath(), "imagePath remains null");
    }

    @Test
    @DisplayName("imagePath remains null when using two-arg constructor even if provided null explicitly")
    void imagePathRemainsNullWithTwoArgConstructor() {
      Token token = new Token(Color.CYAN, "square");
      assertNull(token.getImagePath(), "two-arg constructor always yields null imagePath");
    }
  }
}
