package edu.ntnu.bidata.idatt.controller;

import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEvent;
import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEventType;
import edu.ntnu.bidata.idatt.controller.rules.GameRules;
import edu.ntnu.bidata.idatt.controller.rules.LudoRules;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Dice;
import edu.ntnu.bidata.idatt.model.entity.Die;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.model.logic.action.BackToStartAction;
import edu.ntnu.bidata.idatt.model.logic.action.SkipTurnAction;
import edu.ntnu.bidata.idatt.model.logic.action.TileAction;
import edu.ntnu.bidata.idatt.model.service.BoardService;
import edu.ntnu.bidata.idatt.model.service.PlayerService;
import edu.ntnu.bidata.idatt.view.components.TileView;
import edu.ntnu.bidata.idatt.view.scenes.BoardGameScene;
import edu.ntnu.bidata.idatt.view.scenes.PodiumGameScene;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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

/**
 * Base controller that contains the core game‑flow logic that is shared across the different board
 * games in the application.
 */
public abstract class GameController {

  private static final double TOKEN_PIXELS_PER_SECOND = 400.0;

  protected final Logger logger = Logger.getLogger(getClass().getName());
  protected final PlayerService playerService = new PlayerService();
  protected final BoardService boardService = new BoardService();
  protected final BoardGameScene boardGameScene;
  protected final Board board;
  protected final Dice dice;
  protected final Die die;
  final GameRules gameRules;

  /**
   * Players that must skip a number of upcoming turns. The value represents how many turns remain
   * to be skipped.
   */
  private final Map<Player, Integer> skipTurnMap = new HashMap<>();

  private final List<Player> turnOrder = new ArrayList<>();
  private final List<Player> finishedPlayers = new ArrayList<>();
  private int currentIndex = 0;

  protected GameController(BoardGameScene boardGameScene,
                           Board board,
                           int numberOfDice,
                           GameRules gameRules) throws IOException {
    this.boardGameScene = boardGameScene;
    this.board = board;
    this.dice = new Dice(numberOfDice);
    this.die = new Die();
    this.gameRules = gameRules;

    boardService.setBoard(board);
  }

  public abstract int[] tileToGridPosition(Tile tile, Board board);

  protected void applyLandAction(Player player, Tile landed, Runnable onDone) {
    TileAction action = landed.getLandAction();
    switch (action) {
      case null -> {
        onDone.run();
        return;
      }
      case BackToStartAction back -> {
        logger.log(Level.INFO, "BackToStart");
        back.perform(player);
        animateLadderMovement(player, landed.getTileId(), 1, () -> {
          boardGameScene.onEvent(new BoardGameEvent(BoardGameEventType.PLAYER_BACK_START_ACTION,
              player, landed, board.getTile(1)));
        });
        onDone.run();
        return;
      }
      case SkipTurnAction skipAct -> {
        logger.log(Level.INFO, "SkipTurn");
        int toSkip = skipAct.turnsToSkip();
        skipTurnMap.merge(player, toSkip, Integer::sum);
        boardGameScene.onEvent(new BoardGameEvent(BoardGameEventType.PLAYER_SKIP_TURN_ACTION,
            player, landed, landed));
        onDone.run();
        return;
      }
      default -> {
      }
    }

    action.perform(player);
    onDone.run();
  }

  protected boolean shouldFinish(Player player) {
    return player.getCurrentTileId() >= board.getTiles().size();
  }

  public void initializePlayers(List<Player> players) {
    players.forEach(player -> player.setCurrentTileId(0));
    playerService.setPlayers(players);
    boardGameScene.setupPlayersUI(players);

    initializeTurnOrder();
  }

  public Die getDie() {
    return die;
  }

  public void handlePlayerTurn(int steps) {
    initializeTurnOrder();
    dice.setRollResult(steps);

    if (turnOrder.isEmpty()) {
      return;
    }

    Player player = turnOrder.get(currentIndex);

    int remainingSkips = skipTurnMap.getOrDefault(player, 0);
    if (remainingSkips > 0) {
      skipTurnMap.put(player, remainingSkips - 1);
      logger.log(Level.INFO,
          () -> player.getName() + " skips a turn (" + (remainingSkips - 1) + " left)");
      advanceToNextPlayer();
      return;
    }

    if (!gameRules.canEnterTrack(player, steps)) {
      advanceToNextPlayer();
      return;
    }

    int maxTileId = board.getTiles().size();
    int destinationTileId = gameRules.destinationTile(player, steps, maxTileId);

    if (destinationTileId < 0) {
      advanceToNextPlayer();
      return;
    }

    int originTileId = player.getCurrentTileId();
    Tile originTile = originTileId == 0 ? null : board.getTile(originTileId);
    int hopCount = ((destinationTileId - originTileId) + maxTileId) % maxTileId;

    movePlayerAlongTiles(player, hopCount, () -> {
      Tile landed = board.getTile(player.getCurrentTileId());

      boardGameScene.onEvent(new BoardGameEvent(
          BoardGameEventType.PLAYER_MOVED, player, originTile, landed));

      applyLandAction(player, landed, () -> {
        if (shouldFinish(player)) {
          finishPlayer(player);
        } else {
          afterTurnLogic(player);
        }
      });
    });
  }

  private void movePlayerAlongTiles(Player player, int steps, Runnable onDoneCallback) {

    int start = player.getCurrentTileId();
    int boardSize = board.getTiles().size();

    steps = ((steps % boardSize) + boardSize) % boardSize;
    if (steps == 0) {
      onDoneCallback.run();
      return;
    }

    int tmp = (start + steps) % boardSize;
    if (tmp == 0) {
      tmp = boardSize;
    }
    final int target = tmp;

    Node token = player.getToken();
    if (token == null) {
      player.setCurrentTileId(target);
      onDoneCallback.run();
      return;
    }

    SequentialTransition seq = new SequentialTransition();

    boolean isLudo = gameRules instanceof LudoRules;
    boolean leaveYard = (start == 0) && isLudo;

    if (leaveYard) {
      seq.getChildren().add(getHopTransition(player, target, token));
    } else {
      for (int i = 1; i <= steps; i++) {
        int nextId = (start + i) % boardSize;
        if (nextId == 0) {
          nextId = boardSize;
        }
        seq.getChildren().add(getHopTransition(player, nextId, token));
      }
    }

    seq.setOnFinished(e -> {
      Tile landed = board.getTile(target);
      landed.addPlayer(player);
      onDoneCallback.run();
    });
    seq.play();
  }

  private PauseTransition getHopTransition(Player player, int nextId, Node token) {
    PauseTransition pauseTransition = new PauseTransition(Duration.millis(250));
    pauseTransition.setOnFinished(event -> {
      Pane parent = (Pane) token.getParent();
      parent.getChildren().remove(token);

      TileView tileView = lookupTileView(nextId);
      tileView.getChildren().add(token);
      boardGameScene.setTokenPositionOnTile(tileView);

      player.setCurrentTileId(nextId);
    });
    return pauseTransition;
  }

  void animateLadderMovement(Player player, int fromTileId, int toTileId,
                             Runnable onDoneCallback) {
    TileView startTileView = lookupTileView(fromTileId);
    TileView endTileView = lookupTileView(toTileId);
    Node token = player.getToken();

    Point2D startCenter = tileCenter(startTileView);
    Pane parentPane = (Pane) token.getParent();
    parentPane.getChildren().remove(token);

    Pane overlayPane = boardGameScene.getTokenLayer();
    overlayPane.getChildren().add(token);
    token.setTranslateX(startCenter.getX());
    token.setTranslateY(startCenter.getY());

    Point2D endCenter = tileCenter(endTileView);
    Path path = new Path(
        new MoveTo(startCenter.getX(), startCenter.getY()),
        new LineTo(endCenter.getX(), endCenter.getY())
    );

    double distance = startCenter.distance(endCenter);
    double durationSeconds = distance / TOKEN_PIXELS_PER_SECOND;

    PathTransition trans = new PathTransition(Duration.seconds(durationSeconds), path, token);
    trans.setOnFinished(event -> {
      overlayPane.getChildren().remove(token);
      endTileView.getChildren().add(token);
      boardGameScene.setTokenPositionOnTile(endTileView);
      onDoneCallback.run();
    });
    trans.play();
  }

  private Point2D tileCenter(TileView tileView) {
    Bounds bounds = tileView.localToScene(tileView.getBoundsInLocal());
    double x = bounds.getMinX() + bounds.getWidth() * 0.5;
    double y = bounds.getMinY() + bounds.getHeight() * 0.5;
    return boardGameScene.getTokenLayer().sceneToLocal(x, y);
  }

  private TileView lookupTileView(int tileId) {
    return (TileView) boardGameScene.getScene().lookup("#tile" + tileId);
  }

  private void initializeTurnOrder() {
    if (!turnOrder.isEmpty()) {
      return;
    }
    List<Player> players = playerService.getPlayers();
    if (players.isEmpty()) {
      logger.log(Level.SEVERE, "No players in the game");
      return;
    }
    players.sort(Comparator.comparing(Player::getAge));
    turnOrder.addAll(players);
    boardGameScene.setCurrentPlayer(turnOrder.get(currentIndex));
  }

  private void finishPlayer(Player player) {
    logger.log(Level.INFO, player.getName() + " finished");
    finishedPlayers.add(player);
    turnOrder.remove(currentIndex);

    boardGameScene.onEvent(new BoardGameEvent(BoardGameEventType.PLAYER_FINISHED, player, null,
        new Tile(player.getCurrentTileId())));

    if (!turnOrder.isEmpty()) {
      if (currentIndex >= turnOrder.size()) {
        currentIndex = 0;
      }
      boardGameScene.setCurrentPlayer(turnOrder.get(currentIndex));
    } else {
      boardGameScene.setCurrentPlayer(null);
    }

    if (turnOrder.isEmpty()) {
      PodiumGameScene.setFinalRanking(finishedPlayers);
      boardGameScene.onEvent(new BoardGameEvent(BoardGameEventType.GAME_FINISHED, player, null,
          new Tile(player.getCurrentTileId())));
    } else if (currentIndex >= turnOrder.size()) {
      currentIndex = 0;
    }
  }

  protected void advanceToNextPlayer() {
    currentIndex = (currentIndex + 1) % turnOrder.size();
    boardGameScene.setCurrentPlayer(turnOrder.get(currentIndex));
  }

  protected void afterTurnLogic(Player current) {
    advanceToNextPlayer();
  }
}