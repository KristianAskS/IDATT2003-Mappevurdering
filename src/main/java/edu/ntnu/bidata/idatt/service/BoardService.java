package edu.ntnu.bidata.idatt.service;

import edu.ntnu.bidata.idatt.model.Board;
import edu.ntnu.bidata.idatt.utils.io.BoardFileHandler;
import edu.ntnu.bidata.idatt.utils.io.GsonBoardFileHandler;
import java.io.IOException;
import java.util.logging.Logger;

public class BoardService {
  private static final Logger logger = Logger.getLogger(BoardService.class.getName());
  private Board board;
  private BoardFileHandler boardFileHandler = new GsonBoardFileHandler();

  /**
   * Reads a board from the specified file.
   *
   * @param filePath the path to the JSON file containing the board configuration
   * @return the loaded Board object
   * @throws IOException if reading from the file fails
   */
  public Board readBoardFromFile(String filePath) throws IOException {
    board = boardFileHandler.readBoard(filePath);
    return board;
  }

  /**
   * Writes the current board configuration to the specified file.
   *
   * @param filePath the path to the JSON file where the board should be saved
   * @throws IOException if writing to the file fails
   */
  public void writeBoardToFile(String filePath) throws IOException {
    if (board == null) {
      throw new IllegalStateException("No board to write");
    }
    boardFileHandler.writeBoard(board, filePath);
  }

  /**
   * Gets the board used by the service.
   *
   * @return the board used by the service
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Sets the board to be used by the service.
   *
   * @param board the board to be used
   */
  public void setBoard(Board board) {
    this.board = board;
  }
}
