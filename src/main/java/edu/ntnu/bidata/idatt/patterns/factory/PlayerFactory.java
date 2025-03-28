package edu.ntnu.bidata.idatt.patterns.factory;

import edu.ntnu.bidata.idatt.entity.Player;
import edu.ntnu.bidata.idatt.utils.io.CsvPlayerFileHandler;
import edu.ntnu.bidata.idatt.utils.io.FileHandler;
import java.io.IOException;
import java.util.List;

public class PlayerFactory {
  private PlayerFactory() {
    throw new IllegalStateException("Utility class");
  }

  public static List<Player> createPlayerFromCsvFile(String filePath) throws IOException {
    if (filePath == null || filePath.isBlank()) {
      throw new IllegalArgumentException("File path cannot be null");
    }
    FileHandler<Player> playerFileHandler = new CsvPlayerFileHandler();
    return playerFileHandler.readFromFile(filePath);
  }

  public static Player createPlayer(String name) {
    if (name == null || name.isBlank()) {
      throw new IllegalArgumentException("Name cannot be null");
    }
    return new Player(name);
  }
}