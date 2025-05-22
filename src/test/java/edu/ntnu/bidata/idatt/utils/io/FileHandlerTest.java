package edu.ntnu.bidata.idatt.utils.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests for the {@link FileHandler} interface via a simple String-based implementation.
 */
@DisplayName("FileHandler Interface Tests")
class FileHandlerTest {
  @TempDir
  Path tempDir;
  private Path testFile;
  private FileHandler<String> handler;

  @BeforeEach
  void setUp() {
    handler = new StringFileHandler();
    testFile = tempDir.resolve("strings.txt");
  }

  /**
   * A minimal FileHandler<String> implementation for testing.
   * Uses java.nio.Files to write and read plain text lines.
   */
  private static class StringFileHandler implements FileHandler<String> {
    @Override
    public void writeToFile(List<String> element, String filePath) throws IOException {
      Files.write(Path.of(filePath), element);
    }

    @Override
    public List<String> readFromFile(String filePath) throws IOException {
      return Files.readAllLines(Path.of(filePath));
    }
  }

  @Nested
  @DisplayName("Positive behavior")
  class PositiveTests {

    @Test
    @DisplayName("writeToFile then readFromFile returns the same list")
    void writeThenReadCycle() throws IOException {
      List<String> original = List.of("line1", "line2", "line3");

      handler.writeToFile(original, testFile.toString());
      List<String> readBack = handler.readFromFile(testFile.toString());

      assertEquals(original, readBack, "After writing and reading, the lists should match");
    }

    @Test
    @DisplayName("readFromFile returns empty list for empty file")
    void readEmptyFileReturnsEmptyList() throws IOException {
      Files.createFile(testFile);

      List<String> result = handler.readFromFile(testFile.toString());

      assertTrue(result.isEmpty(), "Reading an empty file should yield an empty list");
    }
  }

  @Nested
  @DisplayName("Negative & edge‑case behavior")
  class NegativeTests {

    @Test
    @DisplayName("readFromFile throws IOException for nonexistent file")
    void readMissingFileThrows() {
      assertThrows(IOException.class,
          () -> handler.readFromFile(tempDir.resolve("no_such_file.txt").toString()),
          "Reading a non‑existent file should throw IOException");
    }

    @Test
    @DisplayName("writeToFile throws IOException when path is a directory")
    void writeToDirectoryThrows() {
      assertThrows(IOException.class,
          () -> handler.writeToFile(List.of("Tri"), tempDir.toString()),
          "Writing to a directory path should throw IOException");
    }
  }
}
