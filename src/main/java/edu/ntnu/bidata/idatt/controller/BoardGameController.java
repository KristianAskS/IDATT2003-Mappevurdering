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
import javafx.animation.PathTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
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
    int startTileIndex = player.getCurrentTileId();
    int endTileIndex = Math.min(startTileIndex + steps, gameBoard.getTiles().size());

    TileView startTileView = lookupTileView(startTileIndex);
    TileView endTileView = lookupTileView(endTileIndex);
    Node playerToken = player.getToken();
    Pane tokenOverlay = boardGameScene.getTokenLayer();

    if (startTileView == null || playerToken == null) {
      player.setCurrentTileId(endTileIndex);
      return;
    }

    Bounds tokenBounds = playerToken.localToScene(playerToken.getBoundsInLocal());
    double tokenCenterX = tokenBounds.getMinX() + tokenBounds.getWidth()  * 0.5;
    double tokenCenterY = tokenBounds.getMinY() + tokenBounds.getHeight() * 0.5;

    startTileView.getChildren().remove(playerToken);
    tokenOverlay.getChildren().add(playerToken);
    playerToken.toFront();
    Point2D tokenOverlayStart = tokenOverlay.sceneToLocal(tokenCenterX, tokenCenterY);
    playerToken.setTranslateX(tokenOverlayStart.getX());
    playerToken.setTranslateY(tokenOverlayStart.getY());

    Path movementPath = new Path();
    movementPath.getElements().add(new MoveTo(tokenOverlayStart.getX(), tokenOverlayStart.getY()));

    for (int id = startTileIndex + 1; id <= endTileIndex; id++) {
      TileView tileView = lookupTileView(id);
      if (tileView == null) {
        continue;
      }
      Bounds tileBounds = tileView.localToScene(tileView.getBoundsInLocal());
      double centerX = tileBounds.getMinX() + tileBounds.getWidth()  * 0.40;
      double centerY = tileBounds.getMinY() + tileBounds.getHeight() * 0.40;
      Point2D overlayPoint = tokenOverlay.sceneToLocal(centerX, centerY);
      movementPath.getElements().add(new LineTo(overlayPoint.getX(), overlayPoint.getY()));
    }

    PathTransition movementTransition = new PathTransition(
        Duration.millis(200 * (endTileIndex - startTileIndex)),
        movementPath,
        playerToken);
    movementTransition.setInterpolator(Interpolator.LINEAR);
    movementTransition.setOnFinished(evt -> {
      tokenOverlay.getChildren().remove(playerToken);
      if (endTileView != null) {
        endTileView.getChildren().add(playerToken);
        boardGameScene.setTokenPositionOnTile(endTileView);
      }
      playerToken.setTranslateX(0);
      playerToken.setTranslateY(0);
    });
    movementTransition.play();

    player.setCurrentTileId(endTileIndex);
  }

  private TileView lookupTileView(int tileIndex) {
    return (TileView) boardGameScene.getScene().lookup("#tile" + tileIndex);
  }

  public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
  }
}
