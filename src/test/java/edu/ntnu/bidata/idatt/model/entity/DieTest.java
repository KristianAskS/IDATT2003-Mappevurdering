package edu.ntnu.bidata.idatt.model.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Die Tests")
class DieTest {

  @Nested
  @DisplayName("Positive Tests")
  class PositiveTests {

    @Test
    @DisplayName("Roll returns value between 1 and 6 inclusive")
    void rollReturnsValueBetween1And6() {
      Die die = new Die();
      int result = die.roll();
      assertTrue(result >= 1 && result <= 6);
    }

    @Test
    @DisplayName("getLastRolledValue reflects last roll")
    void getLastRolledValueReflectsLastRoll() {
      Die die = new Die();
      int rollResult = die.roll();
      assertEquals(rollResult, die.getLastRolledValue());
    }

    @Test
    @DisplayName("setLastRolledValue updates value")
    void setLastRolledValueUpdatesValue() {
      Die die = new Die();
      die.setLastRolledValue(4);
      assertEquals(4, die.getLastRolledValue());
    }

    @Test
    @DisplayName("roll overrides previous lastRolledValue and returns within range")
    void rollAfterSettingLastRolledValueOverridesPreviousValue() {
      Die die = new Die();
      die.setLastRolledValue(10);
      int result = die.roll();
      assertTrue(result >= 1 && result <= 6);
      assertEquals(result, die.getLastRolledValue());
    }
  }

  @Nested
  @DisplayName("Negative Tests")
  class NegativeTests {

    @Test
    @DisplayName("Default lastRolledValue is zero")
    void defaultLastRolledValueIsZero() {
      Die die = new Die();
      assertEquals(0, die.getLastRolledValue());
    }

    @Test
    @DisplayName("setLastRolledValue allows negative values")
    void setLastRolledValueAllowsNegativeValues() {
      Die die = new Die();
      die.setLastRolledValue(-5);
      assertEquals(-5, die.getLastRolledValue());
    }

    @Test
    @DisplayName("roll never returns value outside range even after negative setter")
    void rollNeverReturnsValueOutsideRangeEvenAfterNegativeSetter() {
      Die die = new Die();
      die.setLastRolledValue(-100);
      int result = die.roll();
      assertTrue(result >= 1 && result <= 6);
      assertEquals(result, die.getLastRolledValue());
    }
  }
}
