package edu.ntnu.bidata.idatt.model;

import edu.ntnu.bidata.idatt.entity.Player;
import edu.ntnu.bidata.idatt.logic.action.TileAction;

/**
 * The type Tile.
 */
public class Tile {

  private int tileId;
  private int nextTileId;
  private Tile nextTile;
  private TileAction landAction;

  /**
   * Instantiates a new Tile.
   */
  public Tile(){}

  /**
   * Instantiates a new Tile.
   *
   * @param tileId the tile id
   */
  public Tile(int tileId) {
    this.tileId = tileId;
    this.nextTileId = -1;
  }

  public TileAction getLandAction() {
    return landAction;
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
  public void setTileId(int tileId) {
    this.tileId = tileId;
  }

  public int getNextTileId() {
    return nextTileId;
  }
  public void setNextTileId(int nextTileId) {
    this.nextTileId = nextTileId;
  }
}
