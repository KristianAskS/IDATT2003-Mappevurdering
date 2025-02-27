package com.ntnu.idatt.model;

import com.ntnu.idatt.entity.Die;
import java.util.ArrayList;
import java.util.List;

/**
 * Stores the collection of dices
 */
public class Dice {

  private final List<Die> dice;


  /**
   * Instantiates a new Dice.
   *
   * @param numberOfDice the number of dice
   */
  public Dice(int numberOfDice) {

    dice = new ArrayList<>();
    for (int i = 0; i < numberOfDice; i++) {
      dice.add(new Die());
    }
  }

  /**
   * Roll every die in dice.
   *
   * @return the sum of eyes rolled
   */
  public int roll() {
    int sum = 0;
    for (Die die : dice) {
      sum += die.roll();
    }
    return sum;
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