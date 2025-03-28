package edu.ntnu.bidata.idatt.entity;

import edu.ntnu.bidata.idatt.utils.InputValidation;
import java.util.Random;

public class Die {
  private final Random random = new Random();
  private int lastRolledValue;
  public Die() {

  }
  public int roll() {
    lastRolledValue = random.nextInt(6) + 1;
    return lastRolledValue;
  }

  public int getValue() {
    return lastRolledValue;
  }

  public void setValue(int value) {
    InputValidation.validateSetDiceValue(value);
    this.lastRolledValue = value;
  }
}
