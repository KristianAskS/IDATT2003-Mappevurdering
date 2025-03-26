package com.ntnu.idatt.utils;

import com.ntnu.idatt.model.Board;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

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
