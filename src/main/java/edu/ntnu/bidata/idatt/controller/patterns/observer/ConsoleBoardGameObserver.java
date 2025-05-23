package edu.ntnu.bidata.idatt.controller.patterns.observer;

import edu.ntnu.bidata.idatt.controller.patterns.observer.interfaces.BoardGameObserver;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
  A simple observer that logs all events to the console.
 */
public class ConsoleBoardGameObserver implements BoardGameObserver {

  private static final Logger logger = Logger.getLogger(ConsoleBoardGameObserver.class.getName());

  /**
   * Logs all events to the console.
   *
   * @param eventType the event to log
   */
  @Override
  public void onEvent(BoardGameEvent eventType) {
    switch (eventType.eventType()) {
      case PLAYER_MOVED -> {
        logger.log(Level.INFO, "{0} moved from {1} to {2}",
            new Object[] {eventType.player().getName(), eventType.oldTile().getTileId(),
                eventType.newTile().getTileId()});
        break;
      }
      case GAME_FINISHED -> {
        logger.log(Level.INFO, "{0} won the game!", eventType.player().getName());
        break;
      }
      case PLAYER_FINISHED -> {
        logger.log(Level.INFO, "Player {0} finished", eventType.player().getName());
        break;
      }
      default -> logger.log(Level.INFO, "Unknown event type: {0}", eventType.eventType());
    }
  }
}
