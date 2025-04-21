package edu.ntnu.bidata.idatt.model.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Board.
 */
public class Board {
  private Map<Integer, Tile> tiles = new HashMap<>();
  private String name;
  private String description;

  public Board() {
  }

  public Board(String name, String description) {
    this.name = name;
    this.description = description;
    tiles = new HashMap<>();
  }

  public void addTile(Tile tile) {
    tiles.put(tile.getTileId(), tile);
  }

  public Tile getTileId(int tileId) {
    return tiles.get(tileId);
  }

  public Map<Integer, Tile> getTiles() {
    return tiles;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Tile getTile(int tileId) {
    return tiles.get(tileId);
  }
}