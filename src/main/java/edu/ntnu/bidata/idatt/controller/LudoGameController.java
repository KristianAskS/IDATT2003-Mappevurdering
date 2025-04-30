package edu.ntnu.bidata.idatt.controller;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.view.scenes.BoardGameScene;
import java.io.IOException;

public class LudoGameController extends GameController {

  public LudoGameController(BoardGameScene scene,
                            Board board,
                            int dice) throws IOException {
    super(scene, board, dice);
  }

  @Override
  public int[] tileToGridPosition(Tile tile, Board board) {
    return new int[] {0, tile.getTileId() - 1};
  }
}
