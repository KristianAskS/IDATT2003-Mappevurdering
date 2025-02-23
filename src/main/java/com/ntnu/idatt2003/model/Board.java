package com.ntnu.idatt2003.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Board.
 */
public class Board {
  private final Map<Integer, Integer> laddersAndSnakes;
  
  /**
   * Instantiates a new Board.
   */
  public Board() {
    laddersAndSnakes = new HashMap<>();
    laddersAndSnakes.put(4, 14);
    laddersAndSnakes.put(9, 31);
    laddersAndSnakes.put(17, 7);
    laddersAndSnakes.put(28, 84);
    laddersAndSnakes.put(54, 34);
  }
  
  /**
   * Check position int.
   *
   * @param position the position
   * @return the int
   */
  public int checkPosition(int position) {
    return laddersAndSnakes.getOrDefault(position, position);
  }
  
  /**
   * Gets max position.
   *
   * @return the max position
   */
  public int getMaxPosition() { return 90; }
}