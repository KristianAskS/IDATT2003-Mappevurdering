package edu.ntnu.bidata.idatt.model.entity;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Board Entity Tests")
class BoardTest {

  private static Tile tile1;
  private static Tile tile2;
  private Board board;

  @BeforeAll
  static void initAll() {
    tile1 = new Tile(1);
    tile2 = new Tile(2);
  }

  @BeforeEach
  void init() {
    board = new Board();
  }

  @Nested
  @DisplayName("Default State")
  class DefaultStateTests {

    @Test
    @DisplayName("new board has null name and description")
    void defaultConstructorInitializesFieldsToNull() {
      assertAll("default fields",
          () -> assertNull(board.getName(), "name should be null"),
          () -> assertNull(board.getDescription(), "description should be null"),
          () -> assertTrue(board.getTiles().isEmpty(), "tiles should be empty")
      );
    }
  }

  @Nested
  @DisplayName("Positive Tests")
  class PositiveTests {

    @Test
    @DisplayName("parameterized constructor sets name and description")
    void parameterizedConstructorSetsNameAndDescription() {
      String expectedName = "MyBoard";
      String expectedDesc = "FunGame";
      Board b = new Board(expectedName, expectedDesc);
      assertAll("constructor",
          () -> assertEquals(expectedName, b.getName(), "name must match"),
          () -> assertEquals(expectedDesc, b.getDescription(), "description must match")
      );
    }

    @Test
    @DisplayName("setName and getName work correctly, including null")
    void nameSetterAndGetter() {
      String newName = "BoardX";
      board.setName(newName);
      assertEquals(newName, board.getName());
      board.setName(null);
      assertNull(board.getName());
    }

    @Test
    @DisplayName("setDescription and getDescription work correctly, including null")
    void descriptionSetterAndGetter() {
      String newDesc = "A great board";
      board.setDescription(newDesc);
      assertEquals(newDesc, board.getDescription());
      board.setDescription(null);
      assertNull(board.getDescription());
    }

    @Test
    @DisplayName("addTile and getTile return the added tile")
    void addAndGetTile() {
      Tile t = new Tile(42);
      board.addTile(t);
      Tile result = board.getTile(42);
      assertSame(t, result);
    }

    @Test
    @DisplayName("getTiles returns all added tiles")
    void getTilesContainsAllTiles() {
      board.addTile(tile1);
      board.addTile(tile2);
      Map<Integer, Tile> tiles = board.getTiles();
      assertAll("tiles map",
          () -> assertEquals(2, tiles.size(), "should contain two entries"),
          () -> assertSame(tile1, tiles.get(1), "tile1 must be present"),
          () -> assertSame(tile2, tiles.get(2), "tile2 must be present")
      );
    }

    @Test
    @DisplayName("adding a tile with existing ID overwrites previous")
    void addTileWithExistingIdOverwrites() {
      Tile original = new Tile(7);
      Tile replacement = new Tile(7);
      board.addTile(original);
      board.addTile(replacement);
      assertSame(replacement, board.getTile(7));
    }
  }

  @Nested
  @DisplayName("Negative Tests")
  class NegativeTests {

    @Test
    @DisplayName("getTile returns null for unknown ID")
    void getTileUnknownReturnsNull() {
      assertNull(board.getTile(99), "unknown ID should return null");
    }

    @Test
    @DisplayName("getTiles map is unmodifiable")
    void tilesMapIsUnmodifiable() {
      board.addTile(tile1);
      Map<Integer, Tile> tiles = board.getTiles();
      assertThrows(UnsupportedOperationException.class,
          () -> tiles.put(3, new Tile(3)),
          "should not allow modification");
    }

    @Test
    @DisplayName("addTile(null) throws NullPointerException")
    void addNullTileThrowsNPE() {
      assertThrows(NullPointerException.class,
          () -> board.addTile(null),
          "adding null tile should NPE");
    }
  }
}
