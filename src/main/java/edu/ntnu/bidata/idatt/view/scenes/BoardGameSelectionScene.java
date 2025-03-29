package edu.ntnu.bidata.idatt.view.scenes;

import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_WIDTH;

import edu.ntnu.bidata.idatt.controller.patterns.observer.ConsoleBoardGameObserver;
import edu.ntnu.bidata.idatt.view.SceneManager;
import edu.ntnu.bidata.idatt.view.components.BackgroundImageView;
import edu.ntnu.bidata.idatt.view.components.Buttons;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class BoardGameSelectionScene {
  private static final Logger logger = Logger.getLogger(ConsoleBoardGameObserver.class.getName());
  private final Scene scene;

  public BoardGameSelectionScene() {
    BorderPane rootPane = new BorderPane();
    scene = new Scene(rootPane, SCENE_WIDTH, SCENE_HEIGHT, Color.PINK);

    rootPane.setBackground(new Background(BackgroundImageView.getBackgroundImage()));

    Button selectPlayerBtn = Buttons.getMainBtn("Select players");
    Button toLandingSceneBtn = Buttons.getBackBtn("Back");

    HBox toLandingSceneBox = new HBox(toLandingSceneBtn);
    toLandingSceneBox.setAlignment(Pos.CENTER_LEFT);
    toLandingSceneBox.setPadding(new Insets(10));

    rootPane.setCenter(selectPlayerBtn);
    rootPane.setBottom(toLandingSceneBox);

    selectPlayerBtn.setOnAction(e -> {
      SceneManager.showPlayerSelectionScene();
    });
    toLandingSceneBtn.setOnAction(e -> {
      SceneManager.showLandingScene();
    });
    logger.log(Level.INFO, "BoardGameSelectionScene created");
  }

  public Scene getScene() {
    return scene;
  }
}
