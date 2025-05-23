package edu.ntnu.bidata.idatt.view.scenes;

import static edu.ntnu.bidata.idatt.controller.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.controller.SceneManager.SCENE_WIDTH;

import edu.ntnu.bidata.idatt.controller.SceneManager;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

/**
 * Base scene class for UI components.
 */
public abstract class BaseScene {

  protected final BorderPane root;
  protected final Scene scene;

  /**
   * Constructs a new BaseScene.
   */
  protected BaseScene() {
    this(SceneManager.getRootPane());
  }

  /**
   * Constructs a new BaseScene.
   *
   * @param root the root pane
   */
  protected BaseScene(BorderPane root) {
    this.root = root;
    this.scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
    Platform.runLater(() -> {
      try {
        initialize();
      } catch (IOException e) {
        throw new RuntimeException("Failed during initialize()", e);
      }
    });
  }

  /**
   * Initializes the scene.
   *
   * @throws IOException if an I/O error occurs
   */
  protected abstract void initialize() throws IOException;

  /**
   * Returns the scene.
   *
   * @return the scene
   */
  public Scene getScene() {
    return scene;
  }
}
