package edu.ntnu.bidata.idatt.view;

import edu.ntnu.bidata.idatt.MainApplication;
import edu.ntnu.bidata.idatt.entity.Player;
import edu.ntnu.bidata.idatt.logic.BoardGame;
import edu.ntnu.bidata.idatt.logic.BoardGameObserver;
import edu.ntnu.bidata.idatt.model.Tile;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class BoardGameGUI implements BoardGameObserver {
  private final BoardGame boardGame;
  private final BorderPane rootPane = new BorderPane();
  Label statusLabel;
  Scene scene = new Scene(rootPane, 800, 600);;

  public BoardGameGUI() {
    this.boardGame = new BoardGame();
    statusLabel = new Label("Welcome to the game!");
    rootPane.setTop(statusLabel);
    boardGame.addObserver(this);
    Button exitBtn = new Button("Exit");
    Button toLandingSceneBtn = new Button("Back to landing scene");
    toLandingSceneBtn.setOnAction(e->{
      SceneManager.showLandingScene();
    });
    exitBtn.setOnAction(e->{
      System.exit(0);
    });
  }

  public BorderPane getRootPane() {
    return rootPane;
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
