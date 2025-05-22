package edu.ntnu.bidata.idatt.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Die Entity Tests")
class DieTest {

  private Die die;

  @BeforeEach
  void setUp() {
    die = new Die();
  }

  @Nested
  @DisplayName("Positive behavior")
  class PositiveTests {

    @Test
    @DisplayName("Default lastRolledValue is zero")
    void defaultLastRolledValueIsZero() {
      assertEquals(0, die.getLastRolledValue(),
          "A new Die should have lastRolledValue == 0");
    }

    @Test
    @DisplayName("roll returns value between 1 and 6 inclusive")
    void rollReturnsValueBetween1And6() {
      int result = die.roll();
      assertTrue(result >= 1 && result <= 6,
          "roll() should produce a value between 1 and 6, but was " + result);
    }

    @Test
    @DisplayName("getLastRolledValue reflects the last roll")
    void getLastRolledValueReflectsRoll() {
      int firstRoll = die.roll();
      int secondRoll = die.roll();
      assertEquals(secondRoll, die.getLastRolledValue(),
          "getLastRolledValue() should return the most recent roll value");
      assertNotEquals(firstRoll, secondRoll,
          "Two consecutive rolls should generally differ");
    }

    @Test
    @DisplayName("setLastRolledValue accepts valid values and getLastRolledValue returns them")
    void setAndGetLastRolledValueValid() {
      int[] validValues = {1, 3, 6};
      for (int v : validValues) {
        die.setLastRolledValue(v);
        assertEquals(v, die.getLastRolledValue(),
            () -> "After setLastRolledValue(" + v + "), getLastRolledValue() should be " + v);
      }
    }

    @Test
    @DisplayName("roll overrides manually set lastRolledValue")
    void rollOverridesManualValue() {
      die.setLastRolledValue(5);
      int newRoll = die.roll();
      assertTrue(newRoll >= 1 && newRoll <= 6,
          "roll() after manual setter should still be between 1 and 6");
      assertEquals(newRoll, die.getLastRolledValue(),
          "roll() should update lastRolledValue to the new roll");
    }
  }

  @Nested
  @DisplayName("Negative & edge cases")
  class NegativeTests {

    @Test
    @DisplayName("setLastRolledValue with zero throws IllegalArgumentException")
    void setZeroThrows() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> die.setLastRolledValue(0),
          "setLastRolledValue(0) should throw IllegalArgumentException");
      assertTrue(ex.getMessage().contains("between 1 and 6"));
    }

    @Test
    @DisplayName("setLastRolledValue with negative throws IllegalArgumentException")
    void setNegativeThrows() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> die.setLastRolledValue(-2),
          "setLastRolledValue(-2) should throw IllegalArgumentException");
      assertTrue(ex.getMessage().contains("between 1 and 6"));
    }

    @Test
    @DisplayName("setLastRolledValue with value > 6 throws IllegalArgumentException")
    void setAboveSixThrows() {
      IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
          () -> die.setLastRolledValue(7),
          "setLastRolledValue(7) should throw IllegalArgumentException");
      assertTrue(ex.getMessage().contains("between 1 and 6"));
    }

    @Test
    @DisplayName("roll repeatedly does not throw and stays in range")
    void repeatedRollsStayInRange() {
      for (int i = 0; i < 100; i++) {
        int result = die.roll();
        assertTrue(result >= 1 && result <= 6,
            "Iteration " + i + ": roll() should be between 1 and 6, but was " + result);
      }
    }
  }
}
