package edu.ntnu.bidata.idatt.controller.patterns.factory;

import edu.ntnu.bidata.idatt.model.entity.Board;

public class LudoBoardFactory extends BoardFactory {

  @Override
  public Board createDefaultBoard() {
    return createBoardTiles("Classic Ludo", "Standard 52-tile board", 52);
  }
}
