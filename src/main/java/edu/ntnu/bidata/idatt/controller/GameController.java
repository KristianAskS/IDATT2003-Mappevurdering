package edu.ntnu.bidata.idatt.controller;

import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEvent;
import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEventType;
import edu.ntnu.bidata.idatt.controller.patterns.observer.interfaces.BoardGameObserver;
import edu.ntnu.bidata.idatt.controller.rules.GameRules;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Dice;
import edu.ntnu.bidata.idatt.model.entity.Die;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.model.logic.action.BackToStartAction;
import edu.ntnu.bidata.idatt.model.logic.action.LadderAction;
import edu.ntnu.bidata.idatt.model.logic.action.SkipTurnAction;
import edu.ntnu.bidata.idatt.model.service.BoardService;
import edu.ntnu.bidata.idatt.model.service.PlayerService;
import edu.ntnu.bidata.idatt.view.components.GameUiAnimator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Abstract base class for handling the game flow shared by all board games
 * Responsibilities include:
 * <ul>
 *   <li>Maintaining the board, dice, and rule set.</li>
 *   <li>Animating token movement via {@link GameUiAnimator}.</li>
 *   <li>Publishing {@link BoardGameEvent}s to registered {@link BoardGameObserver}s.</li>
 * </ul>
 *
 * @author Tri Tac Le
 * @version 1.2
 * @since 1.0
 */
public abstract class GameController {

  protected final Logger logger = Logger.getLogger(getClass().getName());
  protected final PlayerService playerService = new PlayerService();
  protected final BoardService boardService = new BoardService();
  protected final Board board;
  protected final Dice dice;
  protected final Die die;
  final GameRules gameRules;
  private final Map<Player, Integer> skipTurnMap = new HashMap<>();
  private final List<Player> turnOrder = new ArrayList<>();
  private final List<Player> finishedPlayers = new ArrayList<>();
  private final List<BoardGameObserver> observers = new ArrayList<>();
  private GameUiAnimator animator;
  private int currentIndex = 0;

  /**
   * Constructs a new GameController.
   *
   * @param board        the game board instance
   * @param numberOfDice how many dice are used
   * @param gameRules    the rule set implementation
   */
  protected GameController(Board board,
                           int numberOfDice,
                           GameRules gameRules) {
    this.board = board;
    this.dice = new Dice(numberOfDice);
    this.die = new Die();
    this.gameRules = gameRules;
    boardService.setBoard(board);
  }

  /**
   * Translates a Tile model into its (row, column) position on the GridPane.
   *
   * @param tile  the tile to translate
   * @param board the board containing that tile
   * @return an array of rows and cols
   */
  public abstract int[] tileToGridPosition(Tile tile, Board board);

  /**
   * Injects the UI animator to handle token movement and animations.
   *
   * @param animator the GameUiAnimator instance
   */
  public void setAnimator(GameUiAnimator animator) {
    this.animator = animator;
  }

  /**
   * Registers an observer to receive {@link BoardGameEvent}s.
   *
   * @param observer the observer to add
   */
  public void addObserver(BoardGameObserver observer) {
    observers.add(observer);
  }

  /**
   * Unregisters an observer so it no longer receives game events.
   *
   * @param observer the observer to remove
   */
  public void removeObserver(BoardGameObserver observer) {
    observers.remove(observer);
  }

  /**
   * Notifies all registered observers of the given event.
   *
   * @param event the event to publish
   */
  private void notifyObservers(BoardGameEvent event) {
    for (var observer : observers) {
      observer.onEvent(event);
    }
  }

  /**
   * Determines whether the given player has reached
   * the final tile and thus should finish the game.
   *
   * @param player the player to check
   * @return true if the player’s tile index is at or beyond the last tile
   */
  protected boolean shouldFinish(Player player) {
    return player.getCurrentTileId() >= board.getTiles().size();
  }

  /**
   * Initializes the players at the start of the game:
   * <ol>
   *   <li>Resets each player’s current tile to 0 (staging area).</li>
   *   <li>Registers them with the PlayerService.</li>
   *   <li>Fires a {@link BoardGameEventType#GAME_STARTED} event.</li>
   *   <li>Sorts them into turn order (by age) and fires the first
   *       {@link BoardGameEventType#CURRENT_PLAYER_CHANGED}.</li>
   * </ol>
   *
   * @param players the list of players to initialize
   */
  public void initializePlayers(List<Player> players) {
    players.forEach(p -> p.setCurrentTileId(0));
    playerService.setPlayers(players);

    notifyObservers(new BoardGameEvent(
        BoardGameEventType.GAME_STARTED,
        null, null, null, null));

    initializeTurnOrder();

    notifyObservers(new BoardGameEvent(
        BoardGameEventType.CURRENT_PLAYER_CHANGED,
        turnOrder.get(currentIndex),
        null, null, null));
  }

  /**
   * Builds the initial turnOrder list by sorting registered players by age.
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
  }

  /**
   * Getter for a single {@link Die} instance
   *
   * @return the single {@link Die} instance
   */
  public Die getDie() {
    return die;
  }

  /**
   * Handle a single player’s turn:
   * <ol>
   *   <li>Rolls the dice and sets the result.</li>
   *   <li>Animates the token, then fires a {@link BoardGameEventType#PLAYER_MOVED} event.</li>
   *   <li>Applies any land action and fires the corresponding event.</li>
   *   <li>If the player finishes, fires {@link BoardGameEventType#PLAYER_FINISHED}
   *       followed by {@link BoardGameEventType#GAME_FINISHED}</li>
   * </ol>
   *
   * @param steps the number rolled on the dice
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

      notifyObservers(new BoardGameEvent(
          BoardGameEventType.PLAYER_MOVED,
          player,
          originId == 0 ? null : board.getTile(originId),
          landed,
          null));

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
   * Checks and executes any special action on landing:
   * <ul>
   *   <li>{@link BackToStartAction}</li>
   *   <li>{@link SkipTurnAction}</li>
   *   <li>{@link LadderAction}</li>
   * </ul>
   *
   * @param player the player who just landed
   * @param landed the tile landed upon
   * @param onDone callback function
   */
  protected void applyLandAction(Player player, Tile landed, Runnable onDone) {
    var action = landed.getLandAction();
    if (action != null) {
      switch (action) {
        case BackToStartAction back -> {
          back.perform(player);
          animator.animateLadderMovement(player, landed.getTileId(), 1, () -> {
            notifyObservers(new BoardGameEvent(
                BoardGameEventType.PLAYER_BACK_START_ACTION,
                player, landed, board.getTile(1), null));
            onDone.run();
          });
          return;
        }
        case SkipTurnAction skipAct -> {
          skipTurnMap.merge(player, skipAct.turnsToSkip(), Integer::sum);
          notifyObservers(new BoardGameEvent(
              BoardGameEventType.PLAYER_SKIP_TURN_ACTION,
              player, landed, landed, null));
          onDone.run();
          return;
        }
        case LadderAction la -> {
          int dest = la.getDestinationTileId();
          la.perform(player);
          animator.animateLadderMovement(player, landed.getTileId(), dest, () -> {
            notifyObservers(new BoardGameEvent(
                BoardGameEventType.PLAYER_LADDER_ACTION,
                player, landed, board.getTile(dest), null));
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
   * Handles when a player finishes
   *
   * @param player the player who just finished
   */
  private void finishPlayer(Player player) {
    logger.log(Level.INFO, player.getName() + " finished");
    finishedPlayers.add(player);
    turnOrder.remove(currentIndex);

    notifyObservers(new BoardGameEvent(
        BoardGameEventType.PLAYER_FINISHED,
        player, null,
        new Tile(player.getCurrentTileId()),
        null));

    if (!turnOrder.isEmpty()) {
      if (currentIndex >= turnOrder.size()) {
        currentIndex = 0;
      }
      notifyObservers(new BoardGameEvent(
          BoardGameEventType.CURRENT_PLAYER_CHANGED,
          turnOrder.get(currentIndex),
          null, null, null));
    } else {
      notifyObservers(new BoardGameEvent(
          BoardGameEventType.CURRENT_PLAYER_CHANGED,
          null, null, null, null));

      notifyObservers(new BoardGameEvent(
          BoardGameEventType.GAME_FINISHED,
          null, null, null,
          List.copyOf(finishedPlayers)));
    }
  }

  /**
   * Advances the turn index to the next player in the list
   * and fires {@link BoardGameEventType#CURRENT_PLAYER_CHANGED}.
   */
  protected void advanceToNextPlayer() {
    currentIndex = (currentIndex + 1) % turnOrder.size();
    notifyObservers(new BoardGameEvent(
        BoardGameEventType.CURRENT_PLAYER_CHANGED,
        turnOrder.get(currentIndex),
        null, null, null));
  }

  protected void afterTurnLogic(Player current) {
    advanceToNextPlayer();
  }

  /**
   * Getter for the board
   *
   * @return the game board
   */
  public Board getBoard() {
    return board;
  }
}
