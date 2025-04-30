package edu.ntnu.bidata.idatt.controller.rules;

import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Tile;

public final class LudoRules implements GameRules {

  private static final int[] SAFE = {1, 9, 14, 22, 27, 35, 40, 48};

  @Override
  public boolean canEnterTrack(Player player, int rolled) {
    return player.getCurrentTileId() > 0 || rolled == 6;
  }

  @Override
  public int destinationTile(Player player, int rolled, int max) {
    int dest = player.getCurrentTileId() + rolled;
    return (dest > max) ? -1 : dest;
  }

  @Override
  public void onLand(Player player, Tile landingTile) {
    boolean safe = java.util.Arrays.stream(SAFE)
        .anyMatch(tileId -> tileId == landingTile.getTileId());
    if (safe) {
      return;
    }

    landingTile.getPlayersOnTile().stream()
        .filter(other -> other != player)
        .forEach(other -> other.setCurrentTileId(0));
  }
}
