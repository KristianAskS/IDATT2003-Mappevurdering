package edu.ntnu.bidata.idatt.model.entity;

import edu.ntnu.bidata.idatt.view.components.TokenView;

/**
 * The type Player.
 */
public class Player {
  private final String name;
  private TokenView token;
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

  public void setTokenView(TokenView token) {
    this.token = token;
  }

  public int getCurrentTileId() {
    return currentTileId;
  }

  public void setCurrentTileId(int currentTileId) {
    this.currentTileId = currentTileId;
  }

  @Override
  public String toString() {
    return name + " " + token.toString();
  }
}