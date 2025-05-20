package edu.ntnu.bidata.idatt.utils;

import edu.ntnu.bidata.idatt.controller.patterns.factory.PlayerFactory;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.service.PlayerService;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CsvFilePlayerPopulator {

  private final static Logger logger = Logger.getLogger(CsvFilePlayerPopulator.class.getName());
  private static final PlayerService playerService = new PlayerService();

  public static void main(String[] args) {
    List<Player> players = PlayerFactory.createPlayersDummies();
    playerService.setPlayers(players);

    try {
      playerService.writePlayersToFile(PlayerService.PLAYER_FILE_PATH);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Error writing players to file: " + e.getMessage());
    }
  }
}
