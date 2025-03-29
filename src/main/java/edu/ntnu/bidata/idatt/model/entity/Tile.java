package edu.ntnu.bidata.idatt.model.entity;

import edu.ntnu.bidata.idatt.model.logic.action.TileAction;

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

  public Tile getNextTile() {
    return nextTile;
  }

  public void setNextTile(Tile nextTile) {
    this.nextTile = nextTile;
    if (nextTile != null) {
      this.nextTileId = nextTile.getTileId();
    }
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
