package edu.ntnu.iir.bidata.logic;

import edu.ntnu.iir.bidata.entity.Player;
import edu.ntnu.iir.bidata.model.Board;
import edu.ntnu.iir.bidata.model.Dice;
import edu.ntnu.iir.bidata.model.Tile;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Board game.
 */
public class BoardGame {

  private static Board board;
  private final List<Player> players = new ArrayList<>();
  Logger logger = Logger.getLogger(BoardGame.class.getName());
  private Player currentPlayer;
  private Dice dice;

  /**
   * Gets board.
   *
   * @return the board
   */
  public static Board getBoard() {
    return board;
  }

  /**
   * Add player.
   *
   * @param player the player
   */
  public void addPlayer(Player player) {
    players.add(player);
  }

  /**
   * For del 1 i mappevurderingen UNNGÅ Å BRUK sysout
   */
  public List<Player> getPlayers() {
    if (players.isEmpty()) {
      throw new IllegalStateException("No players found");
    }

    System.out.println("The players are:");
    for (Player player : players) {
      logger.log(Level.INFO, player.getName());
    }
    return players;
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

  public int getNumbOfTiles() {
    return board.getTiles().size();
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
   * Play. Burde del logikk i mindre hjelpemetoder
   */
  public void play() {
    int round = 1;
    Player winner = null;

    while (winner == null) {
      System.out.println("Round number: " + round);

      for (Player player : players) {
        currentPlayer = player;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Player " + player.getName() + " turn. Press enter to roll the dice");
        scanner.nextLine();

        int roll = dice.roll(); //Triller terning
        System.out.println("Player " + player.getName() + " rolled " + roll);

        System.out.println("Press enter to move player " + player.getName());
        currentPlayer.move(roll); //Flytter spiller basert på antall øyne

        System.out.println("Player " + player.getName() + " on tile " + player.getPosition());

        if (getWinner() != null) {
          winner = getWinner();
          break;
        }
      }
      System.out.println();
      round++;
    }
  }

  /**
   * Gets winner.
   *
   * @return the winner
   */
  public Player getWinner() {
    for (Player player : players) {
      if (player.getPosition() >= getNumbOfTiles() - 1) {
        return player;
      }
    }
    return null;
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
