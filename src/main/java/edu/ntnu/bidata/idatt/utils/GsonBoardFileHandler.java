package edu.ntnu.bidata.idatt.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.ntnu.bidata.idatt.model.Board;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GsonBoardFileHandler implements BoardFileHandler {
  Logger logger = Logger.getLogger(GsonBoardFileHandler.class.getName());

  @Override
  public void writeBoard(Board board, String filePath) throws IOException {
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))) {
      Gson gson = new GsonBuilder()
          .setPrettyPrinting()
          .create();
      logger.log(Level.INFO, "Writing board to file: " + filePath);
      gson.toJson(board, bufferedWriter);
    }
  }

  @Override
  public Board readBoard(String filePath) throws IOException {
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
      Gson gson = new Gson();
      logger.log(Level.INFO, "Reading board from file: " + filePath);
      return gson.fromJson(bufferedReader, Board.class);
    }
  }
}
