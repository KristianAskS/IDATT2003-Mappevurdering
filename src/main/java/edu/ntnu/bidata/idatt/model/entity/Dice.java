package edu.ntnu.bidata.idatt.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Stores the collection of dices
 * @author Trile
 * @version 2.0
 */
public class Dice {
  private final List<Die> dice;

  public Dice(int numberOfDice) {
    dice = IntStream.range(0, numberOfDice)
        .mapToObj(Die-> new Die())
        .collect(Collectors.toCollection(ArrayList::new));
  }

  /**
   * Roll all the dices in the dice array
   * @return the sum of the rolled dices
   */
  public int roll() {
    return dice.stream()
        .mapToInt(Die::roll)
        .sum();
  }

  /**
   * Gets die.
   *
   * @param dieNumber the die number
   * @return the die
   */
  public int getDie(int dieNumber) {
    if (dieNumber < 0 || dieNumber >= dice.size()) {
      throw new IllegalArgumentException("Die number is out of bounds");
    }
    return dice.get(dieNumber).getValue();
  }
}