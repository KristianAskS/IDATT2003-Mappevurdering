package edu.ntnu.bidata.idatt.model.entity;

import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.util.logging.Logger;

/**
 * The type Player.
 */
public class Player {
  private static final Logger logger = Logger.getLogger(Player.class.getName());
  private final String name;
  private final TokenView token;
  private int currentTileId;

  public Player(String name, TokenView token) {
    this.name = name;
    this.token = token;
  }

  public String getName() {
    return name;
  }

  public TokenView getToken() {
    return token;
  }

  public int getCurrentTileId() {
    return currentTileId;
  }

  public void setCurrentTileId(int currentTileId) {
    this.currentTileId = currentTileId;
  }
}