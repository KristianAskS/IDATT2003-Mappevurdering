package edu.ntnu.bidata.idatt.model.entity;

/**
 * Visual representation of a ladder connecting two {@link Tile} instances on a Snakes and Ladders
 * board.
 */
public class Ladder {

  private final int startTileId;
  private final int endTileId;

  public Ladder(int startTileId, int endTileId) {
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