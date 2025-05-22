package edu.ntnu.bidata.idatt.model.logic.action;

import edu.ntnu.bidata.idatt.model.entity.Player;

/**
 * <p>Action that sends a player back to the start tile.</p>
 *
 * <p>Implements {@link TileAction}.</p>
 *
 * @author Tri Tac Le
 * @since 1.0
 */
public record BackToStartAction(String description) implements TileAction {

  /**
   * {@inheritDoc}
   *
   * <p>Sets the player's position to the starting tile.</p>
   *
   * @param player the {@link Player} to move back to start
   */
  @Override
  public void perform(Player player) {
    player.setCurrentTileId(0);
  }

  /**
   * {@inheritDoc}
   *
   * @return 0 since the player is sent to the start tile
   */
  @Override
  public int getDestinationTileId() {
    return 0;
  }

  /**
   * {@inheritDoc}
   *
   * <p>No-op because destination is fixed at 0 and cannot be changed.</p>
   *
   * @param id ignored
   */
  @Override
  public void setDestinationTileId(int id) {
  }

  /**
   * {@inheritDoc}
   *
   * <p>No-op because description is set via the record’s constructor and is immutable.</p>
   *
   * @param description ignored
   */
  @Override
  public void setDescription(String description) {
  }
}
