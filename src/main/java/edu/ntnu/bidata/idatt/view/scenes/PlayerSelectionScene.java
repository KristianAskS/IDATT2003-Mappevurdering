package edu.ntnu.bidata.idatt.view.scenes;

import edu.ntnu.bidata.idatt.logic.ConsoleBoardGameObserver;
import edu.ntnu.bidata.idatt.view.SceneManager;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class PlayerSelectionScene {
  private static final Logger logger = Logger.getLogger(ConsoleBoardGameObserver.class.getName());
  private final Scene scene;

  public PlayerSelectionScene() {
    BorderPane rootPane = new BorderPane();
    scene = new Scene(rootPane, 800, 600);
    Button toGameSceneBtn = new Button("To game scene (play)");
    Button toLandingSceneBtn = new Button("Back to landing scene");
    Button toBoardGameSelectionSceneBtn = new Button("Back");
    rootPane.setCenter(toGameSceneBtn);
    rootPane.setBottom(toLandingSceneBtn);
    rootPane.setTop(toBoardGameSelectionSceneBtn);

    toGameSceneBtn.setOnAction(e -> {
      SceneManager.showGameScene();
    });
    toLandingSceneBtn.setOnAction(e -> {
      SceneManager.showLandingScene();
    });
    toBoardGameSelectionSceneBtn.setOnAction(e -> {
      SceneManager.showBoardGameSelectionScene();
    });
  }

  public Scene getScene() {
    return scene;
  }
}
