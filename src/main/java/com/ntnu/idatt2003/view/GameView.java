package com.ntnu.idatt2003.view;

import com.ntnu.idatt2003.model.Game;
import com.ntnu.idatt2003.model.Player;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameView {
  private final Game game;
  private final GridPane boardGrid = new GridPane();
  private final Label statusLabel = new Label("Spillet starter!");
  
  public GameView(Game game, Stage stage) {
    this.game = game;
    VBox root = new VBox(10);
    Button rollButton = new Button("Kast terning");
    rollButton.setOnAction(e -> handleRoll());
    
    root.getChildren().addAll(boardGrid, statusLabel, rollButton);
    stage.setScene(new Scene(root, 600, 600));
    stage.setTitle("Stigespill");
    stage.show();
  }
  
  private void handleRoll() {
    game.playTurn();
    updateStatus();
  }
  
  private void updateStatus() {
    StringBuilder status = new StringBuilder();
    for (Player player : game.getPlayers()) {
      status.append(player.getName()).append(": Rute ").append(player.getPosition()).append("\n");
    }
    statusLabel.setText(status.toString());
  }
}