package edu.ntnu.bidata.idatt.controller;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.service.BoardService;
import edu.ntnu.bidata.idatt.model.service.PlayerService;
import edu.ntnu.bidata.idatt.view.components.TileView;
import edu.ntnu.bidata.idatt.view.scenes.BoardGameScene;

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
  private final BoardGameScene view;
  private final BoardService boardService;
  private final PlayerService playerService;
  private final Board board;

  //Konstruktør. Controlleren får referanser til viewet og de nødvendige modell-tjenestene.
  public BoardGameController(BoardGameScene view, BoardService boardService,
                             PlayerService playerService, Board board) {
    this.view = view;
    this.boardService = boardService;
    this.playerService = playerService;
    this.board = board;
    initController();
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
   * @param steps  Antall steg spilleren skal flyttes
   */
  public void movePlayer(Player player, int steps) {
    int nextTileId = player.getCurrentTileId() + steps;
    if (nextTileId > board.getTiles().size()) {
      nextTileId = board.getTiles().size();
    }

    player.setCurrentTileId(nextTileId);

    TileView nextTileView = (TileView) view.getScene().lookup("#tile" + nextTileId);
    if (nextTileView != null) {
      nextTileView.getChildren().add(player.getToken());
      view.setTokenPositionOnTile(nextTileView);
    }
  }

  /* TODO:
  Andre metoder for view-relaterte operasjoner kan legges til her:
  ex: metoder for å håndtere btn klikk eller andre events, vise dialoger etc.
   */
}
