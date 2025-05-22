package edu.ntnu.bidata.idatt.model.logic.action;

import edu.ntnu.bidata.idatt.model.entity.Player;

/**
 * <p>Action for a player to skip a specified number of turns.</p>
 *
 * <p>Implements {@link TileAction}, but does not move the player to another tile;
 *
 * @author Tri Tac Le
 * @since 1.0
 */
public record SkipTurnAction(int turnsToSkip, String description) implements TileAction {

  /**
   * Constructs a SkipTurnAction, ensuring at least one turn is skipped.
   *
   * @param turnsToSkip the number of turns to skip
   * @param description a description of this skip turn action
   */
  public SkipTurnAction {
    turnsToSkip = Math.max(1, turnsToSkip);
    description = description;
  }

  /**
   * {@inheritDoc}
   *
   * <p>Records the skip turn instruction for the player.</p>
   *
   * @param player the {@link Player} who will skip turns
   */
  @Override
  public void perform(Player player) {
  }

  /**
   * {@inheritDoc}
   *
   * @return always 0, because this action does not change the player’s tile
   */
  @Override
  public int getDestinationTileId() {
    return 0;
  }

  /**
   * {@inheritDoc}
   *
   * <p>No-op since destination is not relevant.</p>
   *
   * @param id ignored
   */
  @Override
  public void setDestinationTileId(int id) {
  }

  /**
   * {@inheritDoc}
   *
   * <p>No-op since the description is set via the record’s constructor.</p>
   *
   * @param desc ignored
   */
  @Override
  public void setDescription(String desc) {
  }
}
