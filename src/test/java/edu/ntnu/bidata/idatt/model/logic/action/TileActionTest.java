package edu.ntnu.bidata.idatt.model.logic.action;

import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.view.components.TileView;
import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.util.Objects;
import org.junit.jupiter.api.*;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for TileAction implementations using Arrange-Act-Assert pattern.
 */
class TileActionTest {

  private static Player sharedPlayer;
  private static TokenView dummyToken;
  private TileAction action;

  @BeforeAll
  static void setupAll() {
    // Runs once before all tests: prepare a dummy token and a player with required constructor args
    dummyToken = mock(TokenView.class);
    sharedPlayer = new Player("TestPlayer", dummyToken, LocalDate.of(2000, 1, 1));
  }

  @BeforeEach
  void setup() {
    // Arrange: create fresh action and reset player before each test
    action = new SimpleTileAction();
    sharedPlayer.setCurrentTileId(0);
  }

  /**
   * Positive test: setting and getting destination tile id.
   */
  @Test
  void testSetAndGetDestinationTileId() {
    // Arrange
    int expected = 5;

    // Act
    action.setDestinationTileId(expected);
    int actual = action.getDestinationTileId();

    // Assert
    assertEquals(expected, actual, "Destination tile ID should match the set value");
  }

  /**
   * Negative test: setting a negative tile id should throw IllegalArgumentException.
   */
  @Test
  void testSetNegativeDestinationTileIdThrows() {
    // Arrange
    int invalidId = -1;

    // Act & Assert
    assertThrows(IllegalArgumentException.class,
        () -> action.setDestinationTileId(invalidId),
        "Setting a negative destination tile ID should throw IllegalArgumentException");
  }

  /**
   * Positive test: setting and getting description.
   */
  @Test
  void testSetAndGetDescription() {
    // Arrange
    String desc = "Move to tile";

    // Act
    action.setDescription(desc);
    String actual = action.description();

    // Assert
    assertEquals(desc, actual, "Description should match the set value");
  }

  /**
   * Negative test: setting null description should throw NullPointerException.
   */
  @Test
  void testSetNullDescriptionThrows() {
    // Act & Assert
    assertThrows(NullPointerException.class,
        () -> action.setDescription(null),
        "Setting a null description should throw NullPointerException");
  }

  /**
   * Positive test: perform action moves player to destination tile.
   */
  @Test
  void testPerformMovesPlayer() {
    // Arrange
    int target = 3;
    action.setDestinationTileId(target);

    // Act
    action.perform(sharedPlayer);

    // Assert
    assertEquals(target, sharedPlayer.getCurrentTileId(),
        "Player should be moved to the destination tile after perform");
  }

  /**
   * Simple implementation of TileAction for testing purposes.
   */
  private static class SimpleTileAction implements TileAction {
    private int destinationTileId;
    private String desc = "";

    @Override
    public int getDestinationTileId() {
      return destinationTileId;
    }

    @Override
    public void setDestinationTileId(int destinationTileId) {
      if (destinationTileId < 0) {
        throw new IllegalArgumentException("Tile ID cannot be negative");
      }
      this.destinationTileId = destinationTileId;
    }

    @Override
    public String description() {
      return desc;
    }

    @Override
    public void setDescription(String description) {
      this.desc = Objects.requireNonNull(description, "Description cannot be null");
    }

    @Override
    public void perform(Player player) {
      player.setCurrentTileId(destinationTileId);
    }
  }
}
