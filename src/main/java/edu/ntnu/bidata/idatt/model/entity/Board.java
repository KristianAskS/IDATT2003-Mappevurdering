package edu.ntnu.bidata.idatt.model.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a game board consisting of tiles mapped.
 */
public class Board {

  private final Map<Integer, Tile> tiles = new HashMap<>();
  private String name;
  private String description;

  public Board() {
  }

  public Board(String name, String description) {
    this.name = name;
    this.description = description;
  }

  public void addTile(Tile tile) {
    tiles.put(tile.getTileId(), tile);
  }

  public Tile getTile(int tileId) {
    return tiles.get(tileId);
  }

  public Map<Integer, Tile> getTiles() {
    return Collections.unmodifiableMap(tiles);
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
}
