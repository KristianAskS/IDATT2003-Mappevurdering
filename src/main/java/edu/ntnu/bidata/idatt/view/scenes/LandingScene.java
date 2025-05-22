package edu.ntnu.bidata.idatt.view.scenes;

import static edu.ntnu.bidata.idatt.controller.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.controller.SceneManager.SCENE_WIDTH;

import edu.ntnu.bidata.idatt.controller.SceneManager;
import edu.ntnu.bidata.idatt.view.components.Buttons;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * <p>Represents the main landing scene of the application.</p>
 * <p>This scene displays a button to choose a game and an exit button
 * within a BorderPane.</p>
 *
 * @author Tri Tac Le
 */
public class LandingScene {
  private static final Logger logger = Logger.getLogger(
      LandingScene.class.getName());

  private final Scene scene;

  /**
   * <p>Constructs and initializes the landing scene, including layout,
   * buttons, and their event handlers.</p>
   */
  public LandingScene() {
    BorderPane rootPane = SceneManager.getRootPane();
    scene = new Scene(rootPane, SCENE_WIDTH, SCENE_HEIGHT, Color.PINK);

    Button chooseGameBtn = Buttons.getPrimaryBtn("Choose game!");
    Button exitBtn = Buttons.getExitBtn("Exit");
    rootPane.setCenter(chooseGameBtn);

    HBox exitBox = new HBox(20);
    exitBox.setAlignment(Pos.CENTER_RIGHT);
    exitBox.setPadding(new Insets(10, 20, 10, 20));
    exitBox.getChildren().add(exitBtn);
    rootPane.setBottom(exitBox);
    BorderPane.setMargin(exitBox, new Insets(10));

    chooseGameBtn.setOnAction(event -> SceneManager.showGameSelectionScene());
    exitBtn.setOnAction(event -> System.exit(0));

    logger.log(Level.INFO, "LandingScene created");
  }

  /**
   * Returns the scene for this landing view.
   *
   * @return the scene object
   */
  public Scene getScene() {
    return scene;
  }
}
