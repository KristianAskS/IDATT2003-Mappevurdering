package edu.ntnu.bidata.idatt.controller;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.logic.BoardGame;
import edu.ntnu.bidata.idatt.model.service.BoardService;
import edu.ntnu.bidata.idatt.model.service.PlayerService;
import edu.ntnu.bidata.idatt.view.scenes.BoardGameScene;

public class BoardGameController {
  private final BoardGame boardGame;
  private final BoardGameScene boardGameScene;

  public BoardGameController(BoardGameScene boardGameScene, BoardService boardService,
                             PlayerService playerService, Board board, int numbOfDices) {
    this.boardGameScene = boardGameScene;
    this.boardGame = new BoardGame(board, playerService.getPlayers(), numbOfDices);
    this.boardGame.addObserver(boardGameScene);
  }

  public void handlePlayerTurn() {
    boardGame.playTurn();
  }

  public int getCurrentPlayerIndex() {
    return boardGame.getCurrentPlayer() != null ? boardGame.getCurrentPlayer().getCurrentTileId() :
        -1;
  }
}
