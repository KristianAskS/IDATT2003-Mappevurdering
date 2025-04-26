package edu.ntnu.bidata.idatt.controller;

import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEvent;
import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEventType;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Dice;
import edu.ntnu.bidata.idatt.model.entity.Die;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.model.service.PlayerService;
import edu.ntnu.bidata.idatt.view.components.TileView;
import edu.ntnu.bidata.idatt.view.scenes.BoardGameScene;
import edu.ntnu.bidata.idatt.view.scenes.PodiumGameScene;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class BoardGameController {
  private static final Logger LOGGER = Logger.getLogger(BoardGameController.class.getName());
  private static Dice dice;
  private static Die die;

  private final PlayerService playerService;
  private final Board gameBoard;
  private final BoardGameScene boardGameScene;

  private final List<Player> turnOrder = new ArrayList<>();
  private final List<Player> finishedPlayers = new ArrayList<>();
  private int currentPlayerIndex = 0;

  public BoardGameController(
      BoardGameScene boardGameScene,
      PlayerService playerService,
      Board gameBoard,
      int numberOfDice) {
    this.boardGameScene = boardGameScene;
    this.playerService = playerService;
    this.gameBoard = gameBoard;
    dice = new Dice(numberOfDice);
    die = new Die();
  }

  public static int getLastRolledValue() {
    return die.getLastRolledValue();
  }

  public static void setRolledValue(int rollValue) {
    dice.setRollResult(rollValue);
  }

  private void ensureTurnOrderInitialized() {
    if (!turnOrder.isEmpty()) {
      return;
    }
    List<Player> players = playerService.getPlayers();
    if (players.isEmpty()) {
      LOGGER.log(Level.WARNING, "No players available to start the game");
      return;
    }
    turnOrder.addAll(players);
    LOGGER.log(Level.INFO, "Turn order initialized with {0} players", turnOrder.size());
  }

  public void handlePlayerTurn(int steps) {
    ensureTurnOrderInitialized();
    if (turnOrder.isEmpty()) {
      return;
    }
    Player currentPlayer = turnOrder.get(currentPlayerIndex);
    int startTileIndex = currentPlayer.getCurrentTileId();
    movePlayer(currentPlayer, steps);
    int endTileIndex = currentPlayer.getCurrentTileId();
    boardGameScene.onEvent(new BoardGameEvent(
        BoardGameEventType.PLAYER_MOVED,
        currentPlayer,
        new Tile(startTileIndex),
        new Tile(endTileIndex)));
    if (endTileIndex >= gameBoard.getTiles().size()) {
      finishPlayer(currentPlayer, startTileIndex, endTileIndex);
    } else {
      currentPlayerIndex = (currentPlayerIndex + 1) % turnOrder.size();
    }
  }

  private void finishPlayer(Player player, int startTileIndex, int endTileIndex) {
    LOGGER.log(Level.INFO, "{0} reached the finish!", player.getName());
    finishedPlayers.add(player);
    turnOrder.remove(currentPlayerIndex);
    boardGameScene.onEvent(new BoardGameEvent(
        BoardGameEventType.PLAYER_FINISHED,
        player,
        new Tile(startTileIndex),
        new Tile(endTileIndex)));
    if (turnOrder.isEmpty()) {
      PodiumGameScene.setFinalRanking(finishedPlayers);
      boardGameScene.onEvent(new BoardGameEvent(
          BoardGameEventType.GAME_FINISHED,
          player,
          new Tile(endTileIndex),
          new Tile(endTileIndex)));
    } else if (currentPlayerIndex >= turnOrder.size()) {
      currentPlayerIndex = 0;
    }
  }

  private void movePlayer(Player player, int steps) {
    int startId = player.getCurrentTileId();
    int endId = Math.min(startId + steps, gameBoard.getTiles().size());

    Node token = player.getToken();
    if (token == null) {
      player.setCurrentTileId(endId);
      return;
    }

    SequentialTransition sequentialTransition = new SequentialTransition();
    for (int tileId = startId + 1; tileId <= endId; tileId++) {
      int next = tileId;
      PauseTransition pauseTransition = new PauseTransition(Duration.millis(300));
      pauseTransition.setOnFinished(evt -> {
        ((Pane) token.getParent()).getChildren().remove(token);
        TileView tileView = lookupTileView(next);
        tileView.getChildren().add(token);
        boardGameScene.setTokenPositionOnTile(tileView);
      });
      sequentialTransition.getChildren().add(pauseTransition);
    }
    sequentialTransition.setOnFinished(evt -> player.setCurrentTileId(endId));
    sequentialTransition.play();
  }


  private TileView lookupTileView(int tileIndex) {
    return (TileView) boardGameScene.getScene().lookup("#tile" + tileIndex);
  }

  public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
  }
}
