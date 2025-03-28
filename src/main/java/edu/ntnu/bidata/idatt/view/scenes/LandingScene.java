package edu.ntnu.bidata.idatt.view.scenes;

import edu.ntnu.bidata.idatt.patterns.observer.ConsoleBoardGameObserver;
import edu.ntnu.bidata.idatt.view.SceneManager;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class LandingScene {
  private static final Logger logger = Logger.getLogger(ConsoleBoardGameObserver.class.getName());
  private final Scene scene;

  public LandingScene() {
    BorderPane rootPane = new BorderPane();
    scene = new Scene(rootPane, 800, 600);
    Button playBtn = new Button("Press to play!");
    Button exitBtn = new Button("Exit");
    rootPane.setBottom(exitBtn);
    rootPane.setCenter(playBtn);
    playBtn.setOnAction(e -> {
      SceneManager.showBoardGameSelectionScene();
    });
    exitBtn.setOnAction(e -> {
      System.exit(0);
    });
  }

  public Scene getScene() {
    return scene;
  }
}
