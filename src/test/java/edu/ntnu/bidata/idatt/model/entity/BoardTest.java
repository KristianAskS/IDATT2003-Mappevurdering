package edu.ntnu.bidata.idatt.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Board Entity Tests")
class BoardTest {

  @Nested
  @DisplayName("Positive Tests")
  class PositiveTests {

    @Test
    @DisplayName("Parameterized constructor sets name and description")
    void parameterizedConstructorSetsNameAndDescription() {
      Board board = new Board("MyBoard", "Boardgame");

      assertEquals("MyBoard", board.getName());
      assertEquals("Boardgame", board.getDescription());
    }

    @Test
    @DisplayName("setName and getName work correctly")
    void nameSetterAndGetter() {
      Board board = new Board();
      board.setName("NewName");

      assertEquals("NewName", board.getName());
    }

    @Test
    @DisplayName("setDescription and getDescription work correctly")
    void descriptionSetterAndGetter() {
      Board board = new Board();
      board.setDescription("NewDesc");

      assertEquals("NewDesc", board.getDescription());
    }

    @Test
    @DisplayName("addTile and getTile return the added tile")
    void addAndGetTile() {
      Board board = new Board();
      Tile tile = new Tile(42);
      board.addTile(tile);

      assertSame(tile, board.getTile(42));
    }

    @Test
    @DisplayName("getTiles returns all added tiles")
    void getTilesContainsAllTiles() {
      Board board = new Board();
      Tile t1 = new Tile(1);
      Tile t2 = new Tile(2);
      board.addTile(t1);
      board.addTile(t2);

      Map<Integer, Tile> tiles = board.getTiles();
      assertEquals(2, tiles.size());
      assertSame(t1, tiles.get(1));
      assertSame(t2, tiles.get(2));
    }
  }

  @Nested
  @DisplayName("Negative Tests")
  class NegativeTests {

    @Test
    @DisplayName("getTile returns null for unknown tileId")
    void getUnknownTileReturnsNull() {
      Board board = new Board();

      assertNull(board.getTile(99));
    }

    @Test
    @DisplayName("getTiles map is unmodifiable")
    void tilesMapIsUnmodifiable() {
      Board board = new Board();
      board.addTile(new Tile(5));

      Map<Integer, Tile> tiles = board.getTiles();
      assertThrows(UnsupportedOperationException.class, () -> tiles.put(6, new Tile(6)));
    }

    @Test
    @DisplayName("adding tile with existing id overwrites previous")
    void addTileWithExistingIdOverwrites() {
      Board board = new Board();
      Tile original = new Tile(7);
      Tile replacement = new Tile(7);
      board.addTile(original);
      board.addTile(replacement);

      assertSame(replacement, board.getTile(7));
    }

    @Test
    @DisplayName("addTile with null throws NullPointerException")
    void addNullTileThrows() {
      Board board = new Board();

      assertThrows(NullPointerException.class, () -> board.addTile(null));
    }
  }
}
