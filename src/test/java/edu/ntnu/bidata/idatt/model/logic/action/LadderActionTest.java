package edu.ntnu.bidata.idatt.model.logic.action;

import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Token;
import edu.ntnu.bidata.idatt.view.components.TokenView;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LadderActionTest {

  private Player player;

  @BeforeAll
  static void initJfx() {
    try {
      Platform.startup(() -> {
      });
    } catch (IllegalStateException e) {
    }
  }

  @BeforeEach
  void setUp() {
    Token token = new Token(Color.BLUE, "circle");
    TokenView tokenView = new TokenView(token);
    player = new Player("TestPlayer", tokenView);
    assertEquals(0, player.getCurrentTileId(), "Player should start on tile 0");
  }

  @Test
  void constructorStoresValues() {
    LadderAction action = new LadderAction(12, "Up the ladder");

    assertEquals(12, action.getDestinationTileId(), "Destination should be as specified");
    assertEquals("Up the ladder", action.description(), "Description should be as specified");
  }

  @Test
  void settersUpdateValues() {
    LadderAction action = new LadderAction(4, "First");

    action.setDestinationTileId(9);
    action.setDescription("Second");

    assertEquals(9, action.getDestinationTileId(), "Setter should change the destination");
    assertEquals("Second", action.description(), "Setter should change the description");
  }

  @Test
  void performMovesPlayer() {
    LadderAction action = new LadderAction(8, "Climb");

    action.perform(player);

    assertEquals(8, player.getCurrentTileId(),
        "perform() should move the player to the destination");
  }

  @Test
  void negativeDestinationAllowed() {
    LadderAction action = new LadderAction(-2, "Backward");

    action.perform(player);

    assertEquals(-2, player.getCurrentTileId(), "perform() should accept negative IDs");
  }

  @Test
  void emptyDescriptionIsSupported() {
    LadderAction action = new LadderAction(3, "");

    assertEquals("", action.description(), "Empty description should be allowed");
    action.setDescription("New");
    assertEquals("New", action.description(), "Setter should work afterwards");
  }
}
