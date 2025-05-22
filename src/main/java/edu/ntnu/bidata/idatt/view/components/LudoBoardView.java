package edu.ntnu.bidata.idatt.view.components;

import static edu.ntnu.bidata.idatt.view.components.TileView.TILE_SIZE_LUDO;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class LudoBoardView extends BaseBoardView {

  private static final int[][] TILES_PATH = {
      {0, 7}, {0, 8}, {1, 8}, {2, 8}, {3, 8}, {4, 8}, {5, 8},
      {6, 9}, {6, 10}, {6, 11}, {6, 12}, {6, 13}, {6, 14},
      {7, 14}, {8, 14}, {8, 13}, {8, 12}, {8, 11}, {8, 10}, {8, 9},
      {9, 8}, {10, 8}, {11, 8}, {12, 8}, {13, 8}, {14, 8},
      {14, 7}, {14, 6}, {13, 6}, {12, 6}, {11, 6}, {10, 6}, {9, 6},
      {8, 5}, {8, 4}, {8, 3}, {8, 2}, {8, 1}, {8, 0},
      {7, 0}, {6, 0}, {6, 1}, {6, 2}, {6, 3}, {6, 4}, {6, 5},
      {5, 6}, {4, 6}, {3, 6}, {2, 6}, {1, 6}, {0, 6}
  };

  private static final int GRID = 15;

  private static final Color BLUE = Color.web("#2196F3");
  private static final Color GREEN = Color.web("#4CAF50");
  private static final Color YELLOW = Color.web("#FFEB3B");
  private static final Color RED = Color.web("#F44336");

  private static final int BLUE_SAFE = 52;
  private static final int GREEN_SAFE = 13;
  private static final int YELLOW_SAFE = 26;
  private static final int RED_SAFE = 39;

  @Override
  protected void layout(Board board, GridPane grid) {

    for (int row = 0; row < GRID; row++) {
      for (int col = 0; col < GRID; col++) {
        boolean centre = 6 <= row && row <= 8 && 6 <= col && col <= 8;
        Rectangle bg = new Rectangle(TILE_SIZE_LUDO, TILE_SIZE_LUDO, Color.WHITE);
        bg.setStroke(centre ? null : Color.BLACK);
        grid.add(bg, col, row);
      }
    }

    paintBlock(grid, 0, 0, GREEN);
    paintBlock(grid, 9, 0, BLUE);
    paintBlock(grid, 0, 9, YELLOW);
    paintBlock(grid, 9, 9, RED);

    paintStar(grid);

    paintColumn(grid, 7, 1, 5, BLUE);
    paintRow(grid, 1, 7, 5, GREEN);
    paintColumn(grid, 7, 9, 5, YELLOW);
    paintRow(grid, 9, 7, 5, RED);

    paintEntry(grid, 1, 8, BLUE);
    paintEntry(grid, 6, 1, GREEN);
    paintEntry(grid, 13, 6, YELLOW);
    paintEntry(grid, 8, 13, RED);

    for (int id = 1; id <= 52; id++) {
      Tile tile = board.getTile(id);
      if (tile == null) {
        continue;
      }
      int[] pos = TILES_PATH[id - 1];
      TileView tv = new TileView(tile, TILE_SIZE_LUDO);
      grid.add(tv, pos[1], pos[0]);
    }
  }


  private void paintBlock(GridPane grid, int col, int row, Color color) {
    for (int r = row; r < row + 6; r++) {
      for (int c = col; c < col + 6; c++) {
        Rectangle sq = new Rectangle(TILE_SIZE_LUDO, TILE_SIZE_LUDO, color);
        sq.setStroke(Color.BLACK);
        grid.add(sq, c, r);
      }
    }
  }

  private void paintColumn(GridPane grid, int col, int startRow, int len, Color color) {
    for (int row = startRow; row < startRow + len; row++) {
      Rectangle sq = new Rectangle(TILE_SIZE_LUDO, TILE_SIZE_LUDO, color);
      sq.setStroke(Color.BLACK);
      grid.add(sq, col, row);
    }
  }

  private void paintRow(GridPane grid, int startCol, int row, int len, Color color) {
    for (int c = startCol; c < startCol + len; c++) {
      Rectangle sq = new Rectangle(TILE_SIZE_LUDO, TILE_SIZE_LUDO, color);
      sq.setStroke(Color.BLACK);
      grid.add(sq, c, row);
    }
  }

  private void paintEntry(GridPane grid, int row, int col, Color colour) {
    Rectangle sq = new Rectangle(TILE_SIZE_LUDO, TILE_SIZE_LUDO, colour);
    sq.setStroke(Color.BLACK);
    grid.add(sq, col, row);
  }

  private void paintStar(GridPane grid) {
    double size = (double) TILE_SIZE_LUDO * 3;
    double mid = size / 2;

    Pane star = new Pane();
    star.setMinSize(size, size);
    star.setPrefSize(size, size);

    Polygon pBlue = new Polygon(0, 0, size, 0, mid, mid);
    Polygon pRed = new Polygon(size, 0, size, size, mid, mid);
    Polygon pYellow = new Polygon(size, size, 0, size, mid, mid);
    Polygon pGreen = new Polygon(0, size, 0, 0, mid, mid);

    pBlue.setFill(BLUE);
    pRed.setFill(RED);
    pYellow.setFill(YELLOW);
    pGreen.setFill(GREEN);

    star.getChildren().addAll(pBlue, pRed, pYellow, pGreen);

    GridPane.setRowSpan(star, 3);
    GridPane.setColumnSpan(star, 3);
    GridPane.setHalignment(star, HPos.CENTER);
    GridPane.setValignment(star, VPos.CENTER);

    grid.add(star, 6, 6);
  }

  @Override
  protected void decorate(GridPane grid) {
    grid.setGridLinesVisible(false);
    grid.setStyle("-fx-border-width: 2; -fx-border-color: black;");
  }
}