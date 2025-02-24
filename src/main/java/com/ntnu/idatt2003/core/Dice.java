package com.ntnu.idatt2003.core;

import java.util.ArrayList;
import java.util.List;

public class Dice {
<<<<<<< HEAD

  private List<Die> dice;

  public Dice(int numberOfDice) {
=======
  private final List<Die> dice;
  
  public Dice(int numberOfDice){
>>>>>>> 0b26a96 (Uncommitted changes extern)
    dice = new ArrayList<>();
    for (int i = 0; i < numberOfDice; i++) {
      dice.add(new Die());
    }
  }

  public int roll() {
    int sum = 0;
    for (Die die : dice) {
      sum += die.roll();
    }
    return sum;
  }

  public int getDie(int dieNumber) {
    if (dieNumber < 0 || dieNumber >= dice.size()) {
      throw new IllegalArgumentException("Die number is out of bounds");
    }
    return dice.get(dieNumber).getValue();
  }


}