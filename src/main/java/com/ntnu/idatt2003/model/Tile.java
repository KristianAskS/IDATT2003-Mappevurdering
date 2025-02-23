package com.ntnu.idatt2003.model;

public class Tile {
  private Tile nextTile;
  private int tileId;
  private TileAction landAction;
  
  public Tile(int tileId) {
    this.tileId = tileId;
  }
  
  public void setLandAction(TileAction landAction) {
    this.landAction = landAction;
  }
  
  public void landPlayer(Player player){
  
  }
  
  public void leavePlayer(Player player){
  
  }
  
  public void setNextTile(Tile nextTile) {
    this.nextTile = nextTile;
  }
  
  public Tile getNextTile(){
    return nextTile;
  }
  
  public int getTileId() {
    return tileId;
  }
  
}
