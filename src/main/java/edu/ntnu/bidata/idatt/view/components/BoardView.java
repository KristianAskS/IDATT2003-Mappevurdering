package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

/**
 * @author Tri Le
 */
public class BoardView {
  private BoardView() {
  }

  /**
   * See the Excel sheet I made Kristian (Teams)
   *
   * @param board Collections of tiles
   * @return visual of the board filled with tiles
   */
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
        tileView.setStyle("-fx-background-color: #1E90FF");
      } else {
        tileView.setStyle("-fx-background-color: #FFA500");
      }
      // Which row the tile belong too from the bottom
      int rowFromBottom = (tileId - 1) / columns;
      // Column position
      int col = (tileId - 1) % columns;
      // Reversing column for odd rows
      if (rowFromBottom % 2 == 1) {
        col = columns - 1 - col;
      }

      // Flipping vertical ordering
      int gridRow = rows - 1 - rowFromBottom;

      grid.add(tileView, col, gridRow);
    }

    grid.setStyle("-fx-border-width: 2; -fx-border-color: black;");

    return grid;
  }
}
