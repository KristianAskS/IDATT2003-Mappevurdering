package com.ntnu.idatt2003.logic;

import com.ntnu.idatt2003.model.Board;
import com.ntnu.idatt2003.model.Dice;
import com.ntnu.idatt2003.entity.Player;
import com.ntnu.idatt2003.model.Tile;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Board game.
 */
public class BoardGame {
  
  private final List<Player> players = new ArrayList<>();
  private Board board;
  private Player currentPlayer;
  private Dice dice;
  
  /**
   * Add player.
   *
   * @param player the player
   */
  public void addPlayer(Player player) {
    players.add(player);
  }
  
  /**
   * Create board.
   *
   * @param numbOfTiles the number of tiles
   */
  public void createBoard(int numbOfTiles) {
    board = new Board();
    
    //Legger tiles
    for (int i = 0; i < numbOfTiles; i++) {
      Tile tile = new Tile(i);
      board.addTile(tile);
    }
    
  }
  
  /**
   * Create dice.
   *
   * @param numbOfDice the number of dice
   */
  public void createDice(int numbOfDice) {
    dice = new Dice(numbOfDice);
  }
  
  /**
   * Play.
   */
  public void play() {
    for (Player p : players) {
      currentPlayer = p;
      int roll = dice.roll();
      p.move(roll);
      
      if (getWinner() != null) {
        System.out.println("We have a winner: " + p.getName()); //fjern sysout, bruk logger
        break;
      }
    }
  }
  
  /**
   * Gets winner.
   *
   * @return the winner
   */
  public Player getWinner() {
    //Implementer ønsket logikk for å sjekke om en spiller har vunnet
    //F.eks. hvis tileID > siste tile eller lignende
    return null;
  }
  
  /**
   * Gets board.
   *
   * @return the board
   */
  public Board getBoard() {
    return board;
  }
  
  /**
   * Gets dice.
   *
   * @return the dice
   */
  public Dice getDice() {
    return dice;
  }
  
  /**
   * Gets current player.
   *
   * @return the current player
   */
  public Player getCurrentPlayer() {
    return currentPlayer;
  }
}
