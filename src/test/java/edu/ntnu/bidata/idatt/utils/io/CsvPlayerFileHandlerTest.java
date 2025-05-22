package edu.ntnu.bidata.idatt.utils.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Token;
import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("CsvPlayerFileHandler Tests")
class CsvPlayerFileHandlerTest {

  private CsvPlayerFileHandler handler;
  private Player alice;
  private Player bob;

  @BeforeEach
  void setUp() {
    handler = new CsvPlayerFileHandler();
    TokenView tA = new TokenView(Token.token(Color.RED, "circle"));
    TokenView tB = new TokenView(Token.token(Color.BLUE, "square"));
    alice = new Player("Alice", tA, LocalDate.of(1990, 1, 1));
    bob = new Player("Bob", tB, LocalDate.of(2000, 12, 31));
  }

  @Nested
  @DisplayName("Positive behavior")
  class PositiveTests {

    @Test
    @DisplayName("appendToFile adds lines without overwriting")
    void appendAddsWithoutOverwriting(@TempDir Path tempDir) throws IOException {
      Path file = tempDir.resolve("players.csv");
      handler.writeToFile(List.of(alice), file.toString());

      handler.appendToFile(List.of(bob), file.toString());
      List<String> lines = Files.readAllLines(file);

      assertEquals(2, lines.size(), "Two lines after append");
      assertTrue(lines.get(0).startsWith("Alice,"), "First line is Alice");
      assertTrue(lines.get(1).startsWith("Bob,"), "Second line is Bob");
    }

    @Test
    @DisplayName("readFromFile skips malformed (too short) lines")
    void readSkipsMalformedLines(@TempDir Path tempDir) throws IOException {
      Path file = tempDir.resolve("mixed.csv");
      Files.writeString(file,
          "Carol,#FF0000FF,triangle,1985-05-05\n" +
              "bad,line\n" +
              "Dave,#00FF00FF,square,1995-10-10\n"
      );

      List<Player> players = handler.readFromFile(file.toString());

      assertEquals(2, players.size(), "Should skip the malformed line");
      assertEquals("Carol", players.get(0).getName());
      assertEquals("Dave", players.get(1).getName());
    }

    @Test
    @DisplayName("invalid date yields null dateOfBirth")
    void invalidDateLeadsToNullDob(@TempDir Path tempDir) throws IOException {
      Path file = tempDir.resolve("invalid.csv");
      Files.writeString(file, "Eve,#0000FFFF,circle,not-a-date\n");

      List<Player> players = handler.readFromFile(file.toString());

      assertEquals(1, players.size());
      assertEquals("Eve", players.get(0).getName());
      assertNull(players.get(0).getDateOfBirth(), "Invalid date should produce null dob");
    }
  }

  @Nested
  @DisplayName("Negative & edge-case behavior")
  class NegativeTests {

    @Test
    @DisplayName("writeToFile throws on null filePath")
    void writeNullPathThrows() {
      assertThrows(IllegalArgumentException.class,
          () -> handler.writeToFile(List.of(alice), null));
    }

    @Test
    @DisplayName("writeToFile throws on blank filePath")
    void writeBlankPathThrows() {
      assertThrows(IllegalArgumentException.class,
          () -> handler.writeToFile(List.of(alice), " "));
    }

    @Test
    @DisplayName("readFromFile throws on null filePath")
    void readNullPathThrows() {
      assertThrows(IllegalArgumentException.class,
          () -> handler.readFromFile(null));
    }

    @Test
    @DisplayName("readFromFile throws on blank filePath")
    void readBlankPathThrows() {
      assertThrows(IllegalArgumentException.class,
          () -> handler.readFromFile(" "));
    }

    @Test
    @DisplayName("writeToFile propagates IOException when writing to directory")
    void writeDirectoryThrowsIOException(@TempDir Path tempDir) throws IOException {
      Path dir = tempDir.resolve("dir");
      Files.createDirectory(dir);

      assertThrows(IOException.class,
          () -> handler.writeToFile(List.of(alice), dir.toString()));
    }

    @Test
    @DisplayName("readFromFile propagates IOException for missing file")
    void readMissingFileThrowsIOException(@TempDir Path tempDir) {
      Path missing = tempDir.resolve("none.csv");

      assertThrows(IOException.class,
          () -> handler.readFromFile(missing.toString()));
    }
  }
}
