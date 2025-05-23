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

class SnakeActionTest {

  private Player player;

  @BeforeAll
  static void initJfx() {
    try {
      Platform.startup(() -> {
      });
    } catch (IllegalStateException ignored) {
    }
  }

  @BeforeEach
  void setUp() {
    TokenView tokenView = new TokenView(new Token(Color.RED, "circle"));
    player = new Player("Test", tokenView);
    assertEquals(0, player.getCurrentTileId(), "Player should start at tile 0");
  }

  @Test
  void constructorStoresValues() {
    SnakeAction action = new SnakeAction(5, "Slide down");

    assertEquals(5, action.getDestinationTileId());
    assertEquals("Slide down", action.description());
  }

  @Test
  void settersUpdateValues() {
    SnakeAction action = new SnakeAction(2, "Start Desc");

    action.setDestinationTileId(8);
    action.setDescription("New Desc");

    assertEquals(8, action.getDestinationTileId());
    assertEquals("New Desc", action.description());
  }

  @Test
  void performMovesPlayer() {
    SnakeAction action = new SnakeAction(7, "Drop");

    action.perform(player);

    assertEquals(7, player.getCurrentTileId(), "perform should set player's tile to destination");
  }

  @Test
  void negativeDestinationIsAllowed() {
    SnakeAction action = new SnakeAction(-3, "Backwards");

    action.perform(player);

    assertEquals(-3, player.getCurrentTileId(), "perform should accept negative destination IDs");
  }

  @Test
  void descriptionCanBeEmpty() {
    SnakeAction action = new SnakeAction(1, "");

    assertEquals("", action.description(), "Empty description should be supported");
    action.setDescription("Desc");
    assertEquals("Desc", action.description());
  }
}