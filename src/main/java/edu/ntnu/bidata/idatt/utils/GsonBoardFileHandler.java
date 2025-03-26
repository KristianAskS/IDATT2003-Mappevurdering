package edu.ntnu.iir.bidata.utils;

import edu.ntnu.iir.bidata.model.Board;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class GsonBoardFileHandler implements BoardFileHandler {

  @Override
  public Board readBoard(Path filePath) throws IOException {
    StringBuilder stringBuilder = new StringBuilder();
    try(BufferedReader bufferedReader = Files.newBufferedReader(filePath)){
      String line;
      while((line = bufferedReader.readLine()) != null){
        stringBuilder.append(line).append("\n");
      }
    }
  }

  @Override
  public void writeBoard(Board board, Path filePath) throws IOException {

  }
}
