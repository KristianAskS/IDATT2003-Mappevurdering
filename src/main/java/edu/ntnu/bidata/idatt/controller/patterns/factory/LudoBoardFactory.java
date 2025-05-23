package edu.ntnu.bidata.idatt.controller.patterns.factory;

import edu.ntnu.bidata.idatt.model.entity.Board;

/**
 * <p>Factory for creating a standard Ludo board.</p>
 * <p>Extends {@link BoardFactory} to generate a 52‑tile board using the
 * {@link #createBoardTiles(String, String, int)} helper method.</p>
 *
 * @author Tri Tac Le
 * @version 1.2
 * @since 1.0
 */
public class LudoBoardFactory extends BoardFactory {

  /**
   * {@inheritDoc}
   *
   * <p>Creates a "Classic Ludo" board with 52 sequentially linked tiles.</p>
   *
   * @return a {@link Board} named "Classic Ludo" with 52 tiles
   */
  @Override
  public Board createDefaultBoard() {
    return createBoardTiles("Classic Ludo", "Standard 52-tile board", 52);
  }
}
