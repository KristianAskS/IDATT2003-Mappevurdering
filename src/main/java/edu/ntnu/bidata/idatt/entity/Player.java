package edu.ntnu.iir.bidata.entity;

/*
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 */


import edu.ntnu.iir.bidata.logic.BoardGame;
import edu.ntnu.iir.bidata.model.Tile;
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
  private String token;


  /**
   * Instantiates a new Player that always starts at tile 0.
   *
   * @param name the name
   */
  public Player(String name) {
    this.name = name;
    this.position = 0;
    this.currentTile = BoardGame.getBoard().getTileId(0);
  }

  /**
   * Gets name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets position.
   *
   * @return the position
   */
  public int getPosition() {
    return position;
  }

  /**
   * Sets position.
   *
   * @param position the position
   */
  public void setPosition(int position) {
    this.position = position;
  }

  /**
   * Set current tile.
   *
   * @param currentTile the current tile
   */
  public void setCurrentTile(Tile currentTile) {
    this.currentTile = currentTile;
  }

  /**
   * Gets token.
   *
   * @return the token
   */
  public String getToken() {
    return token;
  }

  /**
   * Gets current tile.
   *
   * @param token the token
   */
  public void setToken(String token) {
    this.token = token;
  }

  /**
   * Place on tile.
   *
   * @param tile the tile
   */
  public void placeOnTile(Tile tile) {
    if (currentTile != null) {
      currentTile.leavePlayer(this);
    }
    currentTile = tile;
    tile.landPlayer(this);
  }

  /**
   * Move. ikke bruk system.out.println, er placeholder for logger
   *
   * @param steps the steps
   */
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