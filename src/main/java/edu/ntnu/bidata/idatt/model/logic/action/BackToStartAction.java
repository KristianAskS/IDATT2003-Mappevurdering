package edu.ntnu.bidata.idatt.model.logic.action;

import edu.ntnu.bidata.idatt.model.entity.Player;

public record BackToStartAction(String description) implements TileAction {

  @Override
  public void perform(Player player) {
    player.setCurrentTileId(0);
  }

  @Override
  public int getDestinationTileId() {
    return 0;
  }

  @Override
  public void setDestinationTileId(int id) {

  }

  @Override
  public String getDescription() {
    return "Back to start";
  }

  @Override
  public void setDescription(String d) {

  }
}
