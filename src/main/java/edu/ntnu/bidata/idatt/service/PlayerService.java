package edu.ntnu.bidata.idatt.service;

import edu.ntnu.bidata.idatt.entity.Player;
import edu.ntnu.bidata.idatt.utils.CsvPlayerFileHandler;
import edu.ntnu.bidata.idatt.utils.PlayerFileHandler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlayerService {
  private List<Player> players = new ArrayList<>();
  private PlayerFileHandler playerFileHandler = new CsvPlayerFileHandler();

  public void writePlayersToFile(String filePath) throws IOException {
    playerFileHandler.writePlayers(players, filePath);
  }

  public List<Player> readPlayersFromFile(String filePath) throws IOException {
    List<Player> readPlayers = playerFileHandler.readPlayers(filePath);
    this.players.clear();
    this.players.addAll(readPlayers);
    players = playerFileHandler.readPlayers(filePath);
    return players;
  }
}

