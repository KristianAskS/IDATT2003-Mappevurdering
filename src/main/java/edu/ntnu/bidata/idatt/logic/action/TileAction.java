package edu.ntnu.bidata.idatt.logic.action;

import edu.ntnu.bidata.idatt.entity.Player;

public interface TileAction {

  int getDestinationTileId();

  void setDestinationTileId(int destinationTileId);

  String getDescription();

  void setDescription(String description);

  void perform(Player player);
}
