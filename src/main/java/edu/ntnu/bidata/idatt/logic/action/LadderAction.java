package edu.ntnu.bidata.idatt.logic.action;

import edu.ntnu.bidata.idatt.entity.Player;

public class LadderAction implements TileAction {

  private final int destinationTileld;
  private final String description;

  public LadderAction(int destinationTileld, String description) {
    this.destinationTileld = destinationTileld;
    this.description = description;
  }

  @Override
  public void perform(Player player) {
    player.move(destinationTileld);
  }

  public int getDestinationTileld() {
    return destinationTileld;
  }

  @Override
  public int getDestinationTileId() {
    return 0;
  }

  public String getDescription() {
    return description;
  }

  @Override
  public void setDestinationTileId(int destinationTileId) {

  }

  @Override
  public void setDescription(String description) {

  }
}
