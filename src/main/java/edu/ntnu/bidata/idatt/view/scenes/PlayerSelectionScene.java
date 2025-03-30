package edu.ntnu.bidata.idatt.view.scenes;

import static edu.ntnu.bidata.idatt.model.service.PlayerService.PLAYER_FILE_PATH;
import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_WIDTH;

import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.TokenType;
import edu.ntnu.bidata.idatt.model.service.PlayerService;
import edu.ntnu.bidata.idatt.utils.io.CsvPlayerFileHandler;
import edu.ntnu.bidata.idatt.view.SceneManager;
import edu.ntnu.bidata.idatt.view.components.Buttons;
import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class PlayerSelectionScene {
  private static final Logger logger = Logger.getLogger(PlayerSelectionScene.class.getName());
  private final Scene scene;
  private final PlayerService playerService = new PlayerService();

  // Midlertidig - Observable list for displaying player names in the ListView
  private final ObservableList<String> players = FXCollections.observableArrayList(
      "Player 1", "Player 2", "Player 3", "Player 4","Player 5"
  );

  public PlayerSelectionScene() {
    BorderPane rootPane = SceneManager.getRootPane();
    scene = new Scene(rootPane, SCENE_WIDTH, SCENE_HEIGHT, Color.LIGHTBLUE);

    ListView<String> playerListView = new ListView<>(players);
    playerListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    playerListView.setPrefHeight(200);

    Button addPlayerBtn = new Button("Add Player");
    Button startGameBtn = Buttons.getPrimaryBtn("Start Game!");
    Button mainPageBtn = Buttons.getExitBtn("To Main Page");
    Button backBtn = Buttons.getBackBtn("Back");

    HBox bottomPane = new HBox(10, backBtn, mainPageBtn);
    bottomPane.setPadding(new Insets(10));

    VBox centerBox = new VBox(10, playerListView, addPlayerBtn, startGameBtn);
    centerBox.setPadding(new Insets(10));

    rootPane.setCenter(centerBox);
    rootPane.setBottom(bottomPane);

    addPlayerBtn.setOnAction(e -> {
      TextInputDialog dialog = new TextInputDialog();
      dialog.setTitle("Add New Player");
      dialog.setHeaderText("Enter new player's name:");
      dialog.setContentText("Name:");

      Optional<String> resultName = dialog.showAndWait();
      if (resultName.isPresent() && !resultName.get().trim().isEmpty()) {
        String playerName = resultName.get().trim();

        dialog.setHeaderText("Enter player's token color (midlertidig):");
        dialog.setContentText("Color:");
        Optional<String> resultToken = dialog.showAndWait();
        if (resultToken.isPresent() && !resultToken.get().trim().isEmpty()) {
          TokenType tokenType = TokenType.valueOf(resultToken.get().trim().toUpperCase());
          TokenView tokenView = new TokenView(tokenType);

          Player newPlayer = new Player(playerName, tokenView);
          players.add(playerName);
          playerService.addPlayer(newPlayer);
          logger.log(Level.INFO, "New player created: " + newPlayer.getName());
        }
      }
    });

    startGameBtn.setOnAction(e -> {
      var selectedPlayers = playerListView.getSelectionModel().getSelectedItems();
      if (selectedPlayers.isEmpty()) {
        logger.log(Level.WARNING, "No players selected!");
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("No players selected");
        dialog.setHeaderText(null);
        dialog.setContentText("Please select at least one player.");
        dialog.showAndWait();
      } else {
        try {
          CsvPlayerFileHandler csvHandler = new CsvPlayerFileHandler();
          csvHandler.writeToFile(playerService.getPlayers(), PLAYER_FILE_PATH);
          logger.log(Level.INFO, "Player data saved to CSV file.");
        } catch (IOException ex) {
          logger.log(Level.SEVERE, "Error saving players: " + ex.getMessage());
        }
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
