package edu.ntnu.bidata.idatt.model.entity;

import edu.ntnu.bidata.idatt.model.logic.action.TileAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * The type Tile.
 */
public class Tile {
  private int tileId;
  private int nextTileId;
  private Tile nextTile;
  private TileAction landAction;
  private boolean safe;
  private boolean homeEntry;
  private boolean homeColumn;
  private final List<Player> playersOnTile = new ArrayList<>();

  public Tile(int tileId) {
    this.tileId = tileId;
  }

  public TileAction getLandAction() {
    return landAction;
  }

  public void setLandAction(TileAction landAction) {
    this.landAction = landAction;
  }

  public Tile getNextTile() {
    return nextTile;
  }

  public void setNextTile(Tile nextTile) {
    this.nextTile = nextTile;
    if (nextTile != null) {
      this.nextTileId = nextTile.getTileId();
    }
  }

  public int getTileId() {
    return tileId;
  }

  public void setTileId(int tileId) {
    this.tileId = tileId;
  }

  public int getNextTileId() {
    return nextTileId;
  }

  public void setNextTileId(int nextTileId) {
    this.nextTileId = nextTileId;
  }

  public boolean isSafe() {
    return safe;
  }

  public void setSafe(boolean bool) {
    this.safe = bool;
  }

  public boolean isHomeEntry() {
    return homeEntry;
  }

  public void setHomeEntry(boolean bool) {
    this.homeEntry = bool;
  }

  public boolean isHomeColumn() {
    return homeColumn;
  }

  public void setHomeColumn(boolean bool) {
    this.homeColumn = bool;
  }

  public List<Player> getPlayersOnTile() {
    return Collections.unmodifiableList(playersOnTile);
  }
  void addPlayer(Player player){ playersOnTile.add(player); }
  void removePlayer(Player player){ playersOnTile.remove(player); }
}
