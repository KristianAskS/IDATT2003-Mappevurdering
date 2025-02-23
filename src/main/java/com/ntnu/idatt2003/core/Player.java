package com.ntnu.idatt2003.core;

/*
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 */


import com.ntnu.idatt2003.tile.Tile;

/**
 * The type Player.
 */
public class Player {
  private final String name;
  private Tile currentTile;
  private BoardGame game;
  private int position;
  
  /**
   * Instantiates a new Player.
   *
   * @param name the name
   * @param game the game
   */
  public Player(String name, BoardGame game) {
    this.name = name;
    this.game = game;
    this.position = 0;
  }
  
  /**
   * Gets name.
   *
   * @return the name
   */
  public String getName() { return name; }
  
  /**
   * Gets position.
   *
   * @return the position
   */
  public int getPosition() { return position; }
  
  /**
   * Sets position.
   *
   * @param position the position
   */
  public void setPosition(int position) { this.position = position; }
  
  /**
   * Set current tile.
   *
   * @param currentTile the current tile
   */
  public void setCurrentTile(Tile currentTile){
    this.currentTile = currentTile;
  }
  
  /**
   * Place on tile.
   *
   * @param tile the tile
   */
  public void placeOnTile(Tile tile) {
    if (currentTile!=null){
      currentTile.leavePlayer(this);
    }
    currentTile = tile;
    tile.landPlayer(this);
  }
  
  /**
   * Move.
   *
   * @param steps the steps
   */
  public void move(int steps) {
    System.out.println("Player " + name + " moves " + steps + " steps"); //ikke bruk system.out.println, er placeholder for logger
    if (currentTile == null){
      return;
    }
    Tile tile = currentTile;
    
    for (int i = 0; i < steps; i++) {
      if(tile.getNextTile() != null){
        currentTile = currentTile.getNextTile();
      }else {
        System.out.println("Player " + name + " has reached the end of the board");//ikke bruk system.out.println, er placeholder for logger
        break;
      }
      placeOnTile(tile);
    }
  }
}