package edu.ntnu.bidata.idatt.entity;

/*
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 */


import edu.ntnu.bidata.idatt.model.Tile;
import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Player.
 */
public class Player {
  private final String name;
  Logger logger = Logger.getLogger(Player.class.getName());
  private Tile currentTile;
  private int position;
  private TokenView token;

  public Player(String name) {
    this.name = name;
    this.position = 0;
  }

  public String getName() {
    return name;
  }

  public int getPosition() {
    return position;
  }

  public void setCurrentTile(Tile currentTile) {
    this.currentTile = currentTile;
  }

  public TokenView getToken() {
    return token;
  }

  public void setToken(TokenView token) {
    this.token = token;
  }

  public void placeOnTile(Tile tile) {
    if (currentTile != null) {
      currentTile.leavePlayer(this);
    }
    currentTile = tile;
    tile.landPlayer(this);
  }

  public void move(int steps) {
    logger.log(Level.INFO, "Player " + name + " moves " + steps + " steps");
    Tile tile = currentTile;

    for (int i = 0; i < steps; i++) {
      if (tile.getNextTile() != null) {
        tile = tile.getNextTile();
        placeOnTile(tile);
        this.position = tile.getTileId();
      } else {
        logger
            .log(Level.INFO, "Player " + name + " has reached the end of the board");
        break;
      }
    }
    currentTile = tile;
  }
}