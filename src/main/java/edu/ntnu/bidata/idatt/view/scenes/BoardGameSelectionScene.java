package edu.ntnu.bidata.idatt.view.scenes;

import edu.ntnu.bidata.idatt.patterns.observer.ConsoleBoardGameObserver;
import edu.ntnu.bidata.idatt.view.SceneManager;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class BoardGameSelectionScene {
  private static final Logger logger = Logger.getLogger(ConsoleBoardGameObserver.class.getName());
  private final Scene scene;

  public BoardGameSelectionScene() {
    BorderPane rootPane = new BorderPane();
    scene = new Scene(rootPane, 800, 600);
    Button selectPlayerBtn = new Button("Select players");
    Button toLandingSceneBtn = new Button("Back to landing scene");
    rootPane.setCenter(selectPlayerBtn);
    rootPane.setBottom(toLandingSceneBtn);
    selectPlayerBtn.setOnAction(e -> {
      SceneManager.showPlayerSelectionScene();
    });
    toLandingSceneBtn.setOnAction(e -> {
      SceneManager.showLandingScene();
    });
  }

  public Scene getScene() {
    return scene;
  }
}
