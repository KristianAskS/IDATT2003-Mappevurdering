package edu.ntnu.bidata.idatt.model.logic.action;

import edu.ntnu.bidata.idatt.model.entity.Player;

/**
 * placeholder for other actions that a player must do if landed on the tile containing the action
 */
public class SomeOtherAction implements TileAction {

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

  public void perform(Player player) {

  }
}
