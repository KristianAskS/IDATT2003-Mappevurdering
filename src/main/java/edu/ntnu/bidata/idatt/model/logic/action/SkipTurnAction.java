package edu.ntnu.bidata.idatt.model.logic.action;

import edu.ntnu.bidata.idatt.model.entity.Player;

public class SkipTurnAction implements TileAction {

  private final int turnsToSkip;
  private final String description;

  public SkipTurnAction(int turnsToSkip, String description) {
    this.turnsToSkip = Math.max(1, turnsToSkip);
    this.description = description;
  }

  public int getTurnsToSkip() {
    return turnsToSkip;
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
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String desc) {

  }
}
