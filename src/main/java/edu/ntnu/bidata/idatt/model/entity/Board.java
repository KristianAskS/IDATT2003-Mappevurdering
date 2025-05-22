package edu.ntnu.bidata.idatt.model.entity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Board which includes {@link Tile} instances.</p>
 *
 * @author Tri Tac Le
 * @version 1.2
 * @since 1.0
 */
public class Board {
  private final Map<Integer, Tile> tiles = new HashMap<>();
  private String name;
  private String description;

  /**
   * <p>Creates an empty board with no name or description.</p>
   */
  public Board() {
  }

  /**
   * <p>Creates a board with the given name and description.</p>
   *
   * @param name        the name of the board
   * @param description a description of the board
   */
  public Board(String name, String description) {
    this.name = name;
    this.description = description;
  }

  /**
   * Adds a tile to this board, which has the tile id as the key.
   *
   * @param tile the {@link Tile} to add
   */
  public void addTile(Tile tile) {
    tiles.put(tile.getTileId(), tile);
  }

  /**
   * Retrieves the tile with the specified id.
   *
   * @param tileId the id of the tile to retrieve
   * @return the {@link Tile} for the given id, or {@code null} if none exists
   */
  public Tile getTile(int tileId) {
    return tiles.get(tileId);
  }

  /**
   * Returns an unmodifiable view of all tiles on this board.
   *
   * @return a {@link Map} mapping tile ids to {@link Tile} instances
   */
  public Map<Integer, Tile> getTiles() {
    return Collections.unmodifiableMap(tiles);
  }

  /**
   * Returns the name of this board.
   *
   * @return the board's name
   */
  public String getName() {
    return name;
  }

  /**
   * Updates the name of this board.
   *
   * @param name the new name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the description of this board.
   *
   * @return the board's description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Updates the description of this board.
   *
   * @param description the new description to set
   */
  public void setDescription(String description) {
    this.description = description;
  }
}
