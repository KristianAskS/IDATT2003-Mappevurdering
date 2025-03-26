package edu.ntnu.bidata.idatt.logic;

import edu.ntnu.bidata.idatt.model.Board;
import edu.ntnu.bidata.idatt.utils.PopulateBoard;

public class BoardGameFactory {
  public static Board createBoardGame() {
    Board board = PopulateBoard.createBoard(90);
    return board;
  }
}
