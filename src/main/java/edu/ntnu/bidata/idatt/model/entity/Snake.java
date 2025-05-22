package edu.ntnu.bidata.idatt.model.entity;


public class Snake {

  private final int startTileId;
  private final int endTileId;

  public Snake(int startTileId, int endTileId) {
    this.startTileId = startTileId;
    this.endTileId = endTileId;
  }

  public int getStartTileId() {
    return startTileId;
  }

  public int getEndTileId() {
    return endTileId;
  }

}