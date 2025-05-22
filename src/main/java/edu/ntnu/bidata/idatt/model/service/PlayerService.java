package edu.ntnu.bidata.idatt.model.service;

import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.utils.io.CsvPlayerFileHandler;
import edu.ntnu.bidata.idatt.utils.io.FileHandler;
import edu.ntnu.bidata.idatt.view.scenes.PlayerSelectionScene;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.scene.paint.Color;

public class PlayerService {

  public static final String PLAYER_FILE_PATH = "data/players.csv";
  private final FileHandler<Player> playerFileHandler = new CsvPlayerFileHandler();
  private List<Player> players = new ArrayList<>();

  /**
   * for loose coupling
   *
   * @return
   */
  public static Color getSelectedColor() {
    return PlayerSelectionScene.getSelectedColor();
  }

  public void writePlayersToFile(String filePath) throws IOException {
    playerFileHandler.writeToFile(players, filePath);
  }

  public List<Player> readPlayersFromFile(String filePath) throws IOException {
    List<Player> readPlayers = playerFileHandler.readFromFile(filePath);
    this.players.clear();
    this.players.addAll(readPlayers);
    return readPlayers;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
  }

  public void addPlayers(List<Player> newPlayers) {
    try {
      // 1) read current file (if it exists) to avoid duplicates by *name*
      List<Player> existing = Files.exists(Path.of(PLAYER_FILE_PATH))
          ? readPlayersFromFile(PLAYER_FILE_PATH)
          : List.of();
      Set<String> names = existing.stream()
          .map(Player::getName)
          .collect(Collectors.toSet());

      List<Player> unique = newPlayers.stream()
          .filter(player -> !names.contains(player.getName()))
          .toList();

      if (unique.isEmpty()) {
        return;
      }
      ((CsvPlayerFileHandler) playerFileHandler)
          .appendToFile(unique, PLAYER_FILE_PATH);

      existing = new ArrayList<>(existing);
      existing.addAll(unique);
      this.players = existing;

    } catch (IOException e) {
      throw new RuntimeException("Could not append players to CSV", e);
    }
  }

  public void addPlayer(Player newPlayer) {
  }
}

