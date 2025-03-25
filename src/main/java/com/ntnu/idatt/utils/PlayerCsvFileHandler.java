package com.ntnu.idatt.utils;
import com.ntnu.idatt.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for handling CSV-files containing players
 * @version 1.0
 * @since 1.0
 * @author Trile
 */
public class PlayerCsvFileHandler {
  /**
   *
   * @param players List over players
   * @param filepath the path to the CSV-file
   * @throws IOException if writing to the file fails
   */
  public void writePlayersToCsv(List<Player> players, String filepath) throws IOException{
    try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath))){
      for (Player player : players) {
        String writeLine = player.getName() + "," + player.getToken();
        bufferedWriter.write(writeLine);
        bufferedWriter.newLine();
      }
    }
  }

  /**
   * Reads a list of players objects from a CSV-file
   * @param filepath the path to the CSV-file
   * @return a list of players objects
   * @throws IOException if reading from the file fails
   */
  public List<Player> readPlayersFromCsv(String filepath) throws IOException{
    List<Player> players = new ArrayList<>();
    try(BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath))){
      String line;
      while((line = bufferedReader.readLine()) != null){
        String[] playerData = line.split(",");
        if (playerData.length == 2){
          String name = playerData[0];
          String token = playerData[1];
          Player player = new Player(name);
          player.setToken(token);
          players.add(player);
        }
      }
    }
    return players;
  }
}
