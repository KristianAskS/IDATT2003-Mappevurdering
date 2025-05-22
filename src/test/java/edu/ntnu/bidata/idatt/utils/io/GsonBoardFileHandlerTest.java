package edu.ntnu.bidata.idatt.utils.io;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonSyntaxException;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.model.logic.action.LadderAction;
import edu.ntnu.bidata.idatt.utils.exceptions.BoardParsingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("GsonBoardFileHandler Tests")
class GsonBoardFileHandlerTest {
  @TempDir Path tempDir;
  private GsonBoardFileHandler handler;
  private static Board simpleBoard;
  private static Board linkedBoard;

  @BeforeAll
  static void createSampleBoards() {
    simpleBoard = new Board("SIMPLE", "2 tiles");
    simpleBoard.addTile(new Tile(1));
    simpleBoard.addTile(new Tile(2));

    linkedBoard = new Board("LINKED", "2 tiles with ladder");
    Tile t1 = new Tile(1);
    Tile t2 = new Tile(2);
    t1.setNextTile(t2);
    t1.setLandAction(new LadderAction(2, "ladder"));
    linkedBoard.addTile(t1);
    linkedBoard.addTile(t2);
  }

  @BeforeEach
  void setUp() {
    handler = new GsonBoardFileHandler();
  }

  @Nested
  @DisplayName("Positive Tests")
  class PositiveTests {

    @Test
    @DisplayName("writeToFile and readFromFile round-trip for array")
    void writeReadArray(@TempDir Path dir) throws IOException {
      Path file = dir.resolve("boards.json");
      List<Board> originals = List.of(simpleBoard, linkedBoard);

      handler.writeToFile(originals, file.toString());
      List<Board> readBack = handler.readFromFile(file.toString());

      assertEquals(2, readBack.size(), "Should read two boards");
      assertEquals("SIMPLE", readBack.get(0).getName());
      assertEquals("LINKED", readBack.get(1).getName());
      Board b = readBack.get(1);
      assertEquals(2, b.getTiles().size());
      Tile r1 = b.getTile(1);
      assertNotNull(r1.getLandAction(), "Tile 1 should have ladder action");
      assertEquals(2, r1.getLandAction().getDestinationTileId());
    }

    @Test
    @DisplayName("readFromFile handles single-object JSON")
    void readSingleObject(@TempDir Path dir) throws IOException {
      Path file = dir.resolve("single.json");
      String json = handler.serializeBoardToJson(simpleBoard).toString();
      Files.writeString(file, json);

      List<Board> result = handler.readFromFile(file.toString());

      assertEquals(1, result.size(), "Should read one board");
      assertEquals("SIMPLE", result.get(0).getName());
    }

    @Test
    @DisplayName("serializeBoardToJson returns null for null input")
    void serializeNullReturnsNull() {
      assertNull(handler.serializeBoardToJson(null));
    }
  }

  @Nested
  @DisplayName("Negative & Edge-case Tests")
  class NegativeTests {

    @Test
    @DisplayName("writeToFile rejects empty boards list")
    void writeEmptyListThrows() {
      assertThrows(IllegalArgumentException.class,
          () -> handler.writeToFile(List.of(), tempDir.resolve("x.json").toString()));
    }

    @Test
    @DisplayName("writeToFile rejects null path")
    void writeNullPathThrows() {
      assertThrows(IllegalArgumentException.class,
          () -> handler.writeToFile(List.of(simpleBoard), null));
    }

    @Test
    @DisplayName("readFromFile rejects null path")
    void readNullPathThrows() {
      assertThrows(NullPointerException.class,
          () -> handler.readFromFile(null));
    }

    @Test
    @DisplayName("readFromFile wraps invalid JSON in BoardParsingException")
    void readInvalidJsonThrowsBoardParsingException(@TempDir Path dir) throws IOException {
      Path file = dir.resolve("bad.json");
      Files.writeString(file, "{ not valid json");

      BoardParsingException ex = assertThrows(BoardParsingException.class,
          () -> handler.readFromFile(file.toString()));
      assertInstanceOf(JsonSyntaxException.class, ex.getCause());
    }

    @Test
    @DisplayName("deserializeJsonToBoard rejects unknown action type")
    void deserializeUnknownActionThrows() {
      String json = """
        {"name":"X","description":"desc","tiles":[
          {"tileId":1,"landAction":"com.foo.Unknown","destination tile":5}
        ]}
        """;
      assertThrows(IllegalArgumentException.class,
          () -> handler.deserializeJsonToBoard(json));
    }
  }
}
