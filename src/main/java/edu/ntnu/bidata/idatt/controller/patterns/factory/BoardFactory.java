package edu.ntnu.bidata.idatt.controller.patterns.factory;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import java.util.stream.IntStream;

/**
 * <p>Abstract factory class for building {@link Board} instances composed of
 * linked {@link Tile} objects.</p>
 *
 * <p>Subclasses must implement {@link #createDefaultBoard()}.</p>
 *
 * @author Tri Tac Le
 * @version 1.1
 * @since 1.0
 */
public abstract class BoardFactory {

  /**
   * <p>Creates a new {@link Board} populated with {@code numberOfTiles}</p>
   *
   * <p>Tiles are numbered from 1 to {@code numberOfTiles}</p>
   *
   * @param name          the name of the board
   * @param description   a description of the board
   * @param numberOfTiles the total number of tiles
   * @return a {@link Board} populated and with tiles linked
   */
  protected Board createBoardTiles(String name, String description, int numberOfTiles) {
    Board board = new Board(name, description);

    IntStream.rangeClosed(1, numberOfTiles)
        .mapToObj(Tile::new)
        .forEach(board::addTile);

    IntStream.rangeClosed(1, numberOfTiles - 1)
        .forEach(i -> {
          Tile current = board.getTile(i);
          Tile next = board.getTile(i + 1);
          if (current != null && next != null) {
            current.setNextTile(next);
          }
        });

    return board;
  }

  /**
   * <p>Creates the default board for this factory.</p>
   *
   * @return a configured {@link Board} instance
   */
  public abstract Board createDefaultBoard();
}
