package edu.ntnu.bidata.idatt.view.components;

import static edu.ntnu.bidata.idatt.view.components.TileView.TILE_SIZE;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 * Utility class for rendering a Board as a JavaFX GridPane.
 */
public class BoardView {
  private static final int COLUMNS = 10;
  private static final Logger logger = Logger.getLogger(BoardView.class.getName());

  private BoardView() {
  }

  /**
   * Generates a GridPane visual for a given board.
   *
   * @param board the board model
   * @return a JavaFX GridPane representing the board
   */
  public static GridPane createBoardGUI(Board board) {
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    int totalTiles = board.getTiles().size();
    int rows = (int) Math.ceil(totalTiles / (double) COLUMNS);

    for (int tileId = 1; tileId <= totalTiles; tileId++) {
      Tile tile = board.getTile(tileId);
      TileView tileView = new TileView(tile, TILE_SIZE);

      String color = (tileId % 2 == 0 ? "#1E90FF" : "#FFA500");
      tileView.setStyle("-fx-background-color: " + color + ";");

      if (tile.getLandAction() != null) {
        logger.log(Level.INFO, "tile.getLandAction() != null");
        String description = tile.getLandAction().getDescription();
        description = description.length() < 7 ? description : "";

        tileView.addTileActionViewLbl(description, Color.RED);
      }


      int rowFromBottom = (tileId - 1) / COLUMNS;
      int col = (tileId - 1) % COLUMNS;

      if (rowFromBottom % 2 == 1) {
        col = COLUMNS - 1 - col;
      }

      int gridRow = rows - 1 - rowFromBottom;

      grid.add(tileView, col, gridRow);
    }

    grid.setStyle("-fx-border-width: 2; -fx-border-color: black;");
    return grid;
  }

  /**
   * Utility method to get a tile node from a grid at a specific position (in grid pane).
   *
   * @param grid the grid pane
   * @param row  the row index
   * @param col  the column index
   * @return the node at that location, or null if not found
   */
  public static Node getTileNodeAt(GridPane grid, int row, int col) {
    for (Node node : grid.getChildren()) {
      if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
        return node;
      }
    }
    return null;
  }
}
