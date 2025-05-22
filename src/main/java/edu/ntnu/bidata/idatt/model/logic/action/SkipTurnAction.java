package edu.ntnu.bidata.idatt.model.logic.action;

import edu.ntnu.bidata.idatt.model.entity.Player;

public record SkipTurnAction(int turnsToSkip, String description) implements TileAction {

  public SkipTurnAction(int turnsToSkip, String description) {
    this.turnsToSkip = Math.max(1, turnsToSkip);
    this.description = description;
  }

  @Override
  public void perform(Player player) {

  }

  @Override
  public int getDestinationTileId() {
    return -1;
  }

  @Override
  public void setDestinationTileId(int id) {

  }

  @Override
  public void setDescription(String desc) {

  }
}
