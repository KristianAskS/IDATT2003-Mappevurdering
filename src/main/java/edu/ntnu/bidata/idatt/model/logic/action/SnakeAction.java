package edu.ntnu.bidata.idatt.model.logic.action;

import edu.ntnu.bidata.idatt.model.entity.Player;

public class SnakeAction implements TileAction {
  private int destinationTileld;
  private String description;

  public SnakeAction(int destinationTileId, String description) {
    this.destinationTileld = destinationTileId;
    this.description = description;
  }

  @Override
  public int getDestinationTileId() {
    return 0;
  }

  @Override
  public void setDestinationTileId(int destinationTileId) {

  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public void setDescription(String description) {

  }

  @Override
  public void perform(Player player) {

  }
}
