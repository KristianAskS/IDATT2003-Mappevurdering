package edu.ntnu.bidata.idatt.view.scenes;

import static edu.ntnu.bidata.idatt.controller.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.controller.SceneManager.SCENE_WIDTH;

import edu.ntnu.bidata.idatt.controller.SceneManager;
import java.io.IOException;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public abstract class BaseScene {

  protected final BorderPane root;
  protected final Scene scene;

  protected BaseScene() {
    this(SceneManager.getRootPane());
  }

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

  protected abstract void initialize() throws IOException;

  public Scene getScene() {
    return scene;
  }
}
