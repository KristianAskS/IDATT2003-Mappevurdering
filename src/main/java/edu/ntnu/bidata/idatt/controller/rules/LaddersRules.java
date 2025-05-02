// src/main/java/edu/ntnu/bidata/idatt/rules/LaddersRules.java

package edu.ntnu.bidata.idatt.controller.rules;

import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Tile;

public final class LaddersRules implements GameRules {

  @Override
  public boolean canEnterTrack(Player player, int rolled) {
    return true;
  }

  @Override
  public int destinationTile(Player player, int rolled, int max) {
    return Math.min(player.getCurrentTileId() + rolled, max);
  }

  @Override
  public void onLand(Player player, Tile landed) {
    if (landed.getLandAction() != null) {
      landed.getLandAction().perform(player);
    }
  }
}
