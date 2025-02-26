package com.ntnu.idatt;

import com.ntnu.idatt.entity.Player;
import com.ntnu.idatt.logic.BoardGame;
import com.ntnu.idatt.model.Board;
import com.ntnu.idatt.model.Tile;

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
    
    //Opprett spillere
    Player tri = new Player("Tri", boardGame);
    Player kristian = new Player("Kristian", boardGame);
    Player bjornAdam = new Player("Bjorn Adam", boardGame);
    Player hector = new Player("Hector", boardGame);
    
    boardGame.addPlayer(tri);
    boardGame.addPlayer(kristian);
    boardGame.addPlayer(bjornAdam);
    boardGame.addPlayer(hector);
    
    boardGame.getPlayers();
    
    Tile startTile = board.getTileId(0);
    tri.setCurrentTile(startTile);
    kristian.setCurrentTile(startTile);
    bjornAdam.setCurrentTile(startTile);
    hector.setCurrentTile(startTile);
    
    //Spill
    boardGame.play();
    
    //Vinner
    Player winner = boardGame.getWinner();
    System.out.println("The winner is: " + winner.getName());
  }
}
