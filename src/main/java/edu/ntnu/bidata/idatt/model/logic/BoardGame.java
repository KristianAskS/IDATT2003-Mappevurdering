package edu.ntnu.bidata.idatt.model.logic;

import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEvent;
import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEventType;
import edu.ntnu.bidata.idatt.controller.patterns.observer.interfaces.BoardGameObserver;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Dice;
import edu.ntnu.bidata.idatt.model.entity.Player;
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
  private int currentIndex = 0;
  private boolean started = false;

  public BoardGame(Board board, int numOfDice) {
    this.board = board;
    this.dice = new Dice(numOfDice);
  }

  public void addObserver(BoardGameObserver observer) {
    observers.add(observer);
  }

  public void removeObserver(BoardGameObserver observer) {
    observers.remove(observer);
  }

  public void addPlayer(Player player) {
    if (started) {
      throw new IllegalStateException("Cannot add players after the game has started");
    }
    player.setCurrentTileId(0);
    players.add(player);
  }

  /**
   * Start the game:
   */
  public void start() {
    if (players.isEmpty()) {
      throw new IllegalStateException("Add at least one player before starting the game");
    }
    started = true;
    //Collections.shuffle(players);
    notifyEvent(new BoardGameEvent(BoardGameEventType.PLAYER_MOVED, null, null, null));
  }

  public boolean hasWinner() {
    return players.stream()
        .anyMatch(player -> player.getCurrentTileId() == board.getTiles().size() - 1);
  }

  public Player getWinner() {
    return players.stream()
        .filter(player -> player.getCurrentTileId() == board.getTiles().size() - 1)
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("No winner yet"));
  }


  public List<Player> getPlayers() {
    return Collections.unmodifiableList(players);
  }

  public Board getBoard() {
    return board;
  }

  private void notifyEvent(BoardGameEvent event) {
    observers.forEach(o -> o.onEvent(event));
  }
}
