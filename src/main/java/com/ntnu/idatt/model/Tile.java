package com.ntnu.idatt.model;

import com.ntnu.idatt.entity.Player;
import com.ntnu.idatt.logic.action.TileAction;

/**
 * The type Tile.
 */
public class Tile {
  
  private final int tileId;
  private Tile nextTile;
  private TileAction landAction;
  
  /**
   * Instantiates a new Tile.
   *
   * @param tileId the tile id
   */
  public Tile(int tileId) {
    this.tileId = tileId;
  }
  
  /**
   * Sets land action.
   *
   * @param landAction the land action
   */
  public void setLandAction(TileAction landAction) {
    this.landAction = landAction;
  }
  
  /**
   * Land player.
   *
   * @param player the player
   */
  public void landPlayer(Player player) {
  
  }
  
  /**
   * Leave player.
   *
   * @param player the player
   */
  public void leavePlayer(Player player) {
  
  }
  
  /**
   * Gets next tile.
   *
   * @return the next tile
   */
  public Tile getNextTile() {
    return nextTile;
  }
  
  /**
   * Sets next tile.
   *
   * @param nextTile the next tile
   */
  public void setNextTile(Tile nextTile) {
    this.nextTile = nextTile;
  }
  
  /**
   * Gets tile id.
   *
   * @return the tile id
   */
  public int getTileId() {
    return tileId;
  }
  
}
