package edu.ntnu.bidata.idatt.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.util.List;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Tile Tests")
class TileTest {

  private static Player createPlayer(String name) {
    return new Player(name, new DummyTokenView());
  }

  private static class DummyTokenView extends TokenView {

    DummyTokenView() {
      super(Token.token(Color.WHITE, "", null));
    }

    @Override
    public String toString() {
      return "Dummy";
    }
  }

  @Nested
  @DisplayName("Positive Tests")
  class PositiveTests {

    @Test
    @DisplayName("constructor initializes defaults")
    void constructorInitializesDefaults() {
      Tile tile = new Tile(5);
      assertEquals(5, tile.getTileId());
      assertNull(tile.getNextTile());
      assertEquals(0, tile.getNextTileId());
      assertFalse(tile.isSafe());
      assertFalse(tile.isHomeEntry());
      assertFalse(tile.isHomeColumn());
      assertNull(tile.getLandAction());
      assertTrue(tile.getPlayersOnTile().isEmpty());
    }

    @Test
    @DisplayName("tileId setter and getter work")
    void tileIdSetterGetter() {
      Tile tile = new Tile(0);
      tile.setTileId(7);
      assertEquals(7, tile.getTileId());
    }

    @Test
    @DisplayName("nextTile setter updates nextTile and nextTileId")
    void nextTileSetterUpdatesFields() {
      Tile tile1 = new Tile(1);
      Tile tile2 = new Tile(2);
      tile1.setNextTile(tile2);
      assertSame(tile2, tile1.getNextTile());
      assertEquals(2, tile1.getNextTileId());
    }

    @Test
    @DisplayName("nextTileId setter and getter work independently")
    void nextTileIdSetterGetter() {
      Tile tile = new Tile(3);
      tile.setNextTileId(9);
      assertEquals(9, tile.getNextTileId());
    }

    @Test
    @DisplayName("safe, homeEntry, homeColumn flags setters and getters work")
    void flagsSetterGetter() {
      Tile tile = new Tile(4);
      tile.setSafe(true);
      tile.setHomeEntry(true);
      tile.setHomeColumn(true);
      assertTrue(tile.isSafe());
      assertTrue(tile.isHomeEntry());
      assertTrue(tile.isHomeColumn());
    }

    @Test
    @DisplayName("addPlayer and removePlayer manage playersOnTile")
    void addAndRemovePlayersOnTile() {
      Tile tile = new Tile(6);
      Player p1 = createPlayer("P1");
      Player p2 = createPlayer("P2");
      tile.addPlayer(p1);
      tile.addPlayer(p2);
      List<Player> players = tile.getPlayersOnTile();
      assertEquals(2, players.size());
      assertTrue(players.contains(p1));
      assertTrue(players.contains(p2));
      tile.removePlayer(p1);
      assertEquals(1, tile.getPlayersOnTile().size());
      assertFalse(tile.getPlayersOnTile().contains(p1));
    }
  }

  @Nested
  @DisplayName("Negative Tests")
  class NegativeTests {

    @Test
    @DisplayName("getPlayersOnTile returns unmodifiable list")
    void playersOnTileListUnmodifiable() {
      Tile tile = new Tile(8);
      assertThrows(UnsupportedOperationException.class,
          () -> tile.getPlayersOnTile().add(createPlayer("X")));
    }

    @Test
    @DisplayName("removePlayer on non-existent player does nothing")
    void removeNonExistentPlayerDoesNothing() {
      Tile tile = new Tile(9);
      Player p = createPlayer("P");
      tile.removePlayer(p);
      assertTrue(tile.getPlayersOnTile().isEmpty());
    }
  }
}
