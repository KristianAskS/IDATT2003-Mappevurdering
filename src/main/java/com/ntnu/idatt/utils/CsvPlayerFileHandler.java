package com.ntnu.idatt.utils;

import com.ntnu.idatt.entity.Player;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for handling CSV-files containing players
 *
 * @author Trile
 * @version 1.0
 * @since 1.0
 */
public class CsvPlayerFileHandler implements PlayerFileHandler {

  Logger logger = Logger.getLogger(CsvPlayerFileHandler.class.getName());

  /**
   * @param players  List over players
   * @param filePath the path to the CSV-file
   * @throws IOException if writing to the file fails
   */
  @Override
  public void writePlayers(List<Player> players, String filePath) throws IOException {
    //String filePath = "/com/ntnu/idatt/players.csv";
    InputStream inputStream = getClass().getResourceAsStream(filePath);
    if (inputStream == null) {
      throw new FileNotFoundException("File not found: " + filePath);
    }
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))) {
      for (Player player : players) {
        String writeLine = player.getName() + "," + player.getToken();
        bufferedWriter.write(writeLine);
        bufferedWriter.newLine();
        logger.log(Level.INFO, "Player: " + player.getName() + " has been written to the file");
      }
    }
  }

  /**
   * Reads a list of players objects from a CSV-file
   *
   * @param filePath the path to the CSV-file
   * @return a list of players objects
   * @throws IOException if reading from the file fails
   */
  @Override
  public List<Player> readPlayers(String filePath) throws IOException {
    //String filepath = "/com/ntnu/idatt/players.csv";
    InputStream inputStream = getClass().getResourceAsStream(filePath);

    if (inputStream == null) {
      throw new FileNotFoundException("File not found: " + filePath);
    }

    List<Player> players = new ArrayList<>();
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        String[] playerData = line.split(",");
        if (playerData.length >= 1) {
          String name = playerData[0].trim();
          Player player = new Player(name);
          if (playerData.length == 2) {
            String token = playerData[1].trim();
            player.setToken(token);
          }
          players.add(player);
        }
      }
    }
    return players;
  }
}
