package edu.ntnu.bidata.idatt.service;

import edu.ntnu.bidata.idatt.entity.Player;
import edu.ntnu.bidata.idatt.utils.io.CsvPlayerFileHandler;
import edu.ntnu.bidata.idatt.utils.io.FileHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerService {
  private final FileHandler<Player> playerFileHandler = new CsvPlayerFileHandler();
  private List<Player> players = new ArrayList<>();
  public void writePlayersToFile(String filePath) throws IOException {
    playerFileHandler.writeToFile(players, filePath);
  }

  public List<Player> readPlayersFromFile(String filePath) throws IOException {
    List<Player> readPlayers = playerFileHandler.readFromFile(filePath);
    this.players.clear();
    this.players.addAll(readPlayers);
    players = playerFileHandler.readFromFile(filePath);
    return players;
  }

  public List<Player> getPlayers() {
    return players;
  }

  public void setPlayers(List<Player> players) {
    this.players = players;
  }
}

