package edu.ntnu.bidata.idatt.controller;

import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEvent;
import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEventType;
import edu.ntnu.bidata.idatt.controller.rules.GameRules;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Dice;
import edu.ntnu.bidata.idatt.model.entity.Die;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.model.service.BoardService;
import edu.ntnu.bidata.idatt.model.service.PlayerService;
import edu.ntnu.bidata.idatt.view.components.GameUiAnimator;
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


/**
 * Base controller that contains the core game‑flow logic that is shared across the different board
 * games in the application.
 */
public abstract class GameController {

  protected final Logger logger = Logger.getLogger(getClass().getName());
  protected final PlayerService playerService = new PlayerService();
  protected final BoardService boardService = new BoardService();
  protected final BoardGameScene boardGameScene;
  protected final Board board;
  protected final Dice dice;
  protected final Die die;
  final GameRules gameRules;

  private final Map<Player, Integer> skipTurnMap = new HashMap<>();
  private final GameUiAnimator animator;
  private final List<Player> turnOrder = new ArrayList<>();
  private final List<Player> finishedPlayers = new ArrayList<>();
  private int currentIndex = 0;

  /**
   * Constructs a new GameController.
   *
   * @param scene the scene to which the game UI will be added
   * @param board the board model
   * @param numberOfDice the number of dice to use
   * @param gameRules the game rules to apply
   * @throws IOException if an I/O error occurs
   */
  protected GameController(BoardGameScene scene,
                           Board board,
                           int numberOfDice,
                           GameRules gameRules) throws IOException {
    this.boardGameScene = scene;
    this.board = board;
    this.dice = new Dice(numberOfDice);
    this.die = new Die();
    this.gameRules = gameRules;
    this.animator = new GameUiAnimator(scene);
    boardService.setBoard(board);
  }

  /**
   * Converts a tile and board model to a grid position.
   *
   * @param tile the tile to convert
   * @param board the board model
   * @return the grid position of the tile
   */
  public abstract int[] tileToGridPosition(Tile tile, Board board);

  /**
   * Determines if the player should finish the game.
   *
   * @param player the player to check
   * @return true if the player should finish, false otherwise
   */
  protected boolean shouldFinish(Player player) {
    return player.getCurrentTileId() >= board.getTiles().size();
  }

  /**
   * Initializes the players in the game.
   *
   * @param players the players to initialize
   */
  public void initializePlayers(List<Player> players) {
    players.forEach(player -> player.setCurrentTileId(0));
    playerService.setPlayers(players);
    boardGameScene.setupPlayersUI(players);

    initializeTurnOrder();
  }

  /**
   * Returns the die.
   *
   * @return the die
   */
  public void handlePlayerTurn(int steps) {
    initializeTurnOrder();
    dice.setRollResult(steps);

    Player player = turnOrder.get(currentIndex);
    int skips = skipTurnMap.getOrDefault(player, 0);
    if (skips > 0) {
      skipTurnMap.put(player, skips - 1);
      advanceToNextPlayer();
      return;
    }

    if (!gameRules.canEnterTrack(player, steps)) {
      advanceToNextPlayer();
      return;
    }

    int maxId = board.getTiles().size();
    int destId = gameRules.destinationTile(player, steps, maxId);
    if (destId < 0) {
      advanceToNextPlayer();
      return;
    }

    int originId = player.getCurrentTileId();
    int hopCount = ((destId - originId) + maxId) % maxId;

    animator.movePlayerAlongTiles(player, hopCount, () -> {
      Tile landed = board.getTile(player.getCurrentTileId());
      boardGameScene.onEvent(new BoardGameEvent(
          BoardGameEventType.PLAYER_MOVED, player,
          originId == 0 ? null : board.getTile(originId), landed));
      applyLandAction(player, landed, () -> {
        if (shouldFinish(player)) {
          finishPlayer(player);
        } else {
          afterTurnLogic(player);
        }
      });
    });
  }


  /**
   * Initializes the turn order.
   */
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

  /**
   * Applies a land action to the player.
   *
   * @param player the player to apply the action to
   * @param landed the tile that was landed on
   * @param onDone a callback to execute after the action is performed
   */
  protected void applyLandAction(Player player, Tile landed, Runnable onDone) {
    var action = landed.getLandAction();
    if (action != null) {
      switch (action) {
        case edu.ntnu.bidata.idatt.model.logic.action.BackToStartAction back -> {
          back.perform(player);
          animator.animateLadderMovement(player, landed.getTileId(), 1, () -> {
            boardGameScene.onEvent(new BoardGameEvent(
                BoardGameEventType.PLAYER_BACK_START_ACTION,
                player, landed, board.getTile(1)));
            onDone.run();
          });
          return;
        }
        case edu.ntnu.bidata.idatt.model.logic.action.SkipTurnAction skipAct -> {
          skipTurnMap.merge(player, skipAct.turnsToSkip(), Integer::sum);
          boardGameScene.onEvent(new BoardGameEvent(
              BoardGameEventType.PLAYER_SKIP_TURN_ACTION,
              player, landed, landed));
          onDone.run();
          return;
        }
        case edu.ntnu.bidata.idatt.model.logic.action.LadderAction la -> {
          int dest = la.getDestinationTileId();
          la.perform(player);
          animator.animateLadderMovement(player, landed.getTileId(), dest, () -> {
            boardGameScene.onEvent(new BoardGameEvent(
                BoardGameEventType.PLAYER_LADDER_ACTION,
                player, landed, board.getTile(dest)));
            onDone.run();
          });
          return;
        }
        default -> {

        }
      }
    }
    if (action != null) {
      action.perform(player);
    }
    onDone.run();
  }

  /**
   * Finishes the player.
   *
   * @param player the player to finish
   */
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

  /**
   * Advances to the next player in the turn order.
   */
  protected void advanceToNextPlayer() {
    currentIndex = (currentIndex + 1) % turnOrder.size();
    boardGameScene.setCurrentPlayer(turnOrder.get(currentIndex));
  }

  /**
   * Called after a player's turn is completed.
   *
   * @param current the player who just completed their turn
   */
  protected void afterTurnLogic(Player current) {
    advanceToNextPlayer();
  }

  /**
   * Returns the board model.
   *
   * @return the board model
   */
  public Board getBoard() {
    return board;
  }
}