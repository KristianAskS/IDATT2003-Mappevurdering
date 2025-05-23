package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.model.entity.Board;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

/**
 * <p>Abstract class for rendering a {@link Board} object into a {@link GridPane}.</p>
 *
 * @author Tri Tac Le
 * @since 1.0
 */
public abstract class BaseBoardView {

  /**
   * Finds and returns the node at the specified row and column within the given grid (rows and cols).
   *
   * @param grid the {@link GridPane} to search
   * @param row  the row index
   * @param col  the column index
   * @return the {@link Node} at (row, col), or {@code null} if none is found
   */
  public static Node getTileNodeAt(GridPane grid, int row, int col) {
    for (Node node : grid.getChildren()) {
      Integer r = GridPane.getRowIndex(node);
      Integer c = GridPane.getColumnIndex(node);
      if (r != null && c != null && r == row && c == col) {
        return node;
      }
    }
    return null;
  }

  /**
   * Constructs a {@link GridPane} representing the given {@link Board}.
   *
   * <p>Centers the grid in its container, delegates to {@link #layout(Board, GridPane)}
   * for placing tiles, then applies default styling via {@link #decorate(GridPane)}.</p>
   *
   * @param board the board model to render
   * @return a populated and styled {@code GridPane} for ui display
   */
  public final GridPane createBoardGUI(Board board) {
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    layout(board, grid);
    decorate(grid);
    return grid;
  }

  /**
   * Abstract protected method for laying out the board’s tiles into the grid.
   *
   * @param board the board model
   * @param grid  the {@link GridPane} to populate with tiles
   */
  protected abstract void layout(Board board, GridPane grid);

  /**
   * Applies default decoration to the grid.
   * <p>Subclasses may override to provide different styling.</p>
   *
   * @param grid the {@link GridPane} to style
   */
  protected void decorate(GridPane grid) {
    grid.setStyle("-fx-border-width: 2; -fx-border-color: black;");
  }
}
