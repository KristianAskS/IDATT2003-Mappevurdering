package edu.ntnu.bidata.idatt.view.components;

import static edu.ntnu.bidata.idatt.view.components.TileView.TILE_SIZE_LADDER;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class LaddersBoardView extends BaseBoardView {

  private static final int COLUMNS = 10;
  private static final Logger LOG = Logger.getLogger(LaddersBoardView.class.getName());

  public LaddersBoardView() {
  }

  @Override
  protected void layout(Board board, GridPane grid) {
    int total = board.getTiles().size();
    int rows = (int) Math.ceil(total / (double) COLUMNS);

    for (int id = 1; id <= total; id++) {
      Tile tile = board.getTile(id);
      TileView view = new TileView(tile, TILE_SIZE_LADDER);

      String color = (id % 2 == 0 ? "#1E90FF" : "#FFA500");
      view.setStyle("-fx-background-color:" + color + ";");

      if (tile.getLandAction() != null) {
        String desc = tile.getLandAction().getDescription();
        desc = desc.length() < 7 ? desc : ""; // keep short
        view.addTileActionViewLbl(desc, Color.RED);
        int finalId = id;
        LOG.log(Level.INFO, () -> "Added action label to tile " + finalId);
      }

      int rowFromBottom = (id - 1) / COLUMNS;
      int col = (id - 1) % COLUMNS;
      if (rowFromBottom % 2 == 1) {
        col = COLUMNS - 1 - col;
      }
      int gridRow = rows - 1 - rowFromBottom;

      grid.add(view, col, gridRow);
    }
  }
}
