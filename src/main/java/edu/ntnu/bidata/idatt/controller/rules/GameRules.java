package edu.ntnu.bidata.idatt.controller.rules;

import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Tile;

/**
 * Interface for game rules.
 * Defines the methods that must be implemented by a game rule.
 */
public interface GameRules {

  /**
   * Returns true if the player can enter the track with the given roll.
   *
   * @param player the player to check
   * @param rolled the rolled dice value
   * @return true if the player can enter the track, false otherwise
   */
  boolean canEnterTrack(Player player, int rolled);

  /**
   * Returns the destination tile id for the player after rolling the dice.
   *
   * @param player    the player to check
   * @param rolled    the rolled dice value
   * @param maxTileId the maximum tile id
   * @return the destination tile id
   */
  int destinationTile(Player player, int rolled, int maxTileId);

  /**
   * Called when a player lands on a tile.
   *
   * @param player  the player who landed
   * @param landing the tile they landed on
   */
  void onLand(Player player, Tile landing);
}
