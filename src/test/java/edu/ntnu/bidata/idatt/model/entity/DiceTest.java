package edu.ntnu.bidata.idatt.model.entity;

import static edu.ntnu.bidata.idatt.view.components.DiceView.NUMB_OF_DICE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Dice Tests")
class DiceTest {

  @Nested
  @DisplayName("Positive Tests")
  class PositiveTests {

    @Test
    @DisplayName("rollDice returns array of length NUMB_OF_DICE")
    void rollDiceReturnsArrayOfCorrectLength() {
      Dice dice = new Dice(NUMB_OF_DICE);
      int[] results = dice.rollDice();
      assertEquals(NUMB_OF_DICE, results.length);
    }

    @Test
    @DisplayName("rollDice returns values between 1 and 6")
    void rollDiceValuesBetween1And6() {
      Dice dice = new Dice(NUMB_OF_DICE);
      int[] results = dice.rollDice();
      for (int value : results) {
        assertTrue(value >= 1 && value <= 6);
      }
    }

    @Test
    @DisplayName("setRollResult updates value")
    void setRollResultUpdatesValue() {
      Dice dice = new Dice(NUMB_OF_DICE);
      dice.setRollResult(7);
      assertEquals(7, dice.getRollResult());
    }

    @Test
    @DisplayName("getRollResult reflects last set value")
    void getRollResultReflectsSetValue() {
      Dice dice = new Dice(NUMB_OF_DICE);
      dice.setRollResult(3);
      assertEquals(3, dice.getRollResult());
    }
  }

  @Nested
  @DisplayName("Negative Tests")
  class NegativeTests {

    @Test
    @DisplayName("Default rollResult is zero")
    void defaultRollResultIsZero() {
      Dice dice = new Dice(NUMB_OF_DICE);
      assertEquals(0, dice.getRollResult());
    }

    @Test
    @DisplayName("setRollResult allows negative values")
    void setRollResultAllowsNegativeValues() {
      Dice dice = new Dice(NUMB_OF_DICE);
      dice.setRollResult(-8);
      assertEquals(-8, dice.getRollResult());
    }

    @Test
    @DisplayName("rollDice not affected by rollResult setter and returns valid values")
    void rollDiceUnaffectedByRollResultSetter() {
      Dice dice = new Dice(NUMB_OF_DICE);
      dice.setRollResult(-5);
      int[] results = dice.rollDice();
      for (int value : results) {
        assertTrue(value >= 1 && value <= 6);
      }
      assertEquals(-5, dice.getRollResult());
    }
  }
}
