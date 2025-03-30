package edu.ntnu.bidata.idatt.view.scenes;

import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_WIDTH;

import edu.ntnu.bidata.idatt.controller.patterns.factory.BoardGameFactory;
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
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class BoardGameSelectionScene {
  private static final Logger logger = Logger.getLogger(ConsoleBoardGameObserver.class.getName());
  private final Scene scene;

  public BoardGameSelectionScene() {
    BorderPane rootPane = SceneManager.getRootPane();
    rootPane.setStyle("-fx-font-family: 'monospace'; -fx-background-color: #1A237E;");

    scene = new Scene(rootPane, SCENE_WIDTH, SCENE_HEIGHT, Color.PINK);
    rootPane.setBackground(new Background(BackgroundImageView.getBackgroundImage()));

    VBox leftWrapper = new VBox();
    leftWrapper.setAlignment(Pos.CENTER);
    leftWrapper.setPadding(new Insets(20));

    VBox selectionContainer = createSelectionContainer();

    selectionContainer.setMaxWidth(300);

    leftWrapper.getChildren().add(selectionContainer);
    rootPane.setLeft(leftWrapper);

    rootPane.setCenter(createSelectPlayerButton());

    rootPane.setBottom(createBottomContainer());

    logger.log(Level.INFO, "BoardGameSelectionScene created");
  }

  private VBox createSelectionContainer() {
    VBox container = new VBox(15);
    container.setAlignment(Pos.CENTER);
    container.setPadding(new Insets(20));
    container.setMaxWidth(400);
    container.setStyle(
        "-fx-font-family: 'monospace';" +
            "-fx-border-color: white;" +
            "-fx-border-width: 2;" +
            "-fx-background-color: rgba(0, 0, 0, 0.5);" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;"
    );

    Label title = new Label("Select a Board Game");
    title.setStyle(
        "-fx-font-size: 24px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.75), 4, 0, 2, 2);"
    );

    Button smallBoardBtn = createSmallBoardButton();
    Button classicBoardBtn = createClassicBoardButton();
    Button noLaddersSnakesBtn = createBoardNoLaddersAndSnakesButton();

    container.getChildren().addAll(title, smallBoardBtn, classicBoardBtn, noLaddersSnakesBtn);
    return container;
  }


  private HBox createBottomContainer() {
    HBox bottomBox = new HBox();
    bottomBox.setAlignment(Pos.CENTER_LEFT);
    bottomBox.setPadding(new Insets(10));
    bottomBox.getChildren().add(createToLandingSceneButton());
    return bottomBox;
  }

  private Button createSelectPlayerButton() {
    Button selectPlayerBtn = Buttons.getMainBtn("Select players");
    selectPlayerBtn.setOnAction(e -> SceneManager.showPlayerSelectionScene());
    return selectPlayerBtn;
  }

  private Button createSmallBoardButton() {
    Button btn = Buttons.getSecondaryBtn("Small Board");
    btn.setOnAction(e -> {
      BoardGameFactory.createSmallBoard();
      SceneManager.showPlayerSelectionScene();
    });
    return btn;
  }

  private Button createClassicBoardButton() {
    Button btn = Buttons.getSecondaryBtn("Classic Board");
    btn.setOnAction(e -> {
      BoardGameFactory.createClassicBoard();
      SceneManager.showPlayerSelectionScene();
    });
    return btn;
  }

  private Button createBoardNoLaddersAndSnakesButton() {
    Button btn = Buttons.getSecondaryBtn("No Ladders and Snakes");
    btn.setOnAction(e -> {
      BoardGameFactory.createBoardNoLaddersAndSnakes();
      SceneManager.showPlayerSelectionScene();
    });
    return btn;
  }

  private Button createToLandingSceneButton() {
    Button btn = Buttons.getBackBtn("Back");
    btn.setOnAction(e -> SceneManager.showLandingScene());
    return btn;
  }

  public Scene getScene() {
    return scene;
  }
}
