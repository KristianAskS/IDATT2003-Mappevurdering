package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.controller.patterns.factory.BoardGameFactory;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Ladder;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.model.logic.action.LadderAction;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class LadderView {
  private static final Logger logger = Logger.getLogger(LadderView.class.getName());
  private static final Set<Integer> tileIdsWithLadders = new HashSet<>();

  public static Set<Integer> getTileIdsWithLadders() {
    return tileIdsWithLadders;
  }

  //TODO: move this method to another logic class
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

  public static void generateLadder(Board board, GridPane boardGridPane, Pane ladderOverlayPane) {
    tileIdsWithLadders.clear();
    int totalLadders = 5; // TODO: make static or argument

    for (int i = 0; i < totalLadders; i++) {
      int startId = (int) (Math.random() * 88) + 1;
      int endId = startId + 1 + (int) (Math.random() * (88 - startId));

      if (isValidLadder(startId, endId)) {
        BoardGameFactory.createLadder(board, startId, endId);

        Tile start = board.getTile(startId);
        Tile end = board.getTile(endId);
        tileIdsWithLadders.add(startId);
        tileIdsWithLadders.add(endId);
        Ladder ladder = new Ladder(start, end, board, boardGridPane);
        ladderOverlayPane.getChildren().addAll(ladder.getLadders());

        start.setNextTile(end);
        start.setLandAction(
            new LadderAction(end.getNextTileId(), "Ladder from " + startId + " to " + endId));
      } else {
        i--;
      }
    }
  }

  private static boolean isValidLadder(int startId, int endId) {
    if (startId == endId) {
      return false;
    }
    if (tileIdsWithLadders.contains(startId)
        || tileIdsWithLadders.contains(endId)) {
      return false;
    }
    int rowStart = (startId - 1) / 10;
    int rowEnd = (endId - 1) / 10;
    return rowStart != rowEnd;
  }
}
