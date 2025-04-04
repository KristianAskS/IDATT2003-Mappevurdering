package edu.ntnu.bidata.idatt.utils.io;

import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;

/**
 * Class for handling CSV-files containing players
 *
 * @author Trile
 * @version 1.0
 * @since 1.0
 */
public class CsvPlayerFileHandler implements FileHandler<Player> {

  Logger logger = Logger.getLogger(CsvPlayerFileHandler.class.getName());

  /**
   * @param players  List over players
   * @param filePath the path to the CSV-file
   * @throws IOException if writing to the file fails
   */
  @Override
  public void writeToFile(List<Player> players, String filePath) throws IOException {
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))) {
      for (Player player : players) {
        TokenView token = player.getToken();
        String writeLine = player.getName() + "," + toRgbString(token.getTokenColor()) + "," +
            token.getTokenShape();
        bufferedWriter.write(writeLine);
        bufferedWriter.newLine();
        logger.log(Level.INFO, "Player: " + player.getName() + " has been written to the file");
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Error writing to the file: " + e.getMessage());
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
  public List<Player> readFromFile(String filePath) throws IOException {
    List<Player> players = new ArrayList<>();
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = bufferedReader.readLine()) != null) {
        String[] playerData = line.split(",");
        if (playerData.length == 3) {
          String name = playerData[0].trim();
          Color color = Color.web(playerData[1].trim());
          String tokenShape = playerData[2].trim();
          TokenView token = new TokenView(color, tokenShape);
          Player player = new Player(name, token);
          players.add(player);
        }
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Error reading from the file: " + e.getMessage());
    }
    return players;
  }

  private String toRgbString(Color color) {
    int red = (int) (color.getRed() * 255);
    int green = (int) (color.getGreen() * 255);
    int blue = (int) (color.getBlue() * 255);
    int alpha = (int) (color.getOpacity() * 255);
    return String.format("#%02X%02X%02X%02X", red, green, blue, alpha);
  }
}
