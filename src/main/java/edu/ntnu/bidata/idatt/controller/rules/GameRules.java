package edu.ntnu.bidata.idatt.controller.rules;

import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Tile;

public interface GameRules {

  boolean canEnterTrack(Player player, int rolled);

  int destinationTile(Player player, int rolled, int maxTileId);

  void onLand(Player player, Tile landing);
}
