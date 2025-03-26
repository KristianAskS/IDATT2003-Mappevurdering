package edu.ntnu.bidata.idatt;

import edu.ntnu.bidata.idatt.entity.Player;
import edu.ntnu.bidata.idatt.logic.BoardGame;
import edu.ntnu.bidata.idatt.model.Board;
import edu.ntnu.bidata.idatt.model.Tile;
import edu.ntnu.bidata.idatt.service.BoardService;
import edu.ntnu.bidata.idatt.service.PlayerService;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Main class for the board game application This class is the entry point for the application It
 * creates a new board game and sets up the game with the number of tiles, dice and players
 * <p>
 * Represent part 1 of the assignment (Mappevurdering) Can use switch case, but now I just add
 * elements from the source code
 *
 * @author TriLe
 * @version 1.0
 * @since 2025-02-27
 */
public class BoardGameApp {
  private static final String filePathPlayers = "/data/players.csv";
  private static final String filePathLaddersAndSnakes = "/data/games/laddersAndSnakes.json";
  private static final Scanner scanner = new Scanner(System.in);
  private static final PlayerService playerService = new PlayerService();
  private static final Logger logger = Logger.getLogger(BoardGameApp.class.getName());

  /**
   * Main method for the board game application
   *
   * @param args the input arguments
   */
  public static void main(String[] args) {
    System.out.println("Do you want to load the board configuration from a JSON file? (yes/no)");
    String choiceBoardJson = scanner.nextLine().trim();

    BoardGame boardGame = new BoardGame();
    int numberOfTiles = 90;
    Board board = null;

    if (choiceBoardJson.equalsIgnoreCase("yes")) {
      BoardService boardService = new BoardService();
      try {
        board = boardService.readBoardFromFile(filePathLaddersAndSnakes);
        logger.log(Level.INFO, "Board loaded from the file: " + filePathLaddersAndSnakes);
      } catch (IOException e) {
        logger.log(Level.SEVERE, "Error reading from the file: " + e.getMessage());
      }
    }

    if(board == null){
      boardGame.createBoard(numberOfTiles);
      board = BoardGame.getBoard();
    }

    boardGame.createBoard(numberOfTiles);

    int numberOfDice = 3;
    boardGame.createDice(numberOfDice);

    for (int i = 0; i < numberOfTiles; i++) {
      Tile currentTile = board.getTileId(i);
      Tile nextTile = board.getTileId(i + 1);
      if (currentTile != null && nextTile != null) {
        currentTile.setNextTile(nextTile);
      }
    }

    System.out.println("Do you want to load players from the csv file? (yes/no)");
    String choice = scanner.nextLine().trim();

    if (choice.equalsIgnoreCase("yes")) {
      try {
        List<Player> players = playerService.readPlayersFromFile(filePathPlayers);
        for (Player player : players) {
          boardGame.addPlayer(player);
          Tile startTile = board.getTileId(0);
          player.setCurrentTile(startTile);
        }
        logger.log(logger.getLevel(), "Players loaded: " + players + " from the file: " + filePathPlayers +
            " .Size:" + players.size());
        logger.log(Level.FINE, "Players loaded: " + players + " from the file: " + filePathPlayers +
            " .Size:" + players.size());
      } catch (IOException e) {
        logger.log(Level.SEVERE, "Error reading from the file: " + e.getMessage());
      }
    } else {
      int numberOfPlayers = 0;
      try {
        System.out.println("Enter the number of players: ");

        if (!scanner.hasNextInt()) {
          throw new IllegalArgumentException("Please enter a integer");
        }

        numberOfPlayers = scanner.nextInt();
        scanner.nextLine();

        if (numberOfPlayers < 1) {
          throw new IllegalArgumentException("Please enter a number greater than 1");
        }

        if (numberOfPlayers > 5) {
          throw new IllegalArgumentException("Please enter a number less than 5");
        }
      } catch (IllegalArgumentException e) {
        logger.log(Level.SEVERE, e.getMessage());
        System.exit(1);
      }
      for (int i = 0; i < numberOfPlayers; i++) {
        System.out.println("Enter the name of player " + (i + 1) + ": ");
        String playerName = scanner.next();
        Player player = new Player(playerName);
        boardGame.addPlayer(player);

        Tile startTile = board.getTileId(0);
        player.setCurrentTile(startTile);
      }
    }


    boardGame.getPlayers();
    boardGame.play();

    Player winner = boardGame.getWinner();
    logger.log(Level.INFO, "The winner is: " + winner.getName());

    System.out.println("Do you want to save players to the csv file? (yes/no)");
    String choiceSave = scanner.nextLine().trim();

    if (choiceSave.equalsIgnoreCase("yes")) {
      try {
        playerService.writePlayersToFile(filePathPlayers);
        logger.log(Level.INFO, "Players saved to the file: " + filePathPlayers);
      } catch (IOException e) {
        logger.log(Level.SEVERE, "Error writing to the file: " + e.getMessage());
      }
    }
  }
}
