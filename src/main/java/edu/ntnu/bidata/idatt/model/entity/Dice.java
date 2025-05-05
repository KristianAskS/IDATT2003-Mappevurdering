package edu.ntnu.bidata.idatt.model.entity;

import static edu.ntnu.bidata.idatt.view.components.DiceView.NUMB_OF_DICE;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
  private final Random random = new Random();
  private int rollResult;

  public Dice(int numberOfDice) {
    dice = IntStream.range(0, numberOfDice)
        .mapToObj(Die -> new Die())
        .collect(Collectors.toCollection(ArrayList::new));
  }

  public int[] rollDice() {
    int[] results = new int[NUMB_OF_DICE];
    for (int i = 0; i < NUMB_OF_DICE; i++) {
      results[i] = random.nextInt(6) + 1;
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