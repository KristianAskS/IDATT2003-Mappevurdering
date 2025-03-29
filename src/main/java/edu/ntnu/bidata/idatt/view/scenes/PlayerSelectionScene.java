package edu.ntnu.bidata.idatt.view.scenes;

import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_WIDTH;

import edu.ntnu.bidata.idatt.view.SceneManager;
import edu.ntnu.bidata.idatt.view.components.BackgroundImageView;
import edu.ntnu.bidata.idatt.view.components.Buttons;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class PlayerSelectionScene {
  private static final Logger logger = Logger.getLogger(PlayerSelectionScene.class.getName());
  private final Scene scene;

  public PlayerSelectionScene() {
    BorderPane rootPane = new BorderPane();
    scene = new Scene(rootPane, SCENE_WIDTH, SCENE_HEIGHT, Color.PINK);

    rootPane.setBackground(new Background(BackgroundImageView.getBackgroundImage()));

    Button toBoardGameScene = Buttons.getMainBtn("Play!");
    Button toLandingSceneBtn = Buttons.getExitBtn("To main page");
    Button toBoardGameSelectionSceneBtn = Buttons.getBackBtn("Back");

    BorderPane bottomPane = new BorderPane();
    bottomPane.setLeft(toBoardGameSelectionSceneBtn);
    bottomPane.setRight(toLandingSceneBtn);
    bottomPane.setPadding(new Insets(10));
    rootPane.setBottom(bottomPane);

    rootPane.setCenter(toBoardGameScene);

    toBoardGameScene.setOnAction(e -> {
      SceneManager.showBoardGameScene();
    });
    toLandingSceneBtn.setOnAction(e -> {
      SceneManager.showLandingScene();
    });
    toBoardGameSelectionSceneBtn.setOnAction(e -> {
      SceneManager.showBoardGameSelectionScene();
    });
    logger.log(Level.INFO, "PlayerSelectionScene created");
  }

  public Scene getScene() {
    return scene;
  }
}
