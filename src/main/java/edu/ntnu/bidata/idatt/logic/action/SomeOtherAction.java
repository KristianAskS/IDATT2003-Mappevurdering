package edu.ntnu.bidata.idatt.logic.action;

import edu.ntnu.bidata.idatt.entity.Player;

/**
 * placeholder for other actions that a player must do if landed on the tile containing the action
 */
public class SomeOtherAction implements TileAction {

  @Override
  public int getDestinationTileId() {
    return 0;
  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public void setDestinationTileId(int destinationTileId) {

  }

  @Override
  public void setDescription(String description) {

  }

  public void perform(Player player) {

  }
}
