package edu.ntnu.bidata.idatt.view.scenes;

import edu.ntnu.bidata.idatt.controller.SceneManager;
import edu.ntnu.bidata.idatt.view.components.Buttons;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

/**
 * Represents the landing scene.
 * Displays the main menu.
 * Extends {@link BaseScene} for common scene setup.
 */
public class LandingScene extends BaseScene {
  private static final Logger logger = Logger.getLogger(LandingScene.class.getName());

  /**
   * Initializes the UI layout to display the landing scene.
   */
  @Override
  protected void initialize() {
    BorderPane rootPane = this.root;
    scene.setFill(Color.PINK);

    Button chooseGameBtn = Buttons.getPrimaryBtn("Choose game!");
    Button exitBtn = Buttons.getExitBtn("Exit");

    rootPane.setCenter(chooseGameBtn);
    HBox exitBox = new HBox(20, exitBtn);
    exitBox.setAlignment(Pos.CENTER_RIGHT);
    exitBox.setPadding(new Insets(10, 20, 10, 20));
    rootPane.setBottom(exitBox);

    chooseGameBtn.setOnAction(e -> SceneManager.showGameSelectionScene());
    exitBtn.setOnAction(e -> System.exit(0));

    logger.log(Level.INFO, "LandingScene created");
  }
}
