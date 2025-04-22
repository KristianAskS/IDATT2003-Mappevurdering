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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Game logic and state
 */
public class BoardGame {
  private final List<Player> players;
  private final List<BoardGameObserver> observers;
  private final Logger logger = Logger.getLogger(BoardGame.class.getName());
  private Board board;
  private Player currentPlayer;
  private Dice dice;
  private int currentPlayerIndex = 0;

  public BoardGame(Board board, List<Player> players, int numbOfDice) {
    this.board = board;
    this.players = new ArrayList<>();
    this.observers = new ArrayList<>();

    setBoard(board);
    createDice(numbOfDice);
    addPlayers(players);
  }

  public void playTurn() {
    if (players.isEmpty()) {
      logger.log(Level.INFO, "players.isEmpty()");
      return;
    }

    currentPlayer = players.get(currentPlayerIndex);
    int steps = dice.roll();

    Tile oldTile = board.getTile(currentPlayer.getCurrentTileId());
    int nextTileId = Math.min(currentPlayer.getCurrentTileId() + steps, board.getTiles().size());
    currentPlayer.setCurrentTileId(nextTileId);
    Tile newTile = board.getTile(nextTileId);

    if (newTile.getLandAction() != null) {
      newTile.getLandAction().perform(currentPlayer);
      int destinationTileId = newTile.getLandAction().getDestinationTileId();
      newTile = board.getTile(currentPlayer.getCurrentTileId());
      currentPlayer.setCurrentTileId(destinationTileId);
    }

    notifyObservers(BoardGameEventType.PLAYER_MOVED, currentPlayer, oldTile, newTile);

    if (nextTileId >= board.getTiles().size()) {
      notifyObservers(BoardGameEventType.GAME_FINISHED, currentPlayer, oldTile, newTile);
      return;
    }

    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
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

  public void setBoard(Board board) {
    this.board = board;
  }

  public void addPlayers(List<Player> players) {
    players.forEach(player -> player.setCurrentTileId(1));
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