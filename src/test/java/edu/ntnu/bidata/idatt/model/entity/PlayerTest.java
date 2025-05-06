package edu.ntnu.bidata.idatt.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.time.LocalDate;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Player Tests")
class PlayerTest {

  private static class DummyTokenView extends TokenView {
    private final String repr;

    DummyTokenView(String repr) {
      super(Token.token(Color.WHITE, "", null));
      this.repr = repr;
    }

    @Override
    public String toString() {
      return repr;
    }
  }

  @Nested
  @DisplayName("Positive Tests")
  class PositiveTests {

    @Test
    @DisplayName("getName returns the player's name")
    void getNameReturnsName() {
      Player player = new Player("Haaland", new DummyTokenView(""));
      assertEquals("Haaland", player.getName());
    }

    @Test
    @DisplayName("tokenView getter and setter work correctly")
    void tokenViewGetterAndSetter() {
      DummyTokenView initialToken = new DummyTokenView("A");
      Player player = new Player("Kroos", initialToken);
      DummyTokenView newToken = new DummyTokenView("B");
      player.setTokenView(newToken);
      assertSame(newToken, player.getToken());
    }

    @Test
    @DisplayName("default currentTileId is zero and setter updates value")
    void currentTileIdDefaultsAndUpdates() {
      Player player = new Player("Mbappe", new DummyTokenView(""));
      assertEquals(0, player.getCurrentTileId());
      player.setCurrentTileId(5);
      assertEquals(5, player.getCurrentTileId());
    }

    @Test
    @DisplayName("dateOfBirth getter and setter work correctly")
    void dateOfBirthGetterAndSetter() {
      Player player = new Player("Messi", new DummyTokenView(""));
      LocalDate dob = LocalDate.of(2000, 1, 1);
      player.setDateOfBirth(dob);
      assertEquals(dob, player.getDateOfBirth());
    }

    @Test
    @DisplayName("getAge returns 0 when dateOfBirth is today")
    void getAgeReturnsZeroForTodayBirth() {
      LocalDate today = LocalDate.now();
      Player player = new Player("Neymar", new DummyTokenView(""), today);
      assertEquals(0, player.getAge());
    }

    @Test
    @DisplayName("toString returns name and token string")
    void toStringReturnsNameAndTokenString() {
      Player player = new Player("Bjorn", new DummyTokenView("[TK]"));
      assertEquals("Bjorn [TK]", player.toString());
    }
  }

  @Nested
  @DisplayName("Negative Tests")
  class NegativeTests {

    @Test
    @DisplayName("getAge returns -1 when dateOfBirth is null")
    void getAgeReturnsMinusOneWhenNoDOB() {
      Player player = new Player("Hector", new DummyTokenView(""));
      assertEquals(-1, player.getAge());
    }

    @Test
    @DisplayName("default dateOfBirth is null for two-arg constructor")
    void defaultDateOfBirthIsNull() {
      Player player = new Player("Tri", new DummyTokenView(""));
      assertNull(player.getDateOfBirth());
    }

    @Test
    @DisplayName("setCurrentTileId allows negative values")
    void setCurrentTileIdAllowsNegative() {
      Player player = new Player("Kristian", new DummyTokenView(""));
      player.setCurrentTileId(-3);
      assertEquals(-3, player.getCurrentTileId());
    }
  }
}