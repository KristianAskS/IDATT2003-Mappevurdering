package edu.ntnu.bidata.idatt.view.scenes;

import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_WIDTH;

import edu.ntnu.bidata.idatt.controller.patterns.observer.ConsoleBoardGameObserver;
import edu.ntnu.bidata.idatt.view.SceneManager;
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

public class LandingScene {
  private static final Logger logger = Logger.getLogger(ConsoleBoardGameObserver.class.getName());
  private final Scene scene;

  public LandingScene() {
    BorderPane rootPane = SceneManager.getRootPane();
    scene = new Scene(rootPane, SCENE_WIDTH, SCENE_HEIGHT, Color.PINK);

    Button choseGameBtn = Buttons.getPrimaryBtn("Chose game!");
    Button exitBtn = Buttons.getExitBtn("Exit");
    rootPane.setCenter(choseGameBtn);

    HBox exitBox = new HBox(exitBtn);
    exitBox.setAlignment(Pos.CENTER_RIGHT);
    exitBox.setPadding(new Insets(10));
    rootPane.setBottom(exitBox);

    choseGameBtn.setOnAction(e -> SceneManager.showBoardGameSelectionScene());
    exitBtn.setOnAction(e -> System.exit(0));

    logger.log(Level.INFO, "LandingScene created");
  }

  public Scene getScene() {
    return scene;
  }
}
