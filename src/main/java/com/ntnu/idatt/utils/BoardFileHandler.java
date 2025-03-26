package com.ntnu.idatt.utils;

import java.io.IOException;
import com.ntnu.idatt.model.Board;
import java.nio.file.Path;

public interface BoardFileHandler {
  Board readBoard(Path filePath) throws IOException;
  void writeBoard(Board board, Path filePath) throws IOException;
}
