package edu.ntnu.bidata.idatt.model.entity;

/**
 * Visual representation of a ladder connecting two {@link Tile} instances on a Snakes and Ladders
 * board.
 */
public class Ladder {

  private final int startTileId;
  private final int endTileId;

  /**
   * Constructs a new ladder between the specified start and end tile ids
   *
   * @param startTileId the id of the start tile
   * @param endTileId   the id of the end tile
   */
  public Ladder(int startTileId, int endTileId) {
    this.startTileId = startTileId;
    this.endTileId = endTileId;
  }

  /**
   * Returns the id of the start tile
   *
   * @return the start tile id
   */
  public int getStartTileId() {
    return startTileId;
  }

  /**
   * Returns the id of the end tile
   *
   * @return the end tile id
   */
  public int getEndTileId() {
    return endTileId;
  }

}