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

class SkipTurnActionTest {

  private Player player;

  @BeforeAll
  static void initJfx() {
    try {
      Platform.startup(() -> {
      });
    } catch (IllegalStateException e) {
      // JavaFX already initialized
    }
  }

  @BeforeEach
  void setUp() {
    Token token = new Token(Color.GREEN, "circle");
    TokenView tokenView = new TokenView(token);
    player = new Player("Tester", tokenView);
    assertEquals(0, player.getCurrentTileId(), "Player should start on tile 0");
  }

  @Test
  void constructorPositiveTurns() {
    SkipTurnAction action = new SkipTurnAction(3, "Skip 3 turns");
    assertEquals(3, action.turnsToSkip(), "turnsToSkip should be as specified when > 1");
    assertEquals("Skip 3 turns", action.description(), "Description should be as specified");
  }

  @Test
  void constructorZeroTurnsDefaultsToOne() {
    SkipTurnAction action = new SkipTurnAction(0, "None");
    assertEquals(1, action.turnsToSkip(), "turnsToSkip should be at least 1 even if 0 is given");
    assertEquals("None", action.description(), "Description should be as specified");
  }

  @Test
  void constructorNegativeTurnsDefaultsToOne() {
    SkipTurnAction action = new SkipTurnAction(-5, "Negative");
    assertEquals(1, action.turnsToSkip(),
        "turnsToSkip should be at least 1 even if a negative number is given");
    assertEquals("Negative", action.description(), "Description should be as specified");
  }

  @Test
  void performDoesNotMovePlayer() {
    player.setCurrentTileId(7);
    SkipTurnAction action = new SkipTurnAction(2, "Skip");
    action.perform(player);
    assertEquals(7, player.getCurrentTileId(), "perform() should not change the player's position");
  }

  @Test
  void destinationTileIdIsAlwaysZero() {
    SkipTurnAction action = new SkipTurnAction(4, "Skip");
    assertEquals(0, action.getDestinationTileId(), "getDestinationTileId should always return 0");
    action.setDestinationTileId(10);
    assertEquals(0, action.getDestinationTileId(),
        "setDestinationTileId is a no-op and should ignore input");
  }

  @Test
  void setDescriptionIsNoOp() {
    SkipTurnAction action = new SkipTurnAction(1, "Original");
    action.setDescription("New");
    assertEquals("Original", action.description(),
        "setDescription should not change the description");
  }
}
