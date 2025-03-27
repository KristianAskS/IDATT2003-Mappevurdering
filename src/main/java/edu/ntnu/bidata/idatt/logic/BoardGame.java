package edu.ntnu.bidata.idatt.logic;

import edu.ntnu.bidata.idatt.entity.Player;
import edu.ntnu.bidata.idatt.model.Board;
import edu.ntnu.bidata.idatt.model.Dice;
import edu.ntnu.bidata.idatt.model.Tile;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Board game.
 * A facade class that represents the board game.
 */
public class BoardGame {

  private final List<Player> players = new ArrayList<>();
  private final List<BoardGameObserver> observers = new ArrayList<>();
  Logger logger = Logger.getLogger(BoardGame.class.getName());
  private Board board;
  private Player currentPlayer;
  private Dice dice;

  public BoardGame() {
    this.board = new Board();
  }

  public Board getBoard() {
    return board;
  }

  public void setBoard(Board board) {
    this.board = board;
  }

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

    logger.log(Level.INFO, "Players in the game:");
    for (Player player : players) {
      logger.log(Level.INFO, player.getName());
    }
    return players;
  }

  public int getNumbOfTiles() {
    return board.getTiles().size();
  }

  public void createDice(int numbOfDice) {
    dice = new Dice(numbOfDice);
  }

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

        int roll = dice.roll();
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

  public Player getWinner() {
    for (Player player : players) {
      if (player.getPosition() >= getNumbOfTiles() - 1) {
        return player;
      }
    }
    return null;
  }

  public Dice getDice() {
    return dice;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }

  public void addObserver(BoardGameObserver observer) {
    observers.add(observer);
  }

  public void removeObserver(BoardGameObserver observer) {
    observers.remove(observer);
  }

  private void notifyObservers(Player player, Tile oldTile, Tile newTile) {
    for (BoardGameObserver observer : observers) {
      observer.onPlayerMoved(player, oldTile, newTile);
    }
  }

  private void notifyGameFinished(Player player) {
    for (BoardGameObserver observer : observers) {
      observer.onGameFinished(player);
    }
  }
}
