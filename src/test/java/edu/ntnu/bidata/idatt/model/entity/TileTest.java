package edu.ntnu.bidata.idatt.model.entity;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.bidata.idatt.model.logic.action.TileAction;
import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.time.LocalDate;
import java.util.List;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Tile Entity Tests")
class TileTest {
  private Tile tile;
  private Tile otherTile;
  private Player p1;
  private Player p2;
  private TileAction dummyAction;

  @BeforeAll
  void initAll() {
    dummyAction = new DummyAction(99, "dummy");
  }

  @BeforeEach
  void setUp() {
    tile = new Tile(10);
    otherTile = new Tile(20);
    p1 = new Player("Tri", new DummyTokenView(), LocalDate.of(1990, 1, 1));
    p2 = new Player("Kristian", new DummyTokenView(), LocalDate.of(1995, 5, 5));
  }

  private static class DummyAction implements TileAction {
    private int dest;
    private String desc;

    DummyAction(int dest, String desc) {
      this.dest = dest;
      this.desc = desc;
    }

    @Override
    public int getDestinationTileId() {
      return dest;
    }

    @Override
    public void setDestinationTileId(int d) {
      dest = d;
    }

    @Override
    public String description() {
      return desc;
    }

    @Override
    public void setDescription(String d) {
      desc = d;
    }

    @Override
    public void perform(Player p) {

    }
  }

  private static class DummyTokenView extends TokenView {
    DummyTokenView() {
      super(Token.token(Color.BLACK, "circle", null));
    }

    @Override
    public String toString() {
      return "[TK]";
    }
  }

  @Nested
  @DisplayName("Positive Tests")
  class PositiveTests {

    @Test
    @DisplayName("Constructor sets tileId and defaults")
    void constructorAndDefaults() {
      assertAll("defaults",
          () -> assertEquals(10, tile.getTileId(), "tileId"),
          () -> assertNull(tile.getNextTile(), "nextTile should be null"),
          () -> assertEquals(0, tile.getNextTileId(), "nextTileId default"),
          () -> assertNull(tile.getLandAction(), "landAction default"),
          () -> assertTrue(tile.getPlayersOnTile().isEmpty(), "no players by default")
      );
    }

    @Test
    @DisplayName("setTileId and getTileId")
    void tileIdSetterGetter() {
      int newId = 42;
      tile.setTileId(newId);
      assertEquals(newId, tile.getTileId());
    }

    @Test
    @DisplayName("setNextTile links tile and id")
    void linkNextTile() {
      tile.setNextTile(otherTile);
      assertAll("linking",
          () -> assertSame(otherTile, tile.getNextTile(), "nextTile object"),
          () -> assertEquals(20, tile.getNextTileId(), "nextTileId")
      );
    }

    @Test
    @DisplayName("setNextTile(null) clears link but retains id")
    void clearNextTileKeepsId() {
      tile.setNextTile(otherTile);
      assertEquals(20, tile.getNextTileId());
      tile.setNextTile(null);
      assertAll("clearing",
          () -> assertNull(tile.getNextTile(), "nextTile cleared"),
          () -> assertEquals(20, tile.getNextTileId(), "nextTileId unchanged")
      );
    }

    @Test
    @DisplayName("setNextTileId works independently")
    void manualNextTileId() {
      tile.setNextTileId(77);
      assertAll("manual id",
          () -> assertEquals(77, tile.getNextTileId()),
          () -> assertNull(tile.getNextTile(), "nextTile still null")
      );
    }

    @Test
    @DisplayName("setLandAction and getLandAction")
    void landActionSetterGetter() {
      tile.setLandAction(dummyAction);
      assertSame(dummyAction, tile.getLandAction());
    }

    @Test
    @DisplayName("setLandAction(null) resets action")
    void clearLandAction() {
      tile.setLandAction(dummyAction);
      tile.setLandAction(null);
      assertNull(tile.getLandAction());
    }

    @Test
    @DisplayName("addPlayer and getPlayersOnTile")
    void addAndListPlayers() {
      tile.addPlayer(p1);
      tile.addPlayer(p2);
      List<Player> list = tile.getPlayersOnTile();
      // Assert
      assertAll("players list",
          () -> assertEquals(2, list.size(), "two players added"),
          () -> assertTrue(list.contains(p1), "contains p1"),
          () -> assertTrue(list.contains(p2), "contains p2")
      );
    }

    @Test
    @DisplayName("removePlayer removes existing player")
    void removeExistingPlayer() {
      tile.addPlayer(p1);
      tile.removePlayer(p1);
      assertTrue(tile.getPlayersOnTile().isEmpty(), "player removed");
    }

    @Test
    @DisplayName("addPlayer(null) allowed and appears in list")
    void addNullPlayer() {
      tile.addPlayer(null);
      assertNull(tile.getPlayersOnTile().get(0), "null entry present");
    }
  }

  @Nested
  @DisplayName("Negative & Edge Tests")
  class NegativeTests {

    @Test
    @DisplayName("getPlayersOnTile returns unmodifiable list")
    void playersListUnmodifiable() {
      tile.addPlayer(p1);
      List<Player> list = tile.getPlayersOnTile();
      assertThrows(UnsupportedOperationException.class,
          () -> list.add(p2),
          "should not allow modification"
      );
    }

    @Test
    @DisplayName("removePlayer does nothing if player absent")
    void removeAbsentPlayerNoError() {
      assertDoesNotThrow(() -> tile.removePlayer(p1), "removing absent player");
      assertTrue(tile.getPlayersOnTile().isEmpty(), "still empty");
    }

    @Test
    @DisplayName("new Tile with negative ID retains negative ID")
    void negativeTileIdAllowed() {
      Tile t = new Tile(-5);
      assertEquals(-5, t.getTileId(), "negative tileId preserved");
    }
  }
}
