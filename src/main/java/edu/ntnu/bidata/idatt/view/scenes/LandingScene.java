package edu.ntnu.bidata.idatt.view.scenes;

import edu.ntnu.bidata.idatt.patterns.observer.ConsoleBoardGameObserver;
import edu.ntnu.bidata.idatt.view.SceneManager;
import edu.ntnu.bidata.idatt.view.components.Buttons;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class LandingScene {
  private static final Logger logger = Logger.getLogger(ConsoleBoardGameObserver.class.getName());
  private final Scene scene;

  public LandingScene() {
    BorderPane rootPane = new BorderPane();
    scene = new Scene(rootPane, 800, 600);

    Image bgImage = new Image(
        getClass().getResource("/edu/ntnu/bidata/idatt/images/backgroundImg.jpg").toExternalForm()
    );
    BackgroundImage backgroundImage = new BackgroundImage(
        bgImage,
        BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.CENTER,
        new BackgroundSize(1.0, 1.0, true, true, false, false)
    );
    rootPane.setBackground(new Background(backgroundImage));

    Button playBtn = Buttons.getPlayBtn("Press to play!");
    Button exitBtn = Buttons.getExitBtn("Exit");

    rootPane.setCenter(playBtn);

    HBox exitBox = new HBox(exitBtn);
    exitBox.setAlignment(Pos.CENTER_RIGHT);
    exitBox.setPadding(new Insets(10));
    rootPane.setBottom(exitBox);

    playBtn.setOnAction(e -> SceneManager.showBoardGameSelectionScene());
    exitBtn.setOnAction(e -> System.exit(0));
  }

  public Scene getScene() {
    return scene;
  }
}
