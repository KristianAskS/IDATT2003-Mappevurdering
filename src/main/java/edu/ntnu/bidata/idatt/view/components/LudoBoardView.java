package edu.ntnu.bidata.idatt.view.components;

import static edu.ntnu.bidata.idatt.view.components.TileView.TILE_SIZE_LUDO;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class LudoBoardView extends BaseBoardView {
  private static final int[][] PATH = {
      // 1 2 3 4 5 6
      {0, 6}, {1, 6}, {2, 6}, {3, 6}, {4, 6}, {5, 6},
      // 7 8 9 10 11 12
      {6, 5}, {6, 4}, {6, 3}, {6, 2}, {6, 1}, {6, 0},
      // 13
      {7, 0},
      // 14 15 16 17 18 19
      {8, 0}, {8, 1}, {8, 2}, {8, 3}, {8, 4}, {8, 5},
      // 20 21 22 23 24 25
      {9, 6}, {10, 6}, {11, 6}, {12, 6}, {13, 6}, {14, 6},
      // 26
      {14, 7},
      // 27, 28, 29, 30, 31, 32
      {14, 8}, {13, 8}, {12, 8}, {11, 8}, {10, 8}, {9, 8},
      // 33, 34, 35 ,36, 37, 38
      {8, 9}, {8, 10}, {8, 11}, {8, 12}, {8, 13}, {8, 14},
      // 40
      {7, 14},
      // 41 42 43 44 45 46
      {6, 14}, {6, 13}, {6, 12}, {6, 11}, {6, 10}, {6, 9},
      // 47 48 49 50 51
      {5, 8}, {4, 8}, {3, 8}, {2, 8}, {1, 8}, {0, 8},
      // 52
      {0, 7}
  };


  private static final int GRID = 15;

  public LudoBoardView() {
  }

  @Override
  protected void layout(Board board, GridPane grid) {

    for (int i = 0; i < GRID; i++) {
      for (int u = 0; u < GRID; u++) {
        Rectangle tileBg = new Rectangle(TILE_SIZE_LUDO, TILE_SIZE_LUDO,
            Color.web("#F4F1E6"));
        grid.add(tileBg, i, u);
      }
    }

    paintHomeSquare(grid, 0, 0, Color.YELLOW);
    paintHomeSquare(grid, 9, 0, Color.GREEN);
    paintHomeSquare(grid, 0, 9, Color.RED);
    paintHomeSquare(grid, 9, 9, Color.BLUE);

    for (int id = 1; id <= 52; id++) {
      Tile tile = board.getTile(id);
      if (tile == null) {
        continue;
      }
      TileView tileView = new TileView(tile, TILE_SIZE_LUDO);
      int[] gridCoord = PATH[id - 1];
      grid.add(tileView, gridCoord[1], gridCoord[0]);
    }
  }


  private void paintHomeSquare(GridPane grid, int col, int row, Color color) {
    for (int r = row; r < row + 6; r++) {
      for (int c = col; c < col + 6; c++) {
        Rectangle sq = new Rectangle(TILE_SIZE_LUDO, TILE_SIZE_LUDO, color);
        grid.add(sq, c, r);
      }
    }
  }


  @Override
  protected void decorate(GridPane grid) {
    grid.setGridLinesVisible(true);
  }
}
