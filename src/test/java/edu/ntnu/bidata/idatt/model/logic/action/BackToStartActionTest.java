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

class BackToStartActionTest {

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
    Token token = new Token(Color.YELLOW, "circle");
    TokenView view = new TokenView(token);
    player = new Player("Tester", view);
    assertEquals(0, player.getCurrentTileId(), "Player should start on tile 0");
  }

  @Test
  void constructorStoresDescription() {
    BackToStartAction action = new BackToStartAction("Back to Start");
    assertEquals("Back to Start", action.description(),
        "Description should be as given to the constructor");
  }

  @Test
  void performResetsPlayerToZero() {
    // Move the player away from start
    player.setCurrentTileId(5);
    assertEquals(5, player.getCurrentTileId(), "Initial position before perform");

    BackToStartAction action = new BackToStartAction("Reset");
    action.perform(player);

    assertEquals(0, player.getCurrentTileId(), "perform() should reset the player to tile 0");
  }

  @Test
  void destinationIsAlwaysZero() {
    BackToStartAction action = new BackToStartAction("Start");
    assertEquals(0, action.getDestinationTileId(),
        "getDestinationTileId() should always return 0");
    action.setDestinationTileId(10);
    assertEquals(0, action.getDestinationTileId(),
        "setDestinationTileId() should be a no-op and not change the value");
  }

  @Test
  void setDescriptionIsNoOp() {
    BackToStartAction action = new BackToStartAction("Original");
    action.setDescription("New");
    assertEquals("Original", action.description(),
        "setDescription() should not change the description");
  }
}
