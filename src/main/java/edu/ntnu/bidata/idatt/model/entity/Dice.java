package edu.ntnu.bidata.idatt.model.entity;

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

  /**
   * @return the sum of all the rolled values
   */
  public int roll() {
    return dice.stream()
        .mapToInt(Die::roll)
        .sum();
  }

  public void setRollResult(int result) {
    this.rollResult = result;
  }
}