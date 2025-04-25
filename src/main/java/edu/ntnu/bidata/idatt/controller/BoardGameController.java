package edu.ntnu.bidata.idatt.controller;

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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * BoardGameController is responsible for handling view-related operations during the board game.
 * It communicates with both the view (BoardGameScene) and the model services (BoardService, PlayerService)
 * without containing any business logic.
 *
 * @author Trile, Kristian Selmer
 * @version 1.0
 * @since 1.0
 */
public class BoardGameController {
  private static final Logger logger = Logger.getLogger(BoardGameController.class.getName());
  private static Dice dice = null;
  private static Die die = null;
  // private final BoardService boardService;
  private final PlayerService playerService;
  private final Board board;
  private final BoardGameScene boardGameScene;
  private final List<Player> turnOrder = new ArrayList<>();
  private final List<Player> finishedPlayers = new ArrayList<>();
  private int currentPlayerIndex = 0;

  //Konstruktør. Controlleren får referanser til viewet og de nødvendige modell-tjenestene.
  public BoardGameController(BoardGameScene boardGameScene,
      BoardService boardService,
      PlayerService playerService,
      Board board,
      int numberOfDice) {

    this.boardGameScene = boardGameScene;
    //this.boardService = boardService;
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

  /**
   * Initializes the turn order if it has not already been initialized by fetching players from the player service.
   */
  private void ensureTurnOrderInitialised() {
    if (turnOrder.isEmpty()) {
      List<Player> players = playerService.getPlayers();
      if (players.isEmpty()) {
        logger.log(Level.WARNING,
            "PlayerService is still empty – cannot start the game yet");
        return;
      }
      turnOrder.addAll(players);
      logger.log(Level.INFO, "Turn order initialised with {0} players", turnOrder.size());
    }
  }

  /**
   * Moves a player's token a given number of steps and updates the corresponding view.
   *
   * @param player The player to move
   * @param steps The number of steps to move
   */
  public void movePlayer(Player player, int steps) {
    int fromId = player.getCurrentTileId();
    int toId = Math.min(fromId + steps, board.getTiles().size());

    TileView oldView = (TileView) boardGameScene.getScene().lookup("#tile" + fromId);
    if (oldView != null) {
      oldView.getChildren().remove(player.getToken());
    }

    player.setCurrentTileId(toId);

    TileView newView = (TileView) boardGameScene.getScene().lookup("#tile" + toId);
    if (newView != null) {
      newView.getChildren().add(player.getToken());
      boardGameScene.setTokenPositionOnTile(newView);
    }
  }

  /**
   * Handles a player's turn by moving the player, checking if the player has finished,
   * updating the finished players list, and firing events to the view.
   *
   * @param steps The number of steps rolled by the dice
   */
  public void handlePlayerTurn(int steps) {

    ensureTurnOrderInitialised();
    if (turnOrder.isEmpty()) {
      logger.log(Level.INFO,
          "players is empty");
      return;
    }

    Player current = turnOrder.get(currentPlayerIndex);
    int fromId = current.getCurrentTileId();

    movePlayer(current, steps);
    int toId = current.getCurrentTileId();

    boardGameScene.onEvent(new BoardGameEvent(
        BoardGameEventType.PLAYER_MOVED, current,
        new Tile(fromId), new Tile(toId)));

    // If they reach the finish, they are finished
    if (toId >= board.getTiles().size()) {
      logger.log(Level.INFO, "{0} reached the finish!", current.getName());

      finishedPlayers.add(current);
      turnOrder.remove(currentPlayerIndex);

      boardGameScene.onEvent(new BoardGameEvent(
          BoardGameEventType.PLAYER_FINISHED, current,
          new Tile(fromId), new Tile(toId)));

      if (currentPlayerIndex >= turnOrder.size()) {
        currentPlayerIndex = 0;
      }

      if (turnOrder.isEmpty()) {
        PodiumGameScene.setFinalRanking(finishedPlayers);
        boardGameScene.onEvent(new BoardGameEvent(
            BoardGameEventType.GAME_FINISHED, current,
            new Tile(toId), new Tile(toId)));
      }

    } else {
      currentPlayerIndex = (currentPlayerIndex + 1) % turnOrder.size();
    }
  }

  /**
   * Returns the index of the player whose turn it currently is.
   *
   * @return The current player's index
   */
  public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
  }

  /* TODO:
  Andre metoder for view-relaterte operasjoner kan legges til her:
  ex: metoder for å håndtere btn klikk eller andre events, vise dialoger etc.
   */
}