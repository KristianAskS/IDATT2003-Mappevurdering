package edu.ntnu.bidata.idatt.model.logic;

import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEvent;
import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEventType;
import edu.ntnu.bidata.idatt.controller.patterns.observer.interfaces.BoardGameObserver;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Dice;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The type Board game.
 * A facade class that represents the board game.
 */
public class BoardGame {
  private final List<Player> players;
  private final List<BoardGameObserver> observers;
  Logger logger = Logger.getLogger(BoardGame.class.getName());
  private Board board;
  private Player currentPlayer;
  private Dice dice;

  public BoardGame(Board board, List<Player> players, int numbOfDice) {
    this.board = board;
    this.players = new ArrayList<>();
    this.observers = new ArrayList<>();

    setBoard(board);
    createDice(numbOfDice);
    addPlayers(players);
  }

  public void addObserver(BoardGameObserver observer) {
    observers.add(observer);
  }

  public void removeObserver(BoardGameObserver observer) {
    observers.remove(observer);
  }

  private void notifyObservers(BoardGameEventType eventType, Player player, Tile oldTile,
                               Tile newTile) {
    for (BoardGameObserver observer : observers) {
      observer.onEvent(new BoardGameEvent(eventType, player, oldTile, newTile));
    }
  }

  public Board getBoard() {
    return board;
  }

  public void setBoard(Board board) {
    this.board = board;
  }

  public void addPlayers(List<Player> players) {
    this.players.addAll(players);
  }

  public void createDice(int numbOfDice) {
    dice = new Dice(numbOfDice);
  }

  public Dice getDice() {
    return dice;
  }

  public Player getCurrentPlayer() {
    return currentPlayer;
  }
}
