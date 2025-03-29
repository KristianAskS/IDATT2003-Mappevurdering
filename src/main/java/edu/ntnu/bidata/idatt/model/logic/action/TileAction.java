package edu.ntnu.bidata.idatt.model.logic.action;

import edu.ntnu.bidata.idatt.model.entity.Player;

public interface TileAction {

  int getDestinationTileId();

  void setDestinationTileId(int destinationTileId);

  String getDescription();

  void setDescription(String description);

  void perform(Player player);
}
