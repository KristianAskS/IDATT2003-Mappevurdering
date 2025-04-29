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
      {0, 6}, {1, 6}, {2, 6}, {3, 6}, {4, 6}, {5, 6},
      {6, 5}, {6, 4}, {6, 3}, {6, 2}, {6, 1}, {6, 0},
      {7, 0},
      {8, 0}, {8, 1}, {8, 2}, {8, 3}, {8, 4}, {8, 5},
      {9, 6}, {10, 6}, {11, 6}, {12, 6}, {13, 6}, {14, 6},
      {14, 7},
      {14, 8}, {13, 8}, {12, 8}, {11, 8}, {10, 8}, {9, 8},
      {8, 9}, {8, 10}, {8, 11}, {8, 12}, {8, 13}, {8, 14},
      {7, 14},
      {6, 14}, {6, 13}, {6, 12}, {6, 11}, {6, 10}, {6, 9},
      {5, 8}, {4, 8}, {3, 8}, {2, 8}, {1, 8}, {0, 8},
      {0, 7}
  };

  private static final int GRID = 15;

  @Override
  protected void layout(Board board, GridPane grid) {
    for (int row = 0; row < GRID; row++) {
      for (int col = 0; col < GRID; col++) {
        boolean isCentre = 6 <= row && row <= 8 && 6 <= col && col <= 8;
        Rectangle bg = new Rectangle(TILE_SIZE_LUDO, TILE_SIZE_LUDO, Color.WHITE);
        bg.setStroke(isCentre ? null : Color.BLACK);
        grid.add(bg, col, row);
      }
    }

    // Four coloured home yards 6x6
    paintBlock(grid, 0, 9, Color.web("#FFEB3B"));   // Yellow
    paintBlock(grid, 0, 0, Color.web("#4CAF50"));   // Green
    paintBlock(grid, 9, 9, Color.web("#F44336"));   // Red
    paintBlock(grid, 9, 0, Color.web("#2196F3"));   // Blue

    // Home path
    paintColumn(grid, 7, 1, 5, Color.web("#2196F3")); // Blue up
    paintColumn(grid, 7, 9, 5, Color.web("#FFEB3B")); // Yellow down
    paintRow(grid, 1, 7, 5, Color.web("#4CAF50")); // Green left
    paintRow(grid, 9, 7, 5, Color.web("#F44336")); // Red right

    // Entry points
    paintEntry(grid, 1, 8, Color.web("#2196F3"));  // Blue
    paintEntry(grid, 6, 1, Color.web("#4CAF50"));  // Green
    paintEntry(grid, 13, 6, Color.web("#FFEB3B"));  // Yellow
    paintEntry(grid, 8, 13, Color.web("#F44336"));  // Red

    paintStar(grid);

    for (int id = 1; id <= 52; id++) {
      Tile tile = board.getTile(id);
      if (tile == null) {
        continue;
      }
      TileView view = new TileView(tile, TILE_SIZE_LUDO);
      int[] path = TILES_PATH[id - 1];
      grid.add(view, path[1], path[0]);
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
    for (int column = startCol; column < startCol + len; column++) {
      Rectangle sq = new Rectangle(TILE_SIZE_LUDO, TILE_SIZE_LUDO, color);
      sq.setStroke(Color.BLACK);
      grid.add(sq, column, row);
    }
  }

  private void paintStar(GridPane grid) {
    double cell = TILE_SIZE_LUDO;
    double size = cell * 3;
    double mid = size / 2;

    Pane star = new Pane();
    star.setMinSize(size, size);
    star.setPrefSize(size, size);
    star.setMaxSize(size, size);

    Polygon blue = new Polygon(0, 0, size, 0, mid, mid);
    Polygon red = new Polygon(size, 0, size, size, mid, mid);
    Polygon yellow = new Polygon(size, size, 0, size, mid, mid);
    Polygon green = new Polygon(0, size, 0, 0, mid, mid);

    blue.setFill(Color.web("#2196F3")); // Blue
    red.setFill(Color.web("#F44336")); // Red
    yellow.setFill(Color.web("#FFEB3B")); // Yellow
    green.setFill(Color.web("#4CAF50")); // Green

    star.getChildren().addAll(blue, red, yellow, green);

    GridPane.setRowSpan(star, 3);
    GridPane.setColumnSpan(star, 3);
    GridPane.setHalignment(star, HPos.CENTER);
    GridPane.setValignment(star, VPos.CENTER);

    grid.add(star, 6, 6);
  }


  private void paintEntry(GridPane grid, int row, int col, Color colour) {
    Rectangle sq = new Rectangle(TILE_SIZE_LUDO, TILE_SIZE_LUDO, colour);
    sq.setStroke(Color.BLACK);
    grid.add(sq, col, row);
  }

  @Override
  protected void decorate(GridPane grid) {
    grid.setGridLinesVisible(false);
    grid.setStyle("-fx-border-width: 2; -fx-border-color: black;");
  }
}
