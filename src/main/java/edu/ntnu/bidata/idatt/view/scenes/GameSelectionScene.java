package edu.ntnu.bidata.idatt.view.scenes;

import edu.ntnu.bidata.idatt.controller.SceneManager;
import edu.ntnu.bidata.idatt.view.components.BackgroundImageView;
import edu.ntnu.bidata.idatt.view.components.Buttons;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Represents the scene where users can select a game board.
 * It allows users to choose from predefined boards or import custom boards from JSON files.
 * Extends {@link BaseScene} for common scene setup.
 */
public class GameSelectionScene extends BaseScene {

  private static final Logger logger = Logger.getLogger(GameSelectionScene.class.getName());

  private static String selectedGame;

  /**
   * Returns the currently selected game board.
   *
   * @return Board selected by the user, or null if no board is selected.
   */
  public static String getSelectedGame() {
    return selectedGame;
  }

  /**
   * Initializes the UI layout and components for the game selection scene.
   * Sets up containers for game selection.
   */
  @Override
  protected void initialize() {
    scene.setFill(Color.PINK);

    BorderPane rootPane = this.root;
    rootPane.setPadding(Insets.EMPTY);
    rootPane.setStyle("-fx-font-family: 'monospace';");
    rootPane.setBackground(new Background(BackgroundImageView.getBackgroundImage()));

    VBox mainWrapper = new VBox(40);
    mainWrapper.setAlignment(Pos.CENTER);
    mainWrapper.setPadding(new Insets(40));

    Button ludoBtn = Buttons.getPrimaryBtn("Ludo");
    ludoBtn.setOnAction(event -> {
      selectedGame = "LUDO";
      logger.log(Level.INFO, "Ludo selected");
      SceneManager.showBoardSelectionScene();
    });

    Button snlBtn = Buttons.getPrimaryBtn("Snakes & Ladders");
    snlBtn.setOnAction(event -> {
      selectedGame = "SNAKES_AND_LADDERS";
      logger.log(Level.INFO, "Snakes & Ladders selected");
      SceneManager.showBoardSelectionScene();
    });

    mainWrapper.getChildren().addAll(ludoBtn, snlBtn);

    // Build the bottom back‐button bar
    HBox bottomContainer = createBottomContainer();

    // Attach to root
    rootPane.setCenter(mainWrapper);
    rootPane.setBottom(bottomContainer);
    BorderPane.setAlignment(bottomContainer, Pos.BOTTOM_LEFT);
    BorderPane.setMargin(bottomContainer, new Insets(10));

    logger.log(Level.INFO, "GameSelectionScene initialized");
  }

  /**
   * Creates a VBox container for board selection buttons.
   *
   * @return The styled {@link VBox} for board options.
   */
  private HBox createBottomContainer() {
    HBox bottomBox = new HBox(20);
    bottomBox.setPadding(new Insets(10, 20, 10, 20));
    bottomBox.setAlignment(Pos.BOTTOM_CENTER);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    Button backBtn = Buttons.getBackBtn("Back");
    backBtn.setOnAction(e -> SceneManager.showLandingScene());

    bottomBox.getChildren().addAll(backBtn, spacer);
    return bottomBox;
  }
}
