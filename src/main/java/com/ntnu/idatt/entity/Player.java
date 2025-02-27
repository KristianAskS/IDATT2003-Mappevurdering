package com.ntnu.idatt.entity;

/*
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 */


import com.ntnu.idatt.logic.BoardGame;
import com.ntnu.idatt.model.Tile;

/**
 * The type Player.
 */
public class Player {
  
  private final String name;
  private Tile currentTile;
  private int position;
  
  /**
   * Instantiates a new Player that always starts at tile 0.
   *
   * @param name the name
   * @param game the game
   */
  public Player(String name, BoardGame game) {
    this.name = name;
    this.position = 0;
    this.currentTile = game.getBoard().getTileId(0);
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
   * Move.
   *ikke bruk system.out.println, er placeholder for logger
   * @param steps the steps
   */
  public void move(int steps) {
    System.out.println("Player " + name + " moves " + steps + " steps");
    Tile tile = currentTile;
    
    for (int i = 0; i < steps; i++) {
      if (tile.getNextTile() != null) {
        tile = tile.getNextTile();
        placeOnTile(tile);
        this.position = tile.getTileId();
      } else {
        System.out.println("Player " + name + " has reached the end of the board");
        break;
      }
    }
    currentTile = tile;
  }
}