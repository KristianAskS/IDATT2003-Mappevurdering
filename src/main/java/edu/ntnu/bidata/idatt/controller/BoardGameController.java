package edu.ntnu.bidata.idatt.controller;

import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEvent;
import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEventType;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Die;
import edu.ntnu.bidata.idatt.model.entity.Dice;
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
import javafx.animation.Interpolator;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * Controller for handling view-related operations during the board game.
 * Uses hop-by-hop transitions for token movement, keeping token centered.
 */
public class BoardGameController {
  private static final Logger LOGGER = Logger.getLogger(BoardGameController.class.getName());
  private static Dice dice;
  private static Die die;

  private final PlayerService playerService;
  private final Board gameBoard;
  private final BoardGameScene boardGameScene;

  private final List<Player> turnOrder = new ArrayList<>();
  private final List<Player> finishedPlayers = new ArrayList<>();
  private int currentPlayerIndex;

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
    this.currentPlayerIndex = 0;
  }

  public static int getLastRolledValue() {
    return die.getLastRolledValue();
  }

  public static void setRolledValue(int rollValue) {
    dice.setRollResult(rollValue);
  }

  private void ensureTurnOrderInitialized() {
    if (!turnOrder.isEmpty()) return;
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
    if (turnOrder.isEmpty()) return;

    Player currentPlayer = turnOrder.get(currentPlayerIndex);
    int fromTileId = currentPlayer.getCurrentTileId();

    movePlayerWithHops(currentPlayer, steps);

    int toTileId = currentPlayer.getCurrentTileId();
    boardGameScene.onEvent(new BoardGameEvent(
        BoardGameEventType.PLAYER_MOVED,
        currentPlayer,
        new Tile(fromTileId),
        new Tile(toTileId)));

    if (toTileId >= gameBoard.getTiles().size()) {
      finishPlayer(currentPlayer, fromTileId, toTileId);
    } else {
      currentPlayerIndex = (currentPlayerIndex + 1) % turnOrder.size();
    }
  }

  private void finishPlayer(Player player, int fromTileId, int toTileId) {
    LOGGER.log(Level.INFO, "{0} reached the finish!", player.getName());
    finishedPlayers.add(player);
    turnOrder.remove(currentPlayerIndex);
    boardGameScene.onEvent(new BoardGameEvent(
        BoardGameEventType.PLAYER_FINISHED,
        player,
        new Tile(fromTileId),
        new Tile(toTileId)));

    if (turnOrder.isEmpty()) {
      PodiumGameScene.setFinalRanking(finishedPlayers);
      boardGameScene.onEvent(new BoardGameEvent(
          BoardGameEventType.GAME_FINISHED,
          player,
          new Tile(toTileId),
          new Tile(toTileId)));
    } else if (currentPlayerIndex >= turnOrder.size()) {
      currentPlayerIndex = 0;
    }
  }

  private void movePlayerWithHops(Player player, int steps) {
    int fromTileId = player.getCurrentTileId();
    int toTileId = Math.min(fromTileId + steps, gameBoard.getTiles().size());

    TileView startTileView = lookupTileView(fromTileId);
    TileView endTileView = lookupTileView(toTileId);
    Node tokenNode = player.getToken();
    Pane tokenOverlay = boardGameScene.getTokenLayer();

    if (startTileView == null || tokenNode == null) {
      player.setCurrentTileId(toTileId);
      return;
    }

    startTileView.getChildren().remove(tokenNode);
    tokenOverlay.getChildren().add(tokenNode);
    tokenNode.toFront();


    tokenNode.applyCss();
    if (tokenNode instanceof Parent) ((Parent) tokenNode).layout();
    Bounds tokenBounds = tokenNode.getBoundsInLocal();
    double halfWidth = tokenBounds.getWidth() * 0.35;
    double halfHeight = tokenBounds.getHeight() * 0.35;

    Bounds startBounds = startTileView.localToScene(startTileView.getBoundsInLocal());
    double startCenterX = startBounds.getMinX() + startBounds.getWidth() * 0.5;
    double startCenterY = startBounds.getMinY() + startBounds.getHeight() * 0.5;
    Point2D startOverlayPoint = tokenOverlay.sceneToLocal(startCenterX, startCenterY);

    tokenNode.setTranslateX(startOverlayPoint.getX() - halfWidth);
    tokenNode.setTranslateY(startOverlayPoint.getY() - halfHeight);

    SequentialTransition hopSequence = new SequentialTransition();
    Point2D previousOrigin = new Point2D(
        startOverlayPoint.getX() - halfWidth,
        startOverlayPoint.getY() - halfHeight);

    for (int tileId = fromTileId + 1; tileId <= toTileId; tileId++) {
      TileView tileView = lookupTileView(tileId);
      if (tileView == null) continue;

      Bounds tileBounds = tileView.localToScene(tileView.getBoundsInLocal());
      double centerX = tileBounds.getMinX() + tileBounds.getWidth() * 0.5;
      double centerY = tileBounds.getMinY() + tileBounds.getHeight() * 0.5;
      Point2D overlayPoint = tokenOverlay.sceneToLocal(centerX, centerY);
      Point2D nextOrigin = new Point2D(
          overlayPoint.getX() - halfWidth,
          overlayPoint.getY() - halfHeight);

      TranslateTransition hop = new TranslateTransition(Duration.millis(200), tokenNode);
      hop.setByX(nextOrigin.getX() - previousOrigin.getX());
      hop.setByY(nextOrigin.getY() - previousOrigin.getY());
      hop.setInterpolator(Interpolator.LINEAR);
      hopSequence.getChildren().add(hop);

      previousOrigin = nextOrigin;
    }

    hopSequence.setOnFinished(event -> {
      tokenOverlay.getChildren().remove(tokenNode);
      if (endTileView != null) {
        endTileView.getChildren().add(tokenNode);
        boardGameScene.setTokenPositionOnTile(endTileView);
      }
      tokenNode.setTranslateX(0);
      tokenNode.setTranslateY(0);
    });

    hopSequence.play();
    player.setCurrentTileId(toTileId);
  }

  private TileView lookupTileView(int tileId) {
    return (TileView) boardGameScene.getScene().lookup("#tile" + tileId);
  }

  public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
  }
}
