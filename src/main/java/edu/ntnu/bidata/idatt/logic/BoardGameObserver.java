package edu.ntnu.bidata.idatt.logic;

import edu.ntnu.bidata.idatt.entity.Player;
import edu.ntnu.bidata.idatt.model.Tile;

public interface BoardGameObserver {
  void onPlayerMoved(Player player, Tile oldTile, Tile newTile);

  void onGameFinished(Player player);
}
