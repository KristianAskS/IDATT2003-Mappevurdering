package edu.ntnu.bidata.idatt;

import edu.ntnu.bidata.idatt.entity.Player;
import edu.ntnu.bidata.idatt.logic.BoardGame;
import edu.ntnu.bidata.idatt.logic.BoardGameFactory;
import edu.ntnu.bidata.idatt.model.Board;
import edu.ntnu.bidata.idatt.model.Tile;
import edu.ntnu.bidata.idatt.service.BoardService;
import edu.ntnu.bidata.idatt.service.PlayerService;
import edu.ntnu.bidata.idatt.utils.PopulateBoard;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BoardGameApp {
  private static final String filePathPlayers = "data/players.csv";
  private static final String filePathLaddersAndSnakes = "data/games/laddersAndSnakes.json";
  private static final Scanner scanner = new Scanner(System.in);
  private static final PlayerService playerService = new PlayerService();
  private static final Logger logger = Logger.getLogger(BoardGameApp.class.getName());
  private static final BoardGameFactory boardGameFactory = new BoardGameFactory();

  public static void main(String[] args) {
    System.out.println("Do you want to generate a new board and save it to JSON? (yes/no)");
    String choiceGenerate = scanner.nextLine().trim();

    if (choiceGenerate.equalsIgnoreCase("yes")) {
      Board newBoard = PopulateBoard.createBoard(90);

      BoardService boardService = new BoardService();
      boardService.setBoard(newBoard);
      try {
        boardService.writeBoardToFile(filePathLaddersAndSnakes);
        logger.log(Level.INFO, "New board generated and saved to: " + filePathLaddersAndSnakes);
      } catch (IOException e) {
        logger.log(Level.SEVERE, "Error writing new board to JSON: " + e.getMessage());
      }
    }

    System.out.println("Do you want to load the board configuration from JSON? (yes/no)");
    String choiceBoardJson = scanner.nextLine().trim();

    BoardGame boardGame = new BoardGame();
    Board board = null;

    if (choiceBoardJson.equalsIgnoreCase("yes")) {
      BoardService boardService = new BoardService();
      try {
        board = boardService.readBoardFromFile(filePathLaddersAndSnakes);
        logger.log(Level.INFO, "Board loaded from: " + filePathLaddersAndSnakes);
      } catch (IOException e) {
        logger.log(Level.SEVERE, "Error reading from the file: " + e.getMessage());
      }
    }

    if (board == null) {
      board = BoardGameFactory.createClassicBoard();
    } else {
      boardGame.setBoard(board);
    }

    boardGame.createDice(3);

    for (int i = 1; i <= 90; i++) {
      Tile currentTile = board.getTileId(i);
      Tile nextTile = board.getTileId(i + 1);
      if (currentTile != null && nextTile != null) {
        currentTile.setNextTile(nextTile);
      }
    }

    System.out.println("Do you want to load players from the CSV file? (yes/no)");
    String choicePlayers = scanner.nextLine().trim();

    if (choicePlayers.equalsIgnoreCase("yes")) {
      try {
        List<Player> players = playerService.readPlayersFromFile(filePathPlayers);
        for (Player player : players) {
          boardGame.addPlayer(player);
          Tile startTile = board.getTileId(1);
          player.setCurrentTile(startTile);
        }
        logger.log(Level.INFO, "Players loaded from CSV: " + filePathPlayers);
      } catch (IOException e) {
        logger.log(Level.SEVERE, "Error reading players from file: " + e.getMessage());
      }
    } else {
      int numberOfPlayers = 0;
      try {
        System.out.println("Enter the number of players: ");
        if (!scanner.hasNextInt()) {
          throw new IllegalArgumentException("Please enter an integer.");
        }
        numberOfPlayers = scanner.nextInt();
        scanner.nextLine();
        if (numberOfPlayers < 1) {
          throw new IllegalArgumentException("Please enter a number >= 1.");
        }
        if (numberOfPlayers > 5) {
          throw new IllegalArgumentException("Please enter a number <= 5.");
        }
      } catch (IllegalArgumentException e) {
        logger.log(Level.SEVERE, e.getMessage());
        System.exit(1);
      }

      for (int i = 0; i < numberOfPlayers; i++) {
        System.out.println("Enter the name of player " + (i + 1) + ": ");
        String playerName = scanner.next();
        Player player = new Player(playerName);
        player.setCurrentTile(board.getTileId(1));
        boardGame.addPlayer(player);

        Tile startTile = board.getTileId(1);
        player.setCurrentTile(startTile);
      }
    }

    boardGame.getPlayers();
    boardGame.play();

    Player winner = boardGame.getWinner();
    logger.log(Level.INFO, "The winner is: " + winner.getName());

    System.out.println("Do you want to save players to the CSV file? (yes/no)");
    String choiceSave = scanner.nextLine().trim();
    if (choiceSave.equalsIgnoreCase("yes")) {
      try {
        playerService.writePlayersToFile(filePathPlayers);
        logger.log(Level.INFO, "Players saved to: " + filePathPlayers);
      } catch (IOException e) {
        logger.log(Level.SEVERE, "Error writing to the file: " + e.getMessage());
      }
    }
  }
}
