package edu.ntnu.bidata.idatt.service;

import edu.ntnu.bidata.idatt.entity.Player;
import edu.ntnu.bidata.idatt.utils.io.CsvPlayerFileHandler;
import edu.ntnu.bidata.idatt.utils.io.FileHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerService {
  private List<Player> players = new ArrayList<>();
  private final FileHandler<Player> playerFileHandler = new CsvPlayerFileHandler();

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
}

