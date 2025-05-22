package edu.ntnu.bidata.idatt.utils.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("GameUIException Tests")
class GameUIExceptionTest {

  private Throwable sampleCause;

  @BeforeAll
  void initAll() {
    sampleCause = new IllegalStateException("Underlying failure");
  }

  @Nested
  @DisplayName("Constructor with message and cause")
  class MessageAndCauseConstructorTests {

    private GameUIException ex;

    @BeforeEach
    void setUp() {
      ex = new GameUIException("UI error occurred", sampleCause);
    }

    @Test
    @DisplayName("getMessage returns the provided message")
    void messageIsPreserved() {
      String message = ex.getMessage();
      assertEquals("UI error occurred", message,
          "Constructor should preserve the provided message");
    }

    @Test
    @DisplayName("getCause returns the provided cause")
    void causeIsPreserved() {
      Throwable cause = ex.getCause();
      assertSame(sampleCause, cause,
          "Constructor should preserve the provided cause");
    }

    @Test
    @DisplayName("null message is allowed and getMessage returns null")
    void nullMessageAllowed() {
      GameUIException exNullMsg = new GameUIException(null, sampleCause);
      assertNull(exNullMsg.getMessage(),
          "Exception constructed with null message should return null from getMessage()");
      assertSame(sampleCause, exNullMsg.getCause(),
          "Cause should still be preserved when message is null");
    }

    @Test
    @DisplayName("null cause is allowed and getCause returns null")
    void nullCauseAllowed() {
      GameUIException exNullCause = new GameUIException("Some UI error", null);
      assertEquals("Some UI error", exNullCause.getMessage(),
          "Message should be preserved even if cause is null");
      assertNull(exNullCause.getCause(),
          "Exception constructed with null cause should return null from getCause()");
    }
  }

  @Nested
  @DisplayName("Constructor with cause only")
  class CauseOnlyConstructorTests {

    private GameUIException ex;

    @BeforeEach
    void setUp() {
      ex = new GameUIException(sampleCause);
    }

    @Test
    @DisplayName("getMessage returns the default message")
    void defaultMessageIsUsed() {
      String message = ex.getMessage();
      assertEquals("Unexpected UI error", message,
          "Single-arg constructor should set default message");
    }

    @Test
    @DisplayName("getCause returns the provided cause")
    void causeIsPreserved() {
      Throwable cause = ex.getCause();
      assertSame(sampleCause, cause,
          "Single-arg constructor should preserve the provided cause");
    }

    @Test
    @DisplayName("null cause yields default message and null getCause")
    void nullCauseHandled() {
      GameUIException exNull = new GameUIException((Throwable) null);
      assertEquals("Unexpected UI error", exNull.getMessage(),
          "Single-arg constructor should use default message even if cause is null");
      assertNull(exNull.getCause(),
          "Single-arg constructor with null cause should return null from getCause()");
    }
  }
}
