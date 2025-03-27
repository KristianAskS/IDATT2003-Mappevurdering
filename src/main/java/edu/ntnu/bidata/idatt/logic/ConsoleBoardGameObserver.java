package edu.ntnu.bidata.idatt.logic;

import edu.ntnu.bidata.idatt.entity.Player;
import edu.ntnu.bidata.idatt.model.Tile;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsoleBoardGameObserver implements BoardGameObserver {
  private static final Logger logger = Logger.getLogger(ConsoleBoardGameObserver.class.getName());

  @Override
  public void onPlayerMoved(Player player, Tile oldTile, Tile newTile) {
    logger.log(
        Level.INFO,
        player.getName() + " moved from " + oldTile.getTileId() + " to " + newTile.getTileId());
  }

  @Override
  public void onGameFinished(Player player) {
    logger.log(Level.INFO, player.getName() + " won the game!");
  }
}
