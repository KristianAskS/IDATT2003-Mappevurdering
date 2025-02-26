package com.ntnu.idatt2003.model;

import com.ntnu.idatt2003.entity.Player;
import com.ntnu.idatt2003.logic.action.TileAction;

public class Tile {
  
  private final int tileId;
  private Tile nextTile;
  private TileAction landAction;
  
  public Tile(int tileId) {
    this.tileId = tileId;
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
  
}
