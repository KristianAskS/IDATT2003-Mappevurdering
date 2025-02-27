package com.ntnu.idatt;

import com.ntnu.idatt.entity.Player;
import com.ntnu.idatt.logic.BoardGame;
import com.ntnu.idatt.model.Board;
import com.ntnu.idatt.model.Tile;
import java.util.Scanner;

/**
 * Main class for the board game application
 * This class is the entry point for the application
 * It creates a new board game and sets up the game
 * with the number of tiles, dice and players
 *
 * Represent part 1 of the assignment (Mappevurdering)
 * Can use switch case, but now I just add elements from the source code
 *
 * @version 1.0
 * @author TriLe
 * @since 2025-02-27
 */
public class BoardGameApp {
  public static void main(String[] args) {
    //Opprett nytt spill
    BoardGame boardGame = new BoardGame();
    
    //antall ruter
    int numberOfTiles = 90;
    boardGame.createBoard(numberOfTiles);
    
    //antall terninger
    int numberOfDice = 3;
    boardGame.createDice(numberOfDice);
    
    //koble sammen feltene
    Board board = boardGame.getBoard();
    for (int i = 0; i < numberOfTiles; i++) {
      Tile currentTile = board.getTileId(i);
      Tile nextTile = board.getTileId(i + 1);
      if (currentTile != null && nextTile != null) {
        currentTile.setNextTile(nextTile);
      }
    }
    
    Scanner scanner = new Scanner(System.in);
    int numberOfPlayers = 0;
    
    try{
      System.out.println("Enter the number of players: ");
      
      if (!scanner.hasNextInt()) {
        throw new IllegalArgumentException("Please enter a number type.");
      }
      
      numberOfPlayers = scanner.nextInt();
      
      if (numberOfPlayers < 1){
        throw new IllegalArgumentException("Please enter a number greater than 1");
      }
      
      if (numberOfPlayers > 5){
        throw new IllegalArgumentException("Please enter a number less than 5");
      }
    } catch (IllegalArgumentException e) {
      System.out.println("error: " + e.getMessage());
      System.exit(1);
    }
    
    for (int i = 0; i < numberOfPlayers; i++) {
      System.out.println("Enter the name of player " + (i + 1) + ": ");
      String playerName = scanner.next();
      Player player = new Player(playerName, boardGame);
      boardGame.addPlayer(player);
      
      Tile startTile = board.getTileId(0);
      player.setCurrentTile(startTile);
    }
    boardGame.getPlayers();
    
    
    
    /*
    Player tri = new Player("Tri", boardGame);
    Player kristian = new Player("Kristian", boardGame);
    Player bjornAdam = new Player("Bjorn Adam", boardGame);
    Player hector = new Player("Hector", boardGame);
    
    boardGame.addPlayer(tri);
    boardGame.addPlayer(kristian);
    boardGame.addPlayer(bjornAdam);
    boardGame.addPlayer(hector);
    */

    //Spill
    boardGame.play();
    
    //Vinner
    Player winner = boardGame.getWinner();
    System.out.println("The winner is: " + winner.getName());
  }
}
