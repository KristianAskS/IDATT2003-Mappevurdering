package edu.ntnu.bidata.idatt.model.entity;

import edu.ntnu.bidata.idatt.model.logic.action.TileAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tile {
  private final List<Player> playersOnTile = new ArrayList<>();
  private int tileId;
  private int nextTileId;
  private Tile nextTile;
  private TileAction landAction;
  private boolean safe;
  private boolean homeEntry;
  private boolean homeColumn;

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

  public void addPlayer(Player player) {
    playersOnTile.add(player);
  }

  public void removePlayer(Player player) {
    playersOnTile.remove(player);
  }
}
