package com.ntnu.idatt2003.model;

import java.util.ArrayList;
import java.util.List;

public class BoardGame {
  
  private Board board;
  private Player currentPlayer;
  private final List<Player> players = new ArrayList<>();
  private Dice dice;
  
  public void addPlayer(Player player) {
    players.add(player);
  }
  
  public void createBoard() {
    board = new Board(); //antall tiles
    
    //Legger til 100 tiles
    for(int i = 0; i < 100; i++) {
      Tile tile = new Tile(i);
      board.addTile(tile);
    }
    
  }
  
  public void createDice() {
    dice = new Dice(1);
  }
  
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
  
  public Player getWinner() {
    //Implementer ønsket logikk for å sjekke om en spiller har vunnet
    //F.eks. hvis tileID > siste tile eller lignende
    return null;
  }
  
  public Board getBoard() {
    return board;
  }
  
  public Dice getDice() {
    return dice;
  }
  
  public Player getCurrentPlayer() {
    return currentPlayer;
  }
}
