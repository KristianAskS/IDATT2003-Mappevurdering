package edu.ntnu.bidata.idatt.controller.patterns.factory;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import java.util.stream.IntStream;

public abstract class BoardFactory {
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

  public abstract Board createDefaultBoard();
}
