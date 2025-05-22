package edu.ntnu.bidata.idatt.controller.patterns.observer.interfaces;

import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEvent;

/**
 * Interface
 */
public interface BoardGameObserver {

  void onEvent(BoardGameEvent eventType);
}
