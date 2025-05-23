package edu.ntnu.bidata.idatt.model.entity;

import static edu.ntnu.bidata.idatt.view.components.DiceView.NUMB_OF_DICE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <p>Represents a collection of dice.</p>
 * <p>Manages a fixed number of {@link Die}.</p>
 *
 * @author Tri Tac Le
 * @version 2.0
 * @since 1.0
 */
public class Dice {
  private final List<Die> dice;
  private int rollResult;

  /**
   * Constructs a collection of die with the specified number of dice.
   *
   * @param numberOfDice the number of dice to include
   */
  public Dice(int numberOfDice) {
    dice = IntStream.range(0, numberOfDice)
        .mapToObj(i -> new Die())
        .collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * Rolls all dice in the collection.
   *
   * @return an array of roll results
   */
  public int[] rollDice() {
    int[] results = new int[NUMB_OF_DICE];
    for (int i = 0; i < NUMB_OF_DICE; i++) {
      results[i] = dice.get(i).roll();
    }
    return results;
  }

  /**
   * Returns the roll result from the last roll
   *
   * @return the last roll result
   */
  public int getRollResult() {
    return rollResult;
  }

  /**
   * Sets the roll result.
   *
   * @param result the total value as the roll result
   */
  public void setRollResult(int result) {
    this.rollResult = result;
  }
}
