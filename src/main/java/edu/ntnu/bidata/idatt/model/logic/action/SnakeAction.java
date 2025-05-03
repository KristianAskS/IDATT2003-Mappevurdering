package edu.ntnu.bidata.idatt.model.logic.action;

import edu.ntnu.bidata.idatt.model.entity.Player;

public class SnakeAction implements TileAction {

  private int destinationTileId;
  private String description;

  public SnakeAction(int destinationTileId, String description) {
    this.destinationTileId = destinationTileId;
    this.description = description;
  }

  @Override
  public int getDestinationTileId() {
    return destinationTileId;
  }

  @Override
  public void setDestinationTileId(int destinationTileId) {
    this.destinationTileId = destinationTileId;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public void perform(Player player) {
    player.setCurrentTileId(destinationTileId);
  }
}