package com.ntnu.idatt2003.model;

import java.util.HashMap;
import java.util.Map;

public class Board {
  private final Map<Integer, Integer> laddersAndSnakes;
  
  public Board() {
    laddersAndSnakes = new HashMap<>();
    laddersAndSnakes.put(4, 14);
    laddersAndSnakes.put(9, 31);
    laddersAndSnakes.put(17, 7);
    laddersAndSnakes.put(28, 84);
    laddersAndSnakes.put(54, 34);
  }
  
  public int checkPosition(int position) {
    return laddersAndSnakes.getOrDefault(position, position);
  }
  
  public int getMaxPosition() { return 90; }
}