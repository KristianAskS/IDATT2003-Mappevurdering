package edu.ntnu.bidata.idatt.utils.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("InvalidGameStateException Tests")
class InvalidGameStateExceptionTest {

  private String normalMessage;
  private String emptyMessage;
  private String nullMessage;

  @BeforeAll
  void initAll() {
    normalMessage = "Invalid move at this stage";
    emptyMessage = "";
    nullMessage = null;
  }

  @Nested
  @DisplayName("Positive message tests")
  class PositiveMessageTests {

    @Test
    @DisplayName("Constructor preserves non-null message")
    void preservesNonNullMessage() {
      InvalidGameStateException ex = new InvalidGameStateException(normalMessage);
      assertEquals(normalMessage, ex.getMessage(),
          "getMessage() should return the message passed to the constructor");
    }

    @Test
    @DisplayName("Constructor preserves empty message")
    void preservesEmptyMessage() {
      InvalidGameStateException ex = new InvalidGameStateException(emptyMessage);
      assertEquals(emptyMessage, ex.getMessage(),
          "getMessage() should return empty string when passed empty message");
    }
  }

  @Nested
  @DisplayName("Null message tests")
  class NullMessageTests {

    @Test
    @DisplayName("Constructor handles null message gracefully")
    void handlesNullMessage() {
      InvalidGameStateException ex = new InvalidGameStateException(nullMessage);
      assertNull(ex.getMessage(),
          "getMessage() should return null when constructed with null message");
    }

    @Test
    @DisplayName("Stack trace is still available when message is null")
    void stackTracePresentWhenNullMessage() {
      InvalidGameStateException ex = new InvalidGameStateException(nullMessage);
      ex.fillInStackTrace();
      StackTraceElement[] trace = ex.getStackTrace();
      assertTrue(trace.length > 0,
          "Exception should have a non-empty stack trace even if message is null");
    }
  }
}
