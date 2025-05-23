package edu.ntnu.bidata.idatt.model.entity;

import java.util.Random;

/**
 * <p>Represents a single die.</p>
 * <p>Encapsulates the random logic.</p>
 *
 * @author Tri Tac Le
 * @since 1.0
 */
public class Die {
  private final Random random = new Random();
  private int lastRolledValue;

  /**
   * Default constructor.
   * <p>No initialization is needed.</p>
   */
  public Die() {
  }

  /**
   * Rolls the die and gets a random value between 1 and 6 (inclusive),
   * stores it internally, and returns it.
   *
   * @return the value of the roll
   */
  public int roll() {
    lastRolledValue = random.nextInt(6) + 1;
    return lastRolledValue;
  }

  /**
   * Returns the value from the most recent roll.
   *
   * @return the last rolled value, or 0 if {@link #roll()} has not been called
   */
  public int getLastRolledValue() {
    return lastRolledValue;
  }

  /**
   * Manually sets the last rolled value.
   * <p>Used for testing.</p>
   *
   * @param value the value to record
   */
  public void setLastRolledValue(int value) {
    if (value > 6 || value < 1) {
      throw new IllegalArgumentException("Die value mist be between 1 and 6");
    }
    this.lastRolledValue = value;
  }
}
