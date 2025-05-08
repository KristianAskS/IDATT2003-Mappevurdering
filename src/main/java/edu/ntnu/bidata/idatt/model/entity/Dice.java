package edu.ntnu.bidata.idatt.model.entity;

import static edu.ntnu.bidata.idatt.view.components.DiceView.NUMB_OF_DICE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Stores the collection of dices
 *
 * @author Trile
 * @version 2.0
 */
public class Dice {
  private final List<Die> dice;
  private int rollResult;

  public Dice(int numberOfDice) {
    dice = IntStream.range(0, numberOfDice)
        .mapToObj(Die -> new Die())
        .collect(Collectors.toCollection(ArrayList::new));
  }

  public int[] rollDice() {
    int[] results = new int[NUMB_OF_DICE];
    for (int dieNumb = 0; dieNumb < NUMB_OF_DICE; dieNumb++) {
      results[dieNumb] = dice.get(dieNumb).roll();
    }
    return results;
  }

  public int getRollResult() {
    return rollResult;
  }

  public void setRollResult(int result) {
    this.rollResult = result;
  }
}