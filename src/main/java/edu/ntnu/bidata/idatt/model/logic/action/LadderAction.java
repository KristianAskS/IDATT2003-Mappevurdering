package edu.ntnu.bidata.idatt.model.logic.action;

import edu.ntnu.bidata.idatt.model.entity.Player;

public class LadderAction implements TileAction {

  private int destinationTileld;
  private String description;

  public LadderAction(int destinationTileld, String description) {
    this.destinationTileld = destinationTileld;
    this.description = description;
  }


  @Override
  public void perform(Player player) {
    player.setCurrentTileId(destinationTileld);
  }


  public int getDestinationTileld() {
    return destinationTileld;
  }

  @Override
  public int getDestinationTileId() {
    return destinationTileld;
  }

  @Override
  public void setDestinationTileId(int destinationTileId) {
    this.destinationTileld = destinationTileId;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }
}
