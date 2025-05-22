package edu.ntnu.bidata.idatt.model.entity;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.time.LocalDate;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Player Entity Tests")
class PlayerTest {

  private DummyTokenView tokenA;
  private DummyTokenView tokenB;
  private LocalDate birthDate;
  private Player player;

  @BeforeAll
  void beforeAll() {
    tokenA = new DummyTokenView("[A]");
    tokenB = new DummyTokenView("[B]");
    birthDate = LocalDate.of(2000, 1, 1);
  }

  @BeforeEach
  void setUp() {
    player = new Player("Tri", tokenA, birthDate);
  }

  private static class DummyTokenView extends TokenView {
    private final String repr;

    DummyTokenView(String repr) {
      super(Token.token(Color.BLUE, "circle", null));
      this.repr = repr;
    }

    @Override
    public String toString() {
      return repr;
    }
  }

  @Nested
  @DisplayName("Constructor & Defaults")
  class ConstructorTests {

    @Test
    @DisplayName("Two-arg constructor sets dateOfBirth to null")
    void twoArgConstructorDefaultsDobToNull() {
      Player p = new Player("Kristian", tokenA);
      assertAll("default fields",
          () -> assertEquals("Kristian", p.getName()),
          () -> assertSame(tokenA, p.getToken()),
          () -> assertNull(p.getDateOfBirth(), "dateOfBirth should be null"),
          () -> assertEquals(0, p.getCurrentTileId(), "default tileId should be 0"),
          () -> assertEquals(0, p.getAmountOfSteps(), "default steps should be 0")
      );
    }

    @Test
    @DisplayName("Three-arg constructor initializes fields correctly")
    void threeArgConstructorSetsAllFields() {
      Player p = new Player("Hector", tokenB, birthDate);
      assertAll("constructor",
          () -> assertEquals("Hector", p.getName()),
          () -> assertSame(tokenB, p.getToken()),
          () -> assertEquals(birthDate, p.getDateOfBirth()),
          () -> assertEquals(birthDate.until(LocalDate.now()).getYears(), p.getAge()),
          () -> assertEquals(tokenB.getTokenColor(), p.getColor()),
          () -> assertEquals(0, p.getAmountOfSteps())
      );
    }
  }

  @Nested
  @DisplayName("Getter & Setter Behavior")
  class AccessorTests {

    @Test
    @DisplayName("setTokenView updates token and color")
    void setTokenViewUpdatesTokenAndColor() {
      assertSame(tokenA, player.getToken());
      player.setTokenView(tokenB);
      assertAll("token update",
          () -> assertSame(tokenB, player.getToken()),
          () -> assertEquals(tokenB.getTokenColor(), player.getColor())
      );
    }

    @Test
    @DisplayName("setColor overrides display color without touching token")
    void setColorOverridesOnlyColor() {
      Color newColor = Color.RED;
      player.setColor(newColor);
      assertEquals(newColor, player.getColor(), "display color should be overridden");
      assertSame(tokenA, player.getToken(), "token reference should remain unchanged");
    }

    @Test
    @DisplayName("currentTileId getter and setter work, including negatives")
    void currentTileIdGetterSetter() {
      assertEquals(0, player.getCurrentTileId());
      player.setCurrentTileId(7);
      assertEquals(7, player.getCurrentTileId());
      player.setCurrentTileId(-3);
      assertEquals(-3, player.getCurrentTileId(), "negative tile IDs allowed");
    }

    @Test
    @DisplayName("dateOfBirth getter and setter work correctly")
    void dateOfBirthGetterSetter() {
      player.setDateOfBirth(LocalDate.of(1995, 5, 5));
      assertEquals(LocalDate.of(1995, 5, 5), player.getDateOfBirth());
    }

    @Test
    @DisplayName("amountOfSteps getter and setter work")
    void amountOfStepsGetterSetter() {
      player.setAmountOfSteps(15);
      assertEquals(15, player.getAmountOfSteps());
    }
  }

  @Nested
  @DisplayName("Age Calculation")
  class AgeTests {

    @Test
    @DisplayName("getAge returns correct full years")
    void getAgeReturnsFullYears() {
      LocalDate bd = LocalDate.now().minusYears(30).minusDays(1);
      player.setDateOfBirth(bd);
      int age = player.getAge();
      assertEquals(30, age, "age should be 30 full years");
    }

    @Test
    @DisplayName("getAge returns -1 when dateOfBirth is null")
    void getAgeReturnsMinusOneIfNoDob() {
      Player p = new Player("Bjorn", tokenA);
      assertEquals(-1, p.getAge(), "no birthdate means age == -1");
    }

    @Test
    @DisplayName("getAge returns 0 if born today")
    void getAgeZeroIfBornToday() {
      // Arrange
      Player p = new Player("Ingve", tokenA, LocalDate.now());
      // Act & Assert
      assertEquals(0, p.getAge());
    }
  }

  @Nested
  @DisplayName("toString Representation")
  class ToStringTests {

    @Test
    @DisplayName("toString combines name and token.toString()")
    void toStringCombinesNameAndToken() {
      player.setTokenView(new DummyTokenView("XX"));
      String repr = player.toString();
      assertEquals("Tri XX", repr);
    }
  }
}
