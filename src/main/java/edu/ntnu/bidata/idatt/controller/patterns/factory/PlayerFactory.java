package edu.ntnu.bidata.idatt.controller.patterns.factory;

import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.utils.io.CsvPlayerFileHandler;
import edu.ntnu.bidata.idatt.utils.io.FileHandler;
import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

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

  public static Player createPlayer(String name, TokenView token) {
    if (name == null || name.isBlank() || token == null) {
      throw new IllegalArgumentException("Name and token cannot be null");
    }
    return new Player(name, token);
  }

  public static List<Player> createPlayersDummies() {
    List<Player> players = new ArrayList<>();
    players.add(PlayerFactory.createPlayer("Player 1", new TokenView(Color.DARKCYAN, "circle")));
    players.add(PlayerFactory.createPlayer("Player 2", new TokenView(Color.GAINSBORO, "circle")));
    players.add(
        PlayerFactory.createPlayer("Player 3", new TokenView(Color.SPRINGGREEN, "triangle")));
    players.add(PlayerFactory.createPlayer("Player 4", new TokenView(Color.HOTPINK, "square")));
    players.add(PlayerFactory.createPlayer("Player 5", new TokenView(Color.MAGENTA, "triangle")));
    return players;
  }
}