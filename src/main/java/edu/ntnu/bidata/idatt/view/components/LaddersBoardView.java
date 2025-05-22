package edu.ntnu.bidata.idatt.view.components;

import static edu.ntnu.bidata.idatt.view.components.TileView.TILE_SIZE_LADDER;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.layout.GridPane;

/**
 * <p>Renders a {@link Board} for snakes & ladders game, alternating the direction of each row.</p>
 *
 * @author Tri Tac Le
 * @since 1.0
 */
public class LaddersBoardView extends BaseBoardView {
  private static final int COLUMNS = 10;
  private static final Logger LOG = Logger.getLogger(LaddersBoardView.class.getName());

  /**
   * Default constructor
   */
  public LaddersBoardView() {
  }

  /**
   * {@inheritDoc}
   *
   * <p>Lays out the board’s tiles:
   * <ul>
   *   <li>Computes the number of rows from the total tile count.</li>
   *   <li>Alternates column direction each row: left to right, then right to left.</li>
   *   <li>Logs an INFO message for any tile that has a landing action.</li>
   * </ul>
   *
   * @param board the {@link Board} to render
   * @param grid  the {@link GridPane} to add {@link TileView}
   */
  @Override
  protected void layout(Board board, GridPane grid) {
    int totalTiles = board.getTiles().size();
    int rows = (int) Math.ceil(totalTiles / (double) COLUMNS);

    for (int id = 1; id <= totalTiles; id++) {
      Tile tile = board.getTile(id);
      TileView view = new TileView(tile, TILE_SIZE_LADDER);

      String color = (id % 2 == 0 ? "#1E90FF" : "#FFA500");
      view.setStyle("-fx-background-color:" + color + ";");

      if (tile.getLandAction() != null) {
        int finalId = id;
        LOG.log(Level.INFO, () -> "Added action label to tile " + finalId);
      }

      int rowIndexFromBottom = (id - 1) / COLUMNS;
      int colIndex = (id - 1) % COLUMNS;
      if (rowIndexFromBottom % 2 == 1) {
        colIndex = COLUMNS - 1 - colIndex;
      }
      int gridRow = rows - 1 - rowIndexFromBottom;

      grid.add(view, colIndex, gridRow);
    }
  }
}
