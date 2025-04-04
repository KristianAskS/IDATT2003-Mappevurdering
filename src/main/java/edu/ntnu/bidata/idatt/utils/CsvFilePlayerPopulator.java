package edu.ntnu.bidata.idatt.utils;

import edu.ntnu.bidata.idatt.controller.patterns.factory.PlayerFactory;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.service.PlayerService;
import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;

public class CsvFilePlayerPopulator {
  private final static Logger logger = Logger.getLogger(CsvFilePlayerPopulator.class.getName());
  private static final PlayerService playerService = new PlayerService();

  public static void main(String[] args) {
    List<Player> players = PlayerFactory.createPlayersDummies();
    players.add(new Player("Tri", new TokenView(Color.BLUE, "Circle")));
    players.add(new Player("Kristian", new TokenView(Color.RED, "Square")));
    players.add(new Player("Neymar", new TokenView(Color.GREEN, "Triangle")));

    playerService.setPlayers(players);

    try {
      playerService.writePlayersToFile(PlayerService.PLAYER_FILE_PATH);
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Error writing players to file: " + e.getMessage());
    }
  }
}
