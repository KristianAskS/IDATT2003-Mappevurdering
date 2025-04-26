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
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
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
  private final Board board;
  private final BoardGameScene gameScene;

  private final List<Player> turnOrder = new ArrayList<>();
  private final List<Player> finishedPlayers = new ArrayList<>();
  private int currentPlayerIndex = 0;

  public BoardGameController(
      BoardGameScene gameScene,
      PlayerService playerService,
      Board board,
      int numberOfDice) {
    this.gameScene = gameScene;
    this.playerService = playerService;
    this.board = board;
    dice = new Dice(numberOfDice);
    die = new Die();
  }

  public static int getLastRolledValue() {
    return die.getLastRolledValue();
  }

  public static void setRolledValue(int rollValue) {
    dice.setRollResult(rollValue);
  }

  public static int[] tileToGridPosition(Tile tile, Board board) {
    int totalTiles = board.getTiles().size();
    int tileId = tile.getTileId();
    int columns = 10;
    int rows = (int) Math.ceil(totalTiles / (double) columns);

    int row = (tileId - 1) / columns;
    int col = (tileId - 1) % columns;
    if (row % 2 == 1) {
      col = columns - col - 1;
    }

    row = rows - 1 - row;
    return new int[] {row, col};
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
    int originTileId = currentPlayer.getCurrentTileId();

    movePlayerAlongTiles(currentPlayer, steps, () -> {
      int landedTileId = currentPlayer.getCurrentTileId();
      gameScene.onEvent(new BoardGameEvent(
          BoardGameEventType.PLAYER_MOVED,
          currentPlayer,
          new Tile(originTileId),
          new Tile(landedTileId)));

      Tile landedTile = board.getTile(landedTileId);
      if (landedTile.getLandAction() != null) {
        int ladderDestination = landedTile.getLandAction().getDestinationTileId();
        landedTile.getLandAction().perform(currentPlayer);

        animateLadderJump(
            currentPlayer,
            landedTileId,
            ladderDestination,
            () -> {
              gameScene.onEvent(new BoardGameEvent(
                  BoardGameEventType.PLAYER_LADDER_ACTION,
                  currentPlayer,
                  new Tile(landedTileId),
                  new Tile(ladderDestination)));
              advanceOrFinish(currentPlayer);
            });
      } else {
        advanceOrFinish(currentPlayer);
      }
    });
  }

  private void advanceOrFinish(Player player) {
    int position = player.getCurrentTileId();
    if (position >= board.getTiles().size()) {
      finishPlayer(player);
    } else {
      currentPlayerIndex = (currentPlayerIndex + 1) % turnOrder.size();
    }
  }

  private void finishPlayer(Player player) {
    LOGGER.log(Level.INFO, "{0} finished the game!", player.getName());
    finishedPlayers.add(player);
    turnOrder.remove(currentPlayerIndex);

    gameScene.onEvent(new BoardGameEvent(
        BoardGameEventType.PLAYER_FINISHED,
        player,
        null, // old tile not needed here
        new Tile(player.getCurrentTileId())));

    if (turnOrder.isEmpty()) {
      PodiumGameScene.setFinalRanking(finishedPlayers);
      gameScene.onEvent(new BoardGameEvent(
          BoardGameEventType.GAME_FINISHED,
          player,
          null,
          new Tile(player.getCurrentTileId())));
    } else if (currentPlayerIndex >= turnOrder.size()) {
      currentPlayerIndex = 0;
    }
  }

  private void movePlayerAlongTiles(Player player, int steps, Runnable onComplete) {
    int startTileId = player.getCurrentTileId();
    int targetTileId = Math.min(startTileId + steps, board.getTiles().size());
    Node token = player.getToken();
    Pane overlay = gameScene.getTokenLayer();

    if (token == null) {
      player.setCurrentTileId(targetTileId);
      onComplete.run();
      return;
    }

    SequentialTransition sequence = new SequentialTransition();
    for (int nextId = startTileId + 1; nextId <= targetTileId; nextId++) {
      int finalNextId = nextId;
      PauseTransition hop = new PauseTransition(Duration.millis(250));
      hop.setOnFinished(evt -> {
        Pane parent = (Pane) token.getParent();
        parent.getChildren().remove(token);

        TileView tv = lookupTileView(finalNextId);
        tv.getChildren().add(token);
        gameScene.setTokenPositionOnTile(tv);

        player.setCurrentTileId(finalNextId);
      });
      sequence.getChildren().add(hop);
    }

    sequence.setOnFinished(evt -> onComplete.run());
    sequence.play();
  }

  private void animateLadderJump(Player player, int fromTile, int toTile, Runnable onComplete) {
    TileView startView = lookupTileView(fromTile);
    TileView endView = lookupTileView(toTile);
    Node token = player.getToken();
    Pane overlay = gameScene.getTokenLayer();

    Point2D startCenter = tileCenter(startView, overlay);
    Pane parent = (Pane) token.getParent();
    parent.getChildren().remove(token);
    overlay.getChildren().add(token);
    token.setTranslateX(startCenter.getX());
    token.setTranslateY(startCenter.getY());

    Point2D endCenter = tileCenter(endView, overlay);
    Path path = new Path(
        new MoveTo(startCenter.getX(), startCenter.getY()),
        new LineTo(endCenter.getX(), endCenter.getY())
    );

    PathTransition jump = new PathTransition(Duration.seconds(0.5), path, token);
    jump.setInterpolator(Interpolator.EASE_BOTH);
    jump.setOnFinished(evt -> {
      overlay.getChildren().remove(token);
      endView.getChildren().add(token);
      gameScene.setTokenPositionOnTile(endView);
      onComplete.run();
    });
    jump.play();
  }

  private Point2D tileCenter(TileView tileView, Pane overlay) {
    Bounds bounds = tileView.localToScene(tileView.getBoundsInLocal());
    double x = bounds.getMinX() + bounds.getWidth() * 0.5;
    double y = bounds.getMinY() + bounds.getHeight() * 0.5;
    return overlay.sceneToLocal(x, y);
  }

  private TileView lookupTileView(int tileId) {
    return (TileView) gameScene.getScene().lookup("#tile" + tileId);
  }
}
