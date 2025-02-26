package com.ntnu.idatt.logic.action;

import com.ntnu.idatt.entity.Player;

public class LadderAction implements TileAction {
  private int destinationTileld;
  private String description;
  
  public LadderAction(int destinationTileld, String description) {
    this.destinationTileld = destinationTileld;
    this.description = description;
  }
  
  @Override
  public void perform(Player player) {
  
  }
}
