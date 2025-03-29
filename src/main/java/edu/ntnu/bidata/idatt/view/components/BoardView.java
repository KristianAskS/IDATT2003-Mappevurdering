package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.model.Board;
import edu.ntnu.bidata.idatt.model.Tile;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

public class BoardView {
  private BoardView() {
  }

  public static GridPane createBoardGUI(Board board) {
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);

    int columns = 10;
    int totalTiles = board.getTiles().size();
    int rows = (int) Math.ceil(totalTiles / (double) columns);

    for (int tileId = 1; tileId <= totalTiles; tileId++) {
      Tile tile = board.getTileId(tileId);
      TileView tileView = new TileView(tile, TileView.TILE_SIZE);

      if (tileId % 2 == 0) {
        //tileView.setStyle("-fx-background-color: #004DFF"); //1
        tileView.setStyle("-fx-background-color: #1E90FF");
      } else {
        //tileView.setStyle("-fx-background-color: #FF00D4"); //1
        tileView.setStyle("-fx-background-color: #FFA500");
      }

      int bottomRow = (tileId - 1) / columns;
      int col = (tileId - 1) % columns;

      if (bottomRow % 2 == 1) {
        col = columns - 1 - col;
      }

      int gridRow = rows - 1 - bottomRow;

      grid.add(tileView, col, gridRow);
    }

    grid.setStyle("-fx-border-width: 2; -fx-border-color: black;");

    return grid;
  }
}
