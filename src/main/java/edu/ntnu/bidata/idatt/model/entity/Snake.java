package edu.ntnu.bidata.idatt.model.entity;


/**
 * Visual representation of a snake connecting two {@link Tile} instances on a Snakes and Ladders board.
 */
public record Snake(int startTileId, int endTileId) {

  /**
   * Constructs a new snake between the specified start and end tile ids
   *
   * @param startTileId
   * @param endTileId
   */
  public Snake {
  }

}