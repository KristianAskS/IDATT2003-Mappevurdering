package edu.ntnu.bidata.idatt.model.logic.action;

import edu.ntnu.bidata.idatt.model.entity.Player;

/**
 * <p>Represents the action of sliding a player down a snake on the board.</p>
 * <p>When performed, the player is moved directly to the specified
 * destination tile which is the snake’s tail.</p>
 *
 * @author Tri Tac Le
 * @since 1.0
 */
public class SnakeAction implements TileAction {
  private int destinationTileId;
  private String description;

  /**
   * Constructs a new SnakeAction.
   *
   * @param destinationTileId the id of the tile to move the player to
   * @param description       a description for this action
   */
  public SnakeAction(int destinationTileId, String description) {
    this.destinationTileId = destinationTileId;
    this.description = description;
  }

  /**
   * {@inheritDoc}
   *
   * @return the id of the destination tile for this snake action
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
   * @return a string describing this snake action
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

  /**
   * {@inheritDoc}
   *
   * <p>Moves the given player to the snake’s destination tile.</p>
   *
   * @param player the {@link Player} to move
   */
  @Override
  public void perform(Player player) {
    player.setCurrentTileId(destinationTileId);
  }
}
