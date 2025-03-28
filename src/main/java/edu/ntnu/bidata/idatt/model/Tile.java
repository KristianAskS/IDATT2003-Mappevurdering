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

  public Tile(int tileId) {
    this.tileId = tileId;
  }

  public TileAction getLandAction() {
    return landAction;
  }

  public void setLandAction(TileAction landAction) {
    this.landAction = landAction;
  }


  public void landPlayer(Player player) {

  }

  public void leavePlayer(Player player) {

  }

  public Tile getNextTile() {
    return nextTile;
  }

  public void setNextTile(Tile nextTile) {
    this.nextTile = nextTile;
  }

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
