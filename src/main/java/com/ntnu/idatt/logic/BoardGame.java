package com.ntnu.idatt.logic;

import com.ntnu.idatt.entity.Player;
import com.ntnu.idatt.model.Board;
import com.ntnu.idatt.model.Dice;
import com.ntnu.idatt.model.Tile;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The type Board game.
 */
public class BoardGame {

  private final List<Player> players = new ArrayList<>();
  private static Board board;
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
   * For del 1 i mappevurderingen UNNGÅ Å BRUK sysout
   */
  public void getPlayers() {
    if (players.isEmpty()) {
      throw new IllegalStateException("No players found");
    }

    System.out.println("The players are:");
    for (Player player : players) {
      System.out.println(player.getName());
    }
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
   * Gets board.
   *
   * @return the board
   */
  public static Board getBoard() {
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
