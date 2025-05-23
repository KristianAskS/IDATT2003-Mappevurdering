package edu.ntnu.bidata.idatt.model.entity;

/**
 * Visual representation of a ladder connecting two {@link Tile} instances on a Snakes and Ladders
 * board.
 */
public record Ladder(int startTileId, int endTileId) {

  /**
   * Constructs a new ladder between the specified start and end tile ids
   *
   * @param startTileId the id of the start tile
   * @param endTileId   the id of the end tile
   */
  public Ladder {
  }

  /**
   * Returns the id of the start tile
   *
   * @return the start tile id
   */
  @Override
  public int startTileId() {
    return startTileId;
  }

  /**
   * Returns the id of the end tile
   *
   * @return the end tile id
   */
  @Override
  public int endTileId() {
    return endTileId;
  }

}