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

  protected GameController(Board board,
                           int numberOfDice,
                           GameRules gameRules) {
    this.board = board;
    this.dice = new Dice(numberOfDice);
    this.die = new Die();
    this.gameRules = gameRules;
    boardService.setBoard(board);
  }

  public abstract int[] tileToGridPosition(Tile tile, Board board);

  public void setAnimator(GameUiAnimator animator) {
    this.animator = animator;
  }

  public void addObserver(BoardGameObserver o) {
    observers.add(o);
  }

  public void removeObserver(BoardGameObserver o) {
    observers.remove(o);
  }

  private void notifyObservers(BoardGameEvent event) {
    for (var o : observers) {
      o.onEvent(event);
    }
  }

  protected boolean shouldFinish(Player player) {
    return player.getCurrentTileId() >= board.getTiles().size();
  }

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

  public Die getDie() {
    return die;
  }

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

  protected void applyLandAction(Player player, Tile landed, Runnable onDone) {
    var action = landed.getLandAction();
    if (action != null) {
      switch (action) {
        case edu.ntnu.bidata.idatt.model.logic.action.BackToStartAction back -> {
          back.perform(player);
          animator.animateLadderMovement(player, landed.getTileId(), 1, () -> {
            notifyObservers(new BoardGameEvent(
                BoardGameEventType.PLAYER_BACK_START_ACTION,
                player, landed, board.getTile(1), null));
            onDone.run();
          });
          return;
        }
        case edu.ntnu.bidata.idatt.model.logic.action.SkipTurnAction skipAct -> {
          skipTurnMap.merge(player, skipAct.turnsToSkip(), Integer::sum);
          notifyObservers(new BoardGameEvent(
              BoardGameEventType.PLAYER_SKIP_TURN_ACTION,
              player, landed, landed, null));
          onDone.run();
          return;
        }
        case edu.ntnu.bidata.idatt.model.logic.action.LadderAction la -> {
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

  public Board getBoard() {
    return board;
  }
}
