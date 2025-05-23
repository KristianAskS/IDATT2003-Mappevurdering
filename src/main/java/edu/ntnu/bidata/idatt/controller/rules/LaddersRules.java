package edu.ntnu.bidata.idatt.controller.rules;

import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Tile;

/**
 * Ladders rules for the game.
 * Implements the GameRules interface.
 */
public final class LaddersRules implements GameRules {

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canEnterTrack(Player player, int rolled) {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int destinationTile(Player player, int rolled, int max) {
    return Math.min(player.getCurrentTileId() + rolled, max);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onLand(Player player, Tile landed) {
    if (landed.getLandAction() != null) {
      landed.getLandAction().perform(player);
    }
  }
}
