package edu.ntnu.bidata.idatt.model.entity;

import static edu.ntnu.bidata.idatt.view.components.DiceView.NUMB_OF_DICE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Dice Entity Tests")
class DiceTest {

  private Dice dice;
  private int expectedDiceCount;

  @BeforeAll
  void beforeAll() {
    expectedDiceCount = NUMB_OF_DICE;
  }

  @BeforeEach
  void setUp() {
    dice = new Dice(expectedDiceCount);
  }

  @Nested
  @DisplayName("Positive behavior")
  class PositiveTests {

    @Test
    @DisplayName("rollDice returns array of correct length")
    void rollDiceArrayLength() {
      int[] results = dice.rollDice();
      assertEquals(expectedDiceCount, results.length,
          "rollDice() should return array length == NUMB_OF_DICE");
    }

    @Test
    @DisplayName("rollDice values are within 1–6 inclusive")
    void rollDiceValueRange() {
      int[] results = dice.rollDice();
      for (int value : results) {
        assertTrue(value >= 1 && value <= 6,
            "Each die roll must be between 1 and 6, but was " + value);
      }
    }

    @Test
    @DisplayName("Each rollDice call returns a new array instance")
    void rollDiceReturnsNewArray() {
      int[] first = dice.rollDice();
      int[] second = dice.rollDice();
      assertNotSame(first, second,
          "rollDice() should produce a new array on each invocation");
    }

    @Test
    @DisplayName("setRollResult and getRollResult with various values")
    void setAndGetRollResult() {
      int[] testValues = {0, 5, 12, -3};
      for (int v : testValues) {
        dice.setRollResult(v);
        assertEquals(v, dice.getRollResult(),
            () -> "after setRollResult(" + v + "), getRollResult() should be " + v);
      }
    }

    @Test
    @DisplayName("rollDice does not alter the stored rollResult")
    void rollDiceDoesNotChangeStoredRollResult() {
      dice.setRollResult(99);
      dice.rollDice();
      assertEquals(99, dice.getRollResult(),
          "Calling rollDice() should not modify rollResult field");
    }
  }

  @Nested
  @DisplayName("Negative & edge cases")
  class NegativeTests {

    @Test
    @DisplayName("Default rollResult is zero")
    void defaultRollResultIsZero() {
      Dice fresh = new Dice(expectedDiceCount);
      assertEquals(0, fresh.getRollResult(),
          "New Dice should have rollResult == 0 by default");
    }

    @Test
    @DisplayName("Insufficient dice throws IndexOutOfBoundsException")
    void insufficientDiceThrows() {
      Dice tooFew = new Dice(expectedDiceCount - 1);
      assertThrows(IndexOutOfBoundsException.class,
          tooFew::rollDice,
          "rollDice() must fail if internal list size < NUMB_OF_DICE");
    }

    @Test
    @DisplayName("Zero dice throws IndexOutOfBoundsException")
    void zeroDiceThrows() {
      Dice none = new Dice(0);
      assertThrows(IndexOutOfBoundsException.class,
          none::rollDice,
          "rollDice() must fail when constructed with zero dice");
    }
  }
}
