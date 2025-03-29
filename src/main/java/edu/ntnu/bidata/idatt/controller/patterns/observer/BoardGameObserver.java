package edu.ntnu.bidata.idatt.controller.patterns.observer;

public interface BoardGameObserver {
  void onEvent(BoardGameEvent eventType);
}
