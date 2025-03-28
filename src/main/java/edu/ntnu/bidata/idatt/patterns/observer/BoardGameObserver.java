package edu.ntnu.bidata.idatt.patterns.observer;

public interface BoardGameObserver {
  void onEvent(BoardGameEvent eventType);
}
