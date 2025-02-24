package com.ntnu.idatt2003.core;

import java.util.Random;

public class Die {
  
  private final Random random = new Random();
  private int lastRolledValue;
  
  public int roll() {
    lastRolledValue = random.nextInt() * 6 + 1;
    return lastRolledValue;
  }
  
  public int getValue() {
    return lastRolledValue;
  }
}
