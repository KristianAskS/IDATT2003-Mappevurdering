package com.ntnu.idatt2003.tile.action;

import com.ntnu.idatt2003.core.Player;

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
