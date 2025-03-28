package edu.ntnu.bidata.idatt.service;

import edu.ntnu.bidata.idatt.model.Board;
import edu.ntnu.bidata.idatt.utils.io.FileHandler;
import edu.ntnu.bidata.idatt.utils.io.GsonBoardFileHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BoardService {
  private static final Logger logger = Logger.getLogger(BoardService.class.getName());
  private final FileHandler<Board> boardFileHandler = new GsonBoardFileHandler();
  private Board board;
  private List<Board> boards;

  /**
   * Reads a board from the specified file.
   *
   * @param filePath the path to the JSON file containing the board configuration
   * @return the loaded Board object
   * @throws IOException if reading from the file fails
   */
  public List<Board> readBoardFromFile(String filePath) throws IOException {
    return boardFileHandler.readFromFile(filePath);
  }

  /**
   * Writes the current board configuration to the specified file.
   *
   * @param filePath the path to the JSON file where the board should be saved
   * @throws IOException if writing to the file fails
   */
  public void writeBoardToFile(List<Board> boards, String filePath) throws IOException {
    if (boards.isEmpty()) {
      throw new IllegalStateException("No board to write");
    }
    boardFileHandler.writeToFile(boards, filePath);
  }

  public Board getBoard() {
    return board;
  }

  public void setBoard(Board board) {
    this.board = board;
  }

  public List<Board> getBoards() {
    if (boards == null) {
      boards = new ArrayList<>();
    }
    return boards;
  } // TODO: Remove <Board>
}
