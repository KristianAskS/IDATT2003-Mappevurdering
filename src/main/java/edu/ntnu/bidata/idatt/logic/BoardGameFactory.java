package edu.ntnu.bidata.idatt.logic;

import edu.ntnu.bidata.idatt.model.Board;
import edu.ntnu.bidata.idatt.model.Tile;
import edu.ntnu.bidata.idatt.utils.BoardFileHandler;
import edu.ntnu.bidata.idatt.utils.GsonBoardFileHandler;
import java.io.IOException;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;

public class BoardGameFactory {
  public static Board createClassicBoard() {
    Board board = new Board();
    int numbOfTiles = 90;

    for (int i = 1; i <= numbOfTiles; i++) {
      Tile tile = new Tile(i);
      board.addTile(tile);
    }

    for (int i = 1; i <= 10; i++) {
      Tile currentTile = board.getTileId(i);
      Tile nextTile = board.getTileId(i + 1);
      if (currentTile != null && nextTile != null) {
        currentTile.setNextTile(nextTile);
      }
    }
    //Add landingAction
    return board;
  }

  public static GridPane createBoardGUI(Board board) {
    GridPane grid = new GridPane();
    grid.setAlignment(Pos.CENTER);

    int columns = 10;
    int totalTiles = board.getTiles().size();
    int rows = (int) Math.ceil(totalTiles / (double) columns);

    for (int tileId = 1; tileId <= totalTiles; tileId++) {
      Tile tile = board.getTileId(tileId);
      if(tileId % 2 == 0){
        tile.setStyle("-fx-background-color: #004DFF;");
      } else {
        tile.setStyle("-fx-background-color: #FF00D4;");
      }

      int bottomRow = (tileId - 1) / columns;
      int col = (tileId - 1) % columns;

      if (bottomRow % 2 == 1) {
        col = columns - 1 - col;
      }

      int gridRow = rows - 1 - bottomRow;

      grid.add(tile, col, gridRow);
    }

    grid.setStyle("-fx-border-width: 2; -fx-border-color: black;");

    return grid;
  }


  public static Board createBoardFromJSON(String filePath) throws IOException {
    BoardFileHandler boardFileHandler = new GsonBoardFileHandler();
    return boardFileHandler.readBoard(filePath);
  }
}
