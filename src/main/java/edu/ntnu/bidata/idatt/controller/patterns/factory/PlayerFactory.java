package edu.ntnu.bidata.idatt.controller.patterns.factory;

import static edu.ntnu.bidata.idatt.model.entity.Token.token;

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
    players.add(createPlayer("Lionel Messi", new TokenView(token(Color.LIGHTBLUE, "circle"))));
    players.add(createPlayer("Cristiano Ronaldo", new TokenView(token(Color.CRIMSON, "circle"))));
    players.add(createPlayer("Kylian Mbappé", new TokenView(token(Color.DARKBLUE, "triangle"))));
    players.add(createPlayer("Erling Haaland", new TokenView(token(Color.YELLOW, "square"))));
    players.add(
        createPlayer("Kevin De Bruyne", new TokenView(token(Color.LIGHTSKYBLUE, "triangle"))));
    players.add(createPlayer("Virgil van Dijk", new TokenView(token(Color.DARKGREEN, "square"))));
    players.add(createPlayer("Luka Modrić", new TokenView(token(Color.WHITESMOKE, "circle"))));
    players.add(createPlayer("Neymar Jr", new TokenView(token(Color.DARKORCHID, "triangle"))));
    players.add(
        createPlayer("Robert Lewandowski", new TokenView(token(Color.INDIANRED, "square"))));
    players.add(
        createPlayer("Mohamed Salah", new TokenView(token(Color.MEDIUMVIOLETRED, "triangle"))));
    players.add(createPlayer("Karim Benzema", new TokenView(token(Color.GOLDENROD, "circle"))));
    players.add(createPlayer("Sadio Mané", new TokenView(token(Color.FORESTGREEN, "square"))));
    players.add(
        createPlayer("Heung-min Son", new TokenView(token(Color.CORNFLOWERBLUE, "triangle"))));
    players.add(createPlayer("Toni Kroos", new TokenView(token(Color.SILVER, "circle"))));
    players.add(createPlayer("Pedri", new TokenView(token(Color.HOTPINK, "square"))));
    players.add(createPlayer("Jude Bellingham", new TokenView(token(Color.ROYALBLUE, "triangle"))));

    return players;
  }
}