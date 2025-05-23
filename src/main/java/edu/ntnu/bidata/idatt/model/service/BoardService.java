package edu.ntnu.bidata.idatt.model.service;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.utils.io.FileHandler;
import edu.ntnu.bidata.idatt.utils.io.GsonBoardFileHandler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Board service for loading and saving board models.
 */
public class BoardService {
  private static final Logger logger = Logger.getLogger(BoardService.class.getName());
  private static final String BOARD_DIR = "data/games/";
  private final FileHandler<Board> boardFileHandler = new GsonBoardFileHandler();
  private Board board;
  private List<Board> boards = new ArrayList<>();

  /**
   * Constructs a new BoardService.
   */
  public BoardService() {
    try (Stream<@org.jetbrains.annotations.NotNull Path> files = Files.list(Paths.get(BOARD_DIR))) {
      files.filter(path -> path.toString().endsWith(".json"))
          .forEach(path -> {
            try {
              boards.addAll(boardFileHandler.readFromFile(path.toString()));
            } catch (IOException ioException) {
              logger.log(Level.SEVERE, "Cannot read " + path, ioException);
            }
          });
      logger.log(Level.INFO, "Loaded " + boards.size() + " static boards from " + BOARD_DIR);
    } catch (IOException ioException) {
      logger.log(Level.SEVERE, "Cannot access " + BOARD_DIR, ioException);
    }
  }

  /**
   * Returns the current board.
   *
   * @return the board
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Sets the current board.
   *
   * @param board the board to set
   */
  public void setBoard(Board board) {
    this.board = board;
  }

  /**
   * Returns the board file handler.
   *
   * @return the board file handler
   */
  public FileHandler<Board> getBoardFileHandler() {
    return boardFileHandler;
  }

  /**
   * Returns the list of boards.
   *
   * @return the list of boards
   */
  public List<Board> getBoards() {
    if (boards == null) {
      boards = new ArrayList<>();
    }
    return boards;
  }
}
