package edu.ntnu.bidata.idatt.view.scenes;

import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_WIDTH;

import edu.ntnu.bidata.idatt.view.SceneManager;
import edu.ntnu.bidata.idatt.view.components.Buttons;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class PlayerSelectionScene {
  private static final Logger logger = Logger.getLogger(PlayerSelectionScene.class.getName());
  private final Scene scene;
  private Alert alert;

  public PlayerSelectionScene() {
    BorderPane rootPane = SceneManager.getRootPane();
    scene = new Scene(rootPane, SCENE_WIDTH, SCENE_HEIGHT, Color.LIGHTBLUE);

    // Create an observable list of players (this could come from your data model)
    ObservableList<String> players = FXCollections.observableArrayList(
        "Player 1", "Player 2", "Player 3", "Player 4"
    );

    ListView<String> playerListView = new ListView<>(players);
    playerListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    playerListView.setPrefHeight(200);

    Button startGameBtn = Buttons.getPrimaryBtn("Start Game!");
    Button mainPageBtn = Buttons.getExitBtn("To Main Page");
    Button backBtn = Buttons.getBackBtn("Back");

    HBox bottomPane = new HBox(10, backBtn, mainPageBtn);
    bottomPane.setPadding(new Insets(10));

    VBox centerBox = new VBox(10, playerListView, startGameBtn);
    centerBox.setPadding(new Insets(10));

    rootPane.setCenter(centerBox);
    rootPane.setBottom(bottomPane);


    startGameBtn.setOnAction(e -> {
      ObservableList<String> selectedPlayers =
          playerListView.getSelectionModel().getSelectedItems();
      if (selectedPlayers.isEmpty()) {
        logger.log(Level.WARNING, "No players selected!");
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("No players selected");
        dialog.setHeaderText(null);
        dialog.setContentText("Please select at least one player.");
        dialog.showAndWait();
      } else {
        logger.log(Level.INFO, "Selected players: " + selectedPlayers);
        SceneManager.showBoardGameScene();
      }
    });

    mainPageBtn.setOnAction(e -> SceneManager.showLandingScene());
    backBtn.setOnAction(e -> SceneManager.showBoardGameSelectionScene());

    logger.log(Level.INFO, "ChoosePlayerScene created");
  }

  public Scene getScene() {
    return scene;
  }
}
