package com.ntnu.idatt2003.model;

import java.util.List;
import java.util.ArrayList;

public class Game {
  private final Board board;
  private final List<Player> players;
  private int currentPlayerIndex;
  private final Dice dice;
  
  public Game(int numPlayers) {
    this.board = new Board();
    this.players = new ArrayList<>();
    for (int i = 0; i < numPlayers; i++) {
      players.add(new Player("Spiller " + (i + 1)));
    }
    this.dice = new Dice();
    this.currentPlayerIndex = 0;
  }
  
  public void playTurn() {
    Player currentPlayer = players.get(currentPlayerIndex);
    int roll = dice.roll();
    int newPosition = currentPlayer.getPosition() + roll;
    
    if (newPosition >= board.getMaxPosition()) {
      currentPlayer.setPosition(board.getMaxPosition());
      return;
    }
    
    newPosition = board.checkPosition(newPosition);
    currentPlayer.setPosition(newPosition);
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
  }
  
  public List<Player> getPlayers() { return players; }
}