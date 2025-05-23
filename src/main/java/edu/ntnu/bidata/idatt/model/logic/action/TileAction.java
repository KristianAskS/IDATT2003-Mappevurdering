package edu.ntnu.bidata.idatt.model.logic.action;

import edu.ntnu.bidata.idatt.model.entity.Player;

/**
 * <p>Defines an action that is triggered when a player lands on a tile.</p>
 *
 * @author Tri Tac Le
 * @since 1.0
 */
public interface TileAction {

  /**
   * Returns the id of the tile to which the player will be moved
   * when this action is performed.
   *
   * @return the destination tile id
   */
  int getDestinationTileId();

  /**
   * Sets the id of the tile to which the player will be moved
   * when this action is performed.
   *
   * @param destinationTileId the tile id to set as the action’s target
   */
  @SuppressWarnings("unused")
  void setDestinationTileId(int destinationTileId);

  /**
   * Returns a description of this action.
   *
   * @return a description string
   */
  String description();

  /**
   * Sets the description for this action.
   *
   * @param description the description of this action
   */
  void setDescription(String description);

  /**
   * Executes the action’s logic on the given player
   *
   * @param player the {@link Player} on which to perform the action
   */
  void perform(Player player);
}
