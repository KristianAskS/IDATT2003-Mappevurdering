package edu.ntnu.bidata.idatt.model.entity;

import edu.ntnu.bidata.idatt.utils.InputValidation;
import java.util.Random;

public class Die {
  private final Random random = new Random();
  private int lastRolledValue;

  public Die() {

  }

  /*
   * Roll a single die
   * @return the value rolled
   */
  public int roll() {
    lastRolledValue = random.nextInt(6) + 1;
    return lastRolledValue;
  }

  public int getValue() {
    return lastRolledValue;
  }
}
