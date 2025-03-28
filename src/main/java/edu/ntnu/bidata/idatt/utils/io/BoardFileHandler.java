package edu.ntnu.bidata.idatt.utils.io;

import edu.ntnu.bidata.idatt.model.Board;
import java.io.IOException;

public interface BoardFileHandler {
  Board readBoard(String filePath) throws IOException;

  void writeBoard(Board board, String filePath) throws IOException;
}
