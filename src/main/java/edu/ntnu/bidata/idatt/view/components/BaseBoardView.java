package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.model.entity.Board;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

public abstract class BaseBoardView {

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

  public final GridPane createBoardGUI(Board board) {
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);
    layout(board, grid);
    decorate(grid);
    return grid;
  }

  protected abstract void layout(Board board, GridPane grid);

  protected void decorate(GridPane grid) {
    grid.setStyle("-fx-border-width: 2; -fx-border-color: black;");
  }
}
