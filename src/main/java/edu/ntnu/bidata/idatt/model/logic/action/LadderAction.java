package edu.ntnu.bidata.idatt.model.logic.action;

import edu.ntnu.bidata.idatt.model.entity.Player;

/**
 * <p>Represents the action of climbing a ladder on the board.</p>
 * <p>When performed, the player is moved directly to the specified
 * destination tile at the top of the ladder.</p>
 *
 * @author Tri Tac Le
 * @since 1.0
 */
public class LadderAction implements TileAction {
  private int destinationTileId;
  private String description;

  /**
   * Constructs a new LadderAction.
   *
   * @param destinationTileId the id of the tile to move the player to
   * @param description       a description
   */
  public LadderAction(int destinationTileId, String description) {
    this.destinationTileId = destinationTileId;
    this.description = description;
  }

  /**
   * {@inheritDoc}
   *
   * <p>Moves the given player to the ladder’s destination tile.</p>
   *
   * @param player the {@link Player} to move
   */
  @Override
  public void perform(Player player) {
    player.setCurrentTileId(destinationTileId);
  }

  /**
   * {@inheritDoc}
   *
   * @return the id of the destination tile for this ladder
   */
  @Override
  public int getDestinationTileId() {
    return destinationTileId;
  }

  /**
   * {@inheritDoc}
   *
   * @param destinationTileId the new destination tile id to set
   */
  @Override
  public void setDestinationTileId(int destinationTileId) {
    this.destinationTileId = destinationTileId;
  }

  /**
   * {@inheritDoc}
   *
   * @return the description of this ladder action
   */
  @Override
  public String description() {
    return description;
  }

  /**
   * {@inheritDoc}
   *
   * @param description the description to set for this action
   */
  @Override
  public void setDescription(String description) {
    this.description = description;
  }
}
