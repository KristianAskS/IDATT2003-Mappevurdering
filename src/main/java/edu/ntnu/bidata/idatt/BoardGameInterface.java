package edu.ntnu.bidata.idatt;

import edu.ntnu.bidata.idatt.controller.SceneManager;
import javafx.stage.Stage;

/**
 * <p>Entry point for the board game application, managing the primary stage
 * and initializing the scene manager.</p>
 *
 * @author Tri Tac Le
 */
public class BoardGameInterface {

  private final Stage primaryStage;

  /**
   * <p>Constructs the interface with the primary stage.</p>
   *
   * @param primaryStage the stage provided by the application launcher
   */
  public BoardGameInterface(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  /**
   * <p>Initializes the scene manager for this application and displays the landing scene.</p>
   */
  public void init() {
    new SceneManager(primaryStage);
    SceneManager.showLandingScene();
  }

  /**
   * <p>Shows the primary stage, making the application window visible to the user.</p>
   */
  public void start() {
    primaryStage.show();
  }
}
