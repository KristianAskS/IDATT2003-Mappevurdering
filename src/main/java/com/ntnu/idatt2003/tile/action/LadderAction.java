package com.ntnu.idatt2003.tile.action;

import com.ntnu.idatt2003.core.Player;

public class LadderAction implements TileAction {
<<<<<<< HEAD

  private int destinationTileld;
  private String description;

=======
  private final int destinationTileld;
  private final String description;
  
>>>>>>> 0b26a96 (Uncommitted changes extern)
  public LadderAction(int destinationTileld, String description) {
    this.destinationTileld = destinationTileld;
    this.description = description;
  }

  @Override
  public void perform(Player player) {

  }
}
