// src/main/java/edu/ntnu/bidata/idatt/controller/BoardGameController.java

package edu.ntnu.bidata.idatt.controller;

import static edu.ntnu.bidata.idatt.model.service.BoardService.BOARD_FILE_PATH;

import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEvent;
import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEventType;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Dice;
import edu.ntnu.bidata.idatt.model.entity.Die;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.model.service.BoardService;
import edu.ntnu.bidata.idatt.model.service.PlayerService;
import edu.ntnu.bidata.idatt.view.components.TileView;
import edu.ntnu.bidata.idatt.view.scenes.BoardGameScene;
import edu.ntnu.bidata.idatt.view.scenes.PodiumGameScene;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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

  private final PlayerService playerService;
  private final BoardService boardService;
  private final BoardGameScene boardGameScene;
  private final Board board;
  private final Dice dice;
  private final Die die;

  private final List<Player> turnOrder = new ArrayList<>();
  private final List<Player> finishedPlayers = new ArrayList<>();
  private int currentPlayerIndex = 0;

  public BoardGameController(BoardGameScene boardGameScene, Board board, int numberOfDice)
      throws IOException {
    this.boardGameScene = boardGameScene;
    this.playerService = new PlayerService();
    this.boardService = new BoardService();
    this.board = board;
    this.dice = new Dice(numberOfDice);
    this.die = new Die();

    boardService.setBoard(board);
    boardService.writeBoardToFile(List.of(board), BOARD_FILE_PATH);
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

  public void initializePlayers(List<Player> players) {
    players.forEach(player -> player.setCurrentTileId(0));
    playerService.setPlayers(players);
    boardGameScene.setupPlayersUI(players);
  }

  public int getLastRolledValue() {
    return die.getLastRolledValue();
  }

  public void handlePlayerTurn(int steps) {
    ensureTurnOrderInitialized();
    dice.setRollResult(steps);

    if (turnOrder.isEmpty()) {
      return;
    }
    Player player = turnOrder.get(currentPlayerIndex);
    int originId = player.getCurrentTileId();
    Tile originTile = originId == 0 ? null : board.getTile(originId);

    movePlayerAlongTiles(player, steps, () -> {
      int landedTileId = player.getCurrentTileId();
      Tile landedTile = board.getTile(landedTileId);

      boardGameScene.onEvent(new BoardGameEvent(
          BoardGameEventType.PLAYER_MOVED, player, originTile, landedTile
      ));

      if (landedTile.getLandAction() != null) {
        int destinationTileId = landedTile.getLandAction().getDestinationTileId();
        landedTile.getLandAction().perform(player);
        animateLadderMovement(player, landedTileId, destinationTileId, () -> {
          boardGameScene.onEvent(new BoardGameEvent(
              BoardGameEventType.PLAYER_LADDER_ACTION,
              player, landedTile, board.getTile(destinationTileId)
          ));
          advanceOrFinish(player);
        });
      } else {
        advanceOrFinish(player);
      }
    });
  }

  private void ensureTurnOrderInitialized() {
    if (!turnOrder.isEmpty()) {
      return;
    }
    List<Player> players = playerService.getPlayers();
    if (players.isEmpty()) {
      LOGGER.log(Level.SEVERE, "No players in the game");
      return;
    }
    players.sort(Comparator.comparing(Player::getAge));
    turnOrder.addAll(players);
  }

  private void advanceOrFinish(Player player) {
    if (player.getCurrentTileId() >= board.getTiles().size()) {
      finishPlayer(player);
    } else {
      currentPlayerIndex = (currentPlayerIndex + 1) % turnOrder.size();
    }
  }

  private void finishPlayer(Player player) {
    LOGGER.log(Level.INFO, "{0} finished", player.getName());
    finishedPlayers.add(player);
    turnOrder.remove(currentPlayerIndex);

    boardGameScene.onEvent(new BoardGameEvent(
        BoardGameEventType.PLAYER_FINISHED, player, null, new Tile(player.getCurrentTileId())
    ));

    if (turnOrder.isEmpty()) {
      PodiumGameScene.setFinalRanking(finishedPlayers);
      boardGameScene.onEvent(new BoardGameEvent(
          BoardGameEventType.GAME_FINISHED, player, null, new Tile(player.getCurrentTileId())
      ));
    } else if (currentPlayerIndex >= turnOrder.size()) {
      currentPlayerIndex = 0;
    }
  }

  private void movePlayerAlongTiles(Player player, int steps, Runnable onDoneCallback) {
    int startTileId = player.getCurrentTileId();
    int targetTileId = Math.min(startTileId + steps, board.getTiles().size());
    Node token = player.getToken();

    if (token == null) {
      player.setCurrentTileId(targetTileId);
      onDoneCallback.run();
      return;
    }

    SequentialTransition sequentialTransition = new SequentialTransition();
    for (int next = startTileId + 1; next <= targetTileId; next++) {
      sequentialTransition.getChildren().add(getHopTransition(player, next, token));
    }
    sequentialTransition.setOnFinished(event -> onDoneCallback.run());
    sequentialTransition.play();
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

  private void animateLadderMovement(Player player, int fromTileId, int toTileId,
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

    var trans = new PathTransition(Duration.seconds(0.5), path, token);
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
}
