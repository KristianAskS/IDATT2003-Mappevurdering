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
 * Data persistence
 */
public class BoardService {
  private static final Logger logger = Logger.getLogger(BoardService.class.getName());
  private static final String BOARD_DIR = "data/games/";
  private final FileHandler<Board> boardFileHandler = new GsonBoardFileHandler();
  private Board board;
  private List<Board> boards = new ArrayList<>();

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
  }
}
