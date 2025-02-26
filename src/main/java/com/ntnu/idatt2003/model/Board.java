package com.ntnu.idatt2003.model;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Board.
 */
public class Board {
  
  private Map<Integer, Tile> tiles = new HashMap<>();
  
  public Board() {
  }
  
  public Board(Map<Integer, Tile> tiles) {
    this.tiles = tiles;
  }
  
  public void addTile(Tile tile) {
    tiles.put(tile.getTileId(), tile);
  }
  
  public Tile getTileId(int tileId) {
    return tiles.get(tileId);
  }
}