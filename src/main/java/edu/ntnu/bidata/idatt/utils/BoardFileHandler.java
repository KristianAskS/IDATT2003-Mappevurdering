package edu.ntnu.iir.bidata.utils;

import java.io.IOException;
import edu.ntnu.iir.bidata.model.Board;
import java.nio.file.Path;

public interface BoardFileHandler {
  Board readBoard(Path filePath) throws IOException;
  void writeBoard(Board board, Path filePath) throws IOException;
}
