package edu.ntnu.bidata.idatt.utils.exceptions;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("BoardParsingException Tests")
class BoardParsingExceptionTest {

  private static String validPath;
  private static JsonSyntaxException validCause;

  @BeforeAll
  static void initAll() {
    validPath = "data/board.json";
    validCause = new JsonSyntaxException("invalid JSON");
  }

  @Nested
  @DisplayName("Positive Tests")
  class PositiveTests {

    private BoardParsingException exception;

    @BeforeEach
    void setUp() {
      exception = new BoardParsingException(validPath, validCause);
    }

    @Test
    @DisplayName("getMessage returns correct prefix plus file path")
    void messageIncludesFilePath() {
      String message = exception.getMessage();
      assertEquals(
          "Error parsing board from: " + validPath,
          message,
          "Exception message should include the file path"
      );
    }

    @Test
    @DisplayName("getCause returns the underlying JsonSyntaxException")
    void causeIsPreserved() {
      Throwable cause = exception.getCause();
      assertSame(
          validCause,
          cause,
          "getCause() should return the same JsonSyntaxException passed to constructor"
      );
    }
  }

  @Nested
  @DisplayName("Negative & Edge-case Tests")
  class NegativeTests {

    @Test
    @DisplayName("null cause results in null getCause()")
    void nullCauseAllowed() {
      BoardParsingException ex = new BoardParsingException(validPath, null);
      assertNull(
          ex.getCause(),
          "When constructed with a null cause, getCause() should return null"
      );
    }

    @Test
    @DisplayName("null filePath is rendered as \"null\" in the message")
    void nullFilePathHandled() {
      BoardParsingException ex = new BoardParsingException(null, validCause);
      String message = ex.getMessage();
      assertEquals(
          "Error parsing board from: null",
          message,
          "A null filePath should appear as \"null\" in the exception message"
      );
      assertSame(
          validCause,
          ex.getCause(),
          "Cause should still be preserved when filePath is null"
      );
    }

    @Test
    @DisplayName("both filePath and cause null still constructs exception")
    void bothNullHandledGracefully() {
      BoardParsingException ex = new BoardParsingException(null, null);
      assertAll("both-null",
          () -> assertEquals(
              "Error parsing board from: null",
              ex.getMessage(),
              "Message should default to include \"null\" path"),
          () -> assertNull(
              ex.getCause(),
              "getCause() should be null when constructed with null cause")
      );
    }
  }
}
