package com.ntnu.idatt2003.core;

import java.util.Random;

public class Die {

  private int lastRolledValue;
<<<<<<< HEAD
  private Random random = new Random();

  public int roll() {
    lastRolledValue = random.nextInt() * 6 + 1;
=======
  private final Random random = new Random();
  
  public int roll(){
    lastRolledValue = random.nextInt()*6+1;
>>>>>>> 0b26a96 (Uncommitted changes extern)
    return lastRolledValue;
  }

  public int getValue() {
    return lastRolledValue;
  }
}
