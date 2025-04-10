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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * BoardGameController er ansvarlig for view-relaterte operasjoner,
 * Den kommuniserer med både viewet (BoardGameScene) og modell-tjenestene (BoardService, PlayerService)
 * uten å inneholde forretningslogikk.
 *
 * @author Trile
 * @version 1.0
 * @since 1.0
 */
public class BoardGameController {
  private static final Logger logger = Logger.getLogger(BoardGameController.class.getName());
  private static Dice dice = null;
  private static Die die = null;
  private final BoardService boardService;
  private final PlayerService playerService;
  private final Board board;
  private final BoardGameScene boardGameScene;
  private int currentPlayerIndex = 0;

  //Konstruktør. Controlleren får referanser til viewet og de nødvendige modell-tjenestene.
  public BoardGameController(BoardGameScene boardGameScene, BoardService boardService,
                             PlayerService playerService, Board board, int numbOfDices) {
    this.boardGameScene = boardGameScene;
    this.boardService = boardService;
    this.playerService = playerService;
    this.board = board;
    dice = new Dice(numbOfDices);
    die = new Die();

    initController();
  }

  /**
   * @return the roll value of a single dice
   */
  public static int getLastRolledValue() {
    return die.getLastRolledValue();
  }

  public static void setRolledValue(int rollResult) {
    dice.setRollResult(rollResult);
  }

  /**
   * Initialiserer controlleren, ex: ved å koble eventhandlers til view-komponenter.
   */
  private void initController() {
    //ex: på roll dice knapp, kan du knytte en event-handler:
    //view.getRollButton().setOnAction(e -> handleRollDice());
  }

  /**
   * Flytter en spillers token med et gitt antall steg.
   * Controlleren oppdaterer spillerens posisjon i modellen og deretter viewet.
   *
   * @param player Spilleren som skal flyttes
   */
  public void movePlayer(Player player, int steps) {
    int nextTileId = player.getCurrentTileId() + steps;
    TileView oldTileView =
        (TileView) boardGameScene.getScene().lookup("#tile" + player.getCurrentTileId());
    if (oldTileView != null) {
      oldTileView.getChildren().remove(player.getToken());
    }
    if (nextTileId > board.getTiles().size()) {
      nextTileId = board.getTiles().size();
    }

    player.setCurrentTileId(nextTileId);

    TileView nextTileView = (TileView) boardGameScene.getScene().lookup("#tile" + nextTileId);
    if (nextTileView != null) {
      nextTileView.getChildren().add(player.getToken());
      boardGameScene.setTokenPositionOnTile(nextTileView);
    }
  }

  public void handlePlayerTurn(int steps) {
    List<Player> players = playerService.getPlayers();
    if (players.isEmpty()) {
      logger.log(Level.INFO, "players is empty");
      return;
    }
    Player currentPlayer = players.get(currentPlayerIndex);
    movePlayer(currentPlayer, steps);

    if (currentPlayer.getCurrentTileId() >= board.getTiles().size()) {
      boardGameScene.onEvent(new BoardGameEvent(BoardGameEventType.GAME_FINISHED, currentPlayer,
          new Tile(currentPlayer.getCurrentTileId()),
          new Tile(currentPlayer.getCurrentTileId() + steps)));
    }

    boardGameScene.onEvent(
        new BoardGameEvent(BoardGameEventType.PLAYER_MOVED, currentPlayer,
            new Tile(currentPlayer.getCurrentTileId()),
            new Tile(currentPlayer.getCurrentTileId() + steps)));
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
  }

  /* TODO:
  Andre metoder for view-relaterte operasjoner kan legges til her:
  ex: metoder for å håndtere btn klikk eller andre events, vise dialoger etc.
   */
}
