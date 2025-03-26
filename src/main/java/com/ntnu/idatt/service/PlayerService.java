package com.ntnu.idatt.service;

import com.ntnu.idatt.entity.Player;
import com.ntnu.idatt.utils.CsvPlayerFileHandler;
import com.ntnu.idatt.utils.PlayerFileHandler;
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
