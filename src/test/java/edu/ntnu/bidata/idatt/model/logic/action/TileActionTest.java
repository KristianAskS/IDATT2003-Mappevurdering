package edu.ntnu.bidata.idatt.model.logic.action;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Token;
import edu.ntnu.bidata.idatt.view.components.TokenView;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TileActionTest {

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
    Token token = new Token(Color.BLACK, "circle");
    TokenView tokenView = new TokenView(token);

    player = new Player("TestPlayer", tokenView);
    assertEquals(0, player.getCurrentTileId(), "Player should start on tile 0");
  }

  @Nested
  class PositiveTests {

    @Test
    void ladderActionMovesPlayerToDestination() {
      TileAction action = new LadderAction(10, "Climb the ladder");

      action.perform(player);

      assertEquals(10, player.getCurrentTileId(), "perform() should move player to destination");
    }

    @Test
    void snakeActionMovesPlayerToDestination() {
      TileAction action = new SnakeAction(3, "Slide down the snake");

      action.perform(player);

      assertEquals(3, player.getCurrentTileId(), "perform() should move player to destination");
    }

    @Test
    void backToStartActionResetsPlayerToStart() {
      player.setCurrentTileId(7);
      TileAction action = new BackToStartAction("Back to start");

      action.perform(player);

      assertEquals(0, player.getCurrentTileId(), "perform() should reset player to start");
    }

    @Test
    void skipTurnActionDoesNotMovePlayer() {
      SkipTurnAction skipAction = new SkipTurnAction(2, "Skip two turns");

      assertDoesNotThrow(() -> skipAction.perform(player),
          "perform() should not throw on skip turn");
      assertEquals(0, player.getCurrentTileId(), "SkipTurnAction should not alter tile id");
    }

    @Test
    void descriptionSettersAndGettersWork() {
      TileAction action = new LadderAction(5, "Original description");

      action.setDescription("New description");

      assertEquals("New description", action.description(),
          "setDescription() should update the description");
    }
  }

  @Nested
  class NegativeAndEdgeCaseTests {

    @Test
    void snakeActionWithNegativeDestination() {
      TileAction action = new SnakeAction(3, "Slide down the snake");

      action.setDestinationTileId(-5);
      action.perform(player);

      assertEquals(-5, player.getCurrentTileId(),
          "setDestinationTileId() should accept negative values");
    }

    @Test
    void skipTurnActionWithZeroTurnsDefaultsToOne() {
      SkipTurnAction skipAction = new SkipTurnAction(0, "No turns");

      int turns = skipAction.turnsToSkip();

      assertEquals(1, turns, "SkipTurnAction should default to 1 if given zero or negative");
    }

    @Test
    void ladderActionInvalidDestinationDoesNotThrow() {
      TileAction action = new LadderAction(10, "Climb the ladder");

      assertDoesNotThrow(() -> action.setDestinationTileId(Integer.MIN_VALUE),
          "setDestinationTileId() should not throw on extreme values");
      assertEquals(Integer.MIN_VALUE, action.getDestinationTileId(),
          "getDestinationTileId() should return the set value");
    }
  }
}
