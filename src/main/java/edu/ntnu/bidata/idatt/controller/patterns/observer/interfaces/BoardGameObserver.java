package edu.ntnu.bidata.idatt.controller.patterns.observer.interfaces;

import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEvent;

/**
 * Interface for observing board game events.
 */
public interface BoardGameObserver {

  void onEvent(BoardGameEvent eventType);
}
