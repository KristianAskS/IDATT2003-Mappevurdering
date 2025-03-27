package edu.ntnu.bidata.idatt.view;

import edu.ntnu.bidata.idatt.MainApplication;
import edu.ntnu.bidata.idatt.entity.Player;
import edu.ntnu.bidata.idatt.logic.BoardGame;
import edu.ntnu.bidata.idatt.logic.BoardGameObserver;
import edu.ntnu.bidata.idatt.logic.ConsoleBoardGameObserver;
import edu.ntnu.bidata.idatt.model.Tile;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class BoardGameGUI implements BoardGameObserver {
  private static final Logger logger = Logger.getLogger(BoardGameGUI.class.getName());
  private final BoardGame boardGame;
  private final Label statusLabel;
  private final Scene scene;

  public BoardGameGUI() {
    this.boardGame = new BoardGame();
    boardGame.addObserver(this);

    BorderPane rootPane = new BorderPane();
    scene = new Scene(rootPane, 800, 600);
    Button toLandingSceneBtn = new Button("Back to landing scene");
    Button toBoardGameSelectionSceneBtn = new Button("Back");
    rootPane.setBottom(toLandingSceneBtn);
    rootPane.setTop(toBoardGameSelectionSceneBtn);

    rootPane.setCenter(createBoard());
    statusLabel = new Label("Welcome to the game!");
    rootPane.setCenter(statusLabel);
    drawBoard();
  }
  private GridPane createBoard(){
    GridPane board = new GridPane();
    board.setPrefSize(800, 600);
    for (int i = 0; i < boardGame.getNumbOfTiles(); i++) {
      for (int j = 0; j < boardGame.getNumbOfTiles(); j++) {

        Tile tile = new Tile(j);
        board.add(new StackPane(tile), j, i);
      }
    }
    return board;
  }

  public Scene getScene() {
    return scene;
  }
  private void drawBoard() {

  }

  private void handleMovePlayer() {
    Player currentPlayer = boardGame.getCurrentPlayer();
    if (currentPlayer == null) {
      statusLabel.setText("No player found");
      return;
    }
    Tile oldTile = null;//get old tile
    //move after throwing dice
    Tile newTile = null;//get new tile
    //notify observers

    statusLabel.setText(currentPlayer.getName() + " moved to tile " + newTile.getTileId());
  }

  @Override
  public void onPlayerMoved(Player player, Tile oldTile, Tile newTile) {
    statusLabel.setText(
        player.getName() + " moved from " + oldTile.getTileId() + " to " + newTile.getTileId());
    //Update GUI: move a token from old tile to new tile on the boardPane
  }

  @Override
  public void onGameFinished(Player player) {
    statusLabel.setText(player.getName() + " won the game!");
  }
}
