package edu.ntnu.bidata.idatt.utils;

import edu.ntnu.bidata.idatt.controller.patterns.factory.PlayerFactory;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.service.PlayerService;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <p>Utility class to generate players and add them
 * to the CSV file via {@link PlayerService}.</p>
 *
 * <p>Uses {@link PlayerFactory#createPlayersDummies()} to make a list
 * of dummy players, then writes them to the path defined by
 * {@link PlayerService#PLAYER_FILE_PATH}.</p>
 *
 * @author Tri Tac Le
 * @since 1.0
 */
public class CsvFilePlayerPopulator {
  private static final Logger logger =
      Logger.getLogger(CsvFilePlayerPopulator.class.getName());
  private static final PlayerService playerService = new PlayerService();

  /**
   * <p>Main entry point. Creates dummy players and writes them
   * to the CSV file specified by {@link PlayerService#PLAYER_FILE_PATH}.</p>
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    List<Player> players = PlayerFactory.createPlayersDummies();
    playerService.setPlayers(players);

    try {
      playerService.writePlayersToFile(PlayerService.PLAYER_FILE_PATH);
      logger.log(Level.INFO, "Dummy players written to file successfully.");
    } catch (IOException e) {
      logger.log(Level.SEVERE,
          "Error writing players to file: {0}", e.getMessage());
    }
  }
}
