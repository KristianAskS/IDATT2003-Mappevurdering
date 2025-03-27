package edu.ntnu.bidata.idatt.view;

import edu.ntnu.bidata.idatt.entity.Player;
import edu.ntnu.bidata.idatt.logic.BoardGame;
import edu.ntnu.bidata.idatt.logic.BoardGameObserver;
import edu.ntnu.bidata.idatt.model.Tile;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class BoardGameGUI implements BoardGameObserver {
  private final BorderPane rootPane;
  private final Pane boardPane;
  private final Label statusLabel;
  private BoardGame boardGame;


  public BoardGameGUI() {
    this.rootPane = new BorderPane();
    this.boardPane = new BorderPane();
    this.statusLabel = new Label("Welcome to the board game!!!!!!!");

    boardGame.addObserver(this);

    Button moveButton = new Button("Move Player");
    moveButton.setOnAction(e -> {
      handleMovePlayer();
    });
    rootPane.setTop(moveButton);
    rootPane.setCenter(boardPane);
    rootPane.setBottom(statusLabel);
    drawBoard();
  }

  public BorderPane getRootPane() {
    return rootPane;
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
