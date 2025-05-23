package edu.ntnu.bidata.idatt.model.logic;

import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEvent;
import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEventType;
import edu.ntnu.bidata.idatt.controller.patterns.observer.interfaces.BoardGameObserver;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Dice;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.utils.exceptions.InvalidGameStateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Facade for UI to interact with the board game logic.
 */
public class BoardGame {
  private final Board board;
  private final Dice dice;
  private final List<Player> players = new ArrayList<>();
  private final List<BoardGameObserver> observers = new ArrayList<>();
  private final Logger logger = Logger.getLogger(BoardGame.class.getName());
  private final int currentIndex = 0;
  private boolean started = false;

  /**
   * Constructs a new BoardGame.
   *
   * @param board     the board model
   * @param numOfDice the number of dice to use
   */
  public BoardGame(Board board, int numOfDice) {
    this.board = board;
    this.dice = new Dice(numOfDice);
  }

  /**
   * Adds an observer to the game.
   *
   * @param observer the observer to add
   */
  public void addObserver(BoardGameObserver observer) {
    observers.add(observer);
  }

  /**
   * Removes an observer from the game.
   *
   * @param observer the observer to remove
   */
  public void removeObserver(BoardGameObserver observer) {
    observers.remove(observer);
  }

  /**
   * Adds a player to the game.
   *
   * @param player the player to add
   */
  public void addPlayer(Player player) {
    if (started) {
      throw new InvalidGameStateException("Cannot add players after the game has started");
    }
    player.setCurrentTileId(0);
    players.add(player);
  }

  /**
   * Start the game:
   */
  public void start() {
    if (players.isEmpty()) {
      throw new InvalidGameStateException("Cannot start game without players");
    }
    started = true;
    //Collections.shuffle(players);
    notifyEvent(new BoardGameEvent(BoardGameEventType.PLAYER_MOVED, null, null, null, null));
  }

  /**
   * Determines if the game has a winner.
   *
   * @return true if the game has a winner, false otherwise
   */
  public boolean hasWinner() {
    return players.stream()
        .anyMatch(player -> player.getCurrentTileId() == board.getTiles().size() - 1);
  }

  /**
   * Returns the winner of the game.
   *
   * @return the winner
   */
  public Player getWinner() {
    return players.stream()
        .filter(player -> player.getCurrentTileId() == board.getTiles().size() - 1)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("No winner yet"));
  }


  /**
   * Returns the players in the game.
   *
   * @return the players
   */
  public List<Player> getPlayers() {
    return Collections.unmodifiableList(players);
  }

  /**
   * Returns the board model.
   *
   * @return the board model
   */
  public Board getBoard() {
    return board;
  }

  /**
   * Notifies all observers of an event.
   *
   * @param event the event to notify
   */
  private void notifyEvent(BoardGameEvent event) {
    observers.forEach(o -> o.onEvent(event));
  }
}
