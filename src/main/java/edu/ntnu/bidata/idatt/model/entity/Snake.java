package edu.ntnu.bidata.idatt.model.entity;


/**
 * Visual representation of a snake connecting two {@link Tile} instances on a Snakes and Ladders board.
 */
public class Snake {

  private final int startTileId;
  private final int endTileId;

  /**
   * Constructs a new snake between the specified start and end tile ids
   *
   * @param startTileId
   * @param endTileId
   */
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