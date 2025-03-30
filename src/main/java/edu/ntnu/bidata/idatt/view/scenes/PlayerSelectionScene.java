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
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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

  // Observable list for displaying player names from CSV or added manually.
  private final ObservableList<String> players = FXCollections.observableArrayList();
  private int maxPlayers = 0;

  public PlayerSelectionScene() {
    BorderPane rootPane = SceneManager.getRootPane();
    scene = new Scene(rootPane, SCENE_WIDTH, SCENE_HEIGHT, Color.LIGHTBLUE);

    try {
      List<Player> csvPlayers = playerService.readPlayersFromFile(PLAYER_FILE_PATH);
      for (Player p : csvPlayers) {
        if (!players.contains(p.getName())) {
          players.add(p.getName());
        }
      }
      logger.log(Level.INFO, "Loaded players from CSV: " + players);
    } catch (IOException ex) {
      logger.log(Level.SEVERE, "Error reading players from CSV: " + ex.getMessage());
    }

    promptNumberOfPlayers();

    ListView<String> playerListView = new ListView<>(players);
    playerListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    playerListView.setPrefHeight(200);

    Button addPlayerBtn = new Button("Add Player Manually");
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
      TextInputDialog nameDialog = new TextInputDialog();
      nameDialog.setTitle("Add New Player");
      nameDialog.setHeaderText("Enter new player's name:");
      nameDialog.setContentText("Name:");

      Optional<String> resultName = nameDialog.showAndWait();
      if (resultName.isPresent() && !resultName.get().trim().isEmpty()) {
        String playerName = resultName.get().trim();

        TextInputDialog colorDialog = new TextInputDialog();
        colorDialog.setTitle("Player Token Color");
        colorDialog.setHeaderText("Enter player's token color (e.g., RED, BLUE):");
        colorDialog.setContentText("Color:");

        Optional<String> resultToken = colorDialog.showAndWait();
        if (resultToken.isPresent() && !resultToken.get().trim().isEmpty()) {
          try {
            TokenType tokenType = TokenType.valueOf(resultToken.get().trim().toUpperCase());
            TokenView tokenView = new TokenView(tokenType);
            Player newPlayer = new Player(playerName, tokenView);
            if (!players.contains(playerName)) {
              players.add(playerName);
              playerService.getPlayers().add(newPlayer);
              playerListView.getSelectionModel().select(playerName);
              logger.log(Level.INFO, "New player created: " + newPlayer.getName());
            } else {
              showAlert("Duplicate Player", "A player with that name already exists.");
            }
          } catch (IllegalArgumentException ex) {
            showAlert("Invalid Color",
                "Please enter a valid token color (e.g., RED, BLUE, GREEN, YELLOW, PINK).");
          }
        }
      }
    });

    startGameBtn.setOnAction(e -> {
      var selectedPlayers = playerListView.getSelectionModel().getSelectedItems();
      if (selectedPlayers.size() != maxPlayers) {
        showAlert("Incorrect Number of Players",
            "You must select exactly " + maxPlayers + " player(s).");
        logger.log(Level.WARNING,
            "Expected " + maxPlayers + " players, but got " + selectedPlayers.size());
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

    logger.log(Level.INFO, "PlayerSelectionScene created");
  }

  private void promptNumberOfPlayers() {
    TextInputDialog numPlayersDialog = new TextInputDialog("2");
    numPlayersDialog.setTitle("Select Number of Players");
    numPlayersDialog.setHeaderText("How many players will be playing? (max 5)");
    numPlayersDialog.setContentText("Enter a number (1-5):");

    Optional<String> numPlayersResult = numPlayersDialog.showAndWait();
    if (numPlayersResult.isPresent()) {
      try {
        int num = Integer.parseInt(numPlayersResult.get().trim());
        if (num < 1 || num > 5) {
          showAlert("Invalid Number",
              "Please enter a number between 1 and 5. Defaulting to 2 players.");
          maxPlayers = 2;
        } else {
          maxPlayers = num;
        }
      } catch (NumberFormatException ex) {
        showAlert("Invalid Input", "Please enter a valid number. Defaulting to 2 players.");
        maxPlayers = 2;
      }
    } else {
      // if closed the dialog, the default is 2 players
      maxPlayers = 2;
    }
    logger.log(Level.INFO, "Max players set to: " + maxPlayers);
  }

  /**
   * Utility method to show an alert dialog with the specified title and message.
   */
  private void showAlert(String title, String message) {
    Alert alert = new Alert(AlertType.WARNING);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
  }

  public Scene getScene() {
    return scene;
  }
}
