package edu.ntnu.bidata.idatt.logic.action;

import edu.ntnu.bidata.idatt.entity.Player;

public interface TileAction {

  int getDestinationTileId();
  String getDescription();
  void setDestinationTileId(int destinationTileId);
  void setDescription (String description);
  void perform(Player player);
}
