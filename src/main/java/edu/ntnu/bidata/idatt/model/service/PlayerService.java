package edu.ntnu.bidata.idatt.model.service;

import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.utils.io.CsvPlayerFileHandler;
import edu.ntnu.bidata.idatt.utils.io.FileHandler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Player service for loading and saving player models.
 */
public class PlayerService {

  public static final String PLAYER_FILE_PATH = "data/players.csv";
  private final FileHandler<Player> playerFileHandler = new CsvPlayerFileHandler();
  private List<Player> players = new ArrayList<>();


  /**
   * Writes the players to a file.
   *
   * @param filePath the path to the file
   * @throws IOException if an I/O error occurs
   */
  public void writePlayersToFile(String filePath) throws IOException {
    playerFileHandler.writeToFile(players, filePath);
  }

  /**
   * Reads players from a file.
   *
   * @param filePath the path to the file
   * @return a list of players
   * @throws IOException if an I/O error occurs
   */
  public List<Player> readPlayersFromFile(String filePath) throws IOException {
    List<Player> readPlayers = playerFileHandler.readFromFile(filePath);
    this.players.clear();
    this.players.addAll(readPlayers);
    return readPlayers;
  }

  /**
   * Returns the list of players.
   *
   * @return the list of players
   */
  public List<Player> getPlayers() {
    return players;
  }

  /**
   * Sets the list of players.
   *
   * @param players the list of players
   */
  public void setPlayers(List<Player> players) {
    this.players = players;
  }

  /**
   * Adds players to the list.
   *
   * @param newPlayers the players to add
   */
  public void addPlayers(List<Player> newPlayers) {
    try {
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

  /**
   * Adds a player to the list.
   *
   * @param newPlayer the player to add
   */
  public void addPlayer(Player newPlayer) {
  }
}

