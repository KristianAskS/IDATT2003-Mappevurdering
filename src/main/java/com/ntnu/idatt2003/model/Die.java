package com.ntnu.idatt2003.model;

import java.util.Random;

public class Die {
  private int lastRolledValue;
  private Random random = new Random();
  
  public int roll(){
    lastRolledValue = random.nextInt()*6+1;
    return lastRolledValue;
  }
  
  public int getValue(){
    return lastRolledValue;
  }
}
