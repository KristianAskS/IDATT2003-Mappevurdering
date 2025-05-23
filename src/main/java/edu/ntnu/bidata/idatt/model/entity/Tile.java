package edu.ntnu.bidata.idatt.model.entity;

import edu.ntnu.bidata.idatt.model.logic.action.TileAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>Represents a tile on the board.</p>
 *
 * @author Tri Tac Le
 * @since 1.0
 */
public class Tile {
  private final List<Player> playersOnTile = new ArrayList<>();
  private int tileId;
  private int nextTileId;
  private Tile nextTile;
  private TileAction landAction;

  /**
   * Constructs a new Tile with the given id.
   *
   * @param tileId the unique id for this tile
   */
  public Tile(int tileId) {
    this.tileId = tileId;
  }

  /**
   * Returns the action to perform when landing on this tile.
   *
   * @return the {@link TileAction}, or {@code null}
   */
  public TileAction getLandAction() {
    return landAction;
  }

  /**
   * Sets the action to perform when a player lands here.
   *
   * @param landAction the {@link TileAction}
   */
  public void setLandAction(TileAction landAction) {
    this.landAction = landAction;
  }

  /**
   * Returns the next linked tile.
   *
   * @return the next {@link Tile}, or {@code null}
   */
  public Tile getNextTile() {
    return nextTile;
  }

  /**
   * Links this tile to the given next tile and updates its id reference.
   *
   * @param nextTile the {@link Tile}
   */
  public void setNextTile(Tile nextTile) {
    this.nextTile = nextTile;
    if (nextTile != null) {
      this.nextTileId = nextTile.getTileId();
    }
  }

  /**
   * Returns this tile’s unique identifier.
   *
   * @return the tile id
   */
  public int getTileId() {
    return tileId;
  }

  /**
   * Updates this tile’s unique identifier.
   *
   * @param tileId the new id to assign
   */
  public void setTileId(int tileId) {
    this.tileId = tileId;
  }

  /**
   * Returns the id of the next tile.
   *
   * @return the next tile id
   */
  public int getNextTileId() {
    return nextTileId;
  }

  /**
   * Sets the id of the next tile without linking the object.
   *
   * @param nextTileId the tile id
   */
  public void setNextTileId(int nextTileId) {
    this.nextTileId = nextTileId;
  }

  /**
   * Returns an unmodifiable list of players currently on this tile.
   *
   * @return a {@link List} of players on the tile
   */
  public List<Player> getPlayersOnTile() {
    return Collections.unmodifiableList(playersOnTile);
  }

  /**
   * Adds a player to this the tile
   *
   * @param player the {@link Player} to place on the tile
   */
  public void addPlayer(Player player) {
    playersOnTile.add(player);
  }

  /**
   * Removes a player from the tile.
   *
   * @param player the {@link Player} to remove from the tile
   */
  public void removePlayer(Player player) {
    playersOnTile.remove(player);
  }
}
