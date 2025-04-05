package edu.ntnu.bidata.idatt.view;

import edu.ntnu.bidata.idatt.view.components.BackgroundImageView;
import edu.ntnu.bidata.idatt.view.scenes.BoardGameScene;
import edu.ntnu.bidata.idatt.view.scenes.BoardGameSelectionScene;
import edu.ntnu.bidata.idatt.view.scenes.LandingScene;
import edu.ntnu.bidata.idatt.view.scenes.PlayerSelectionScene;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SceneManager {
  public final static int SCENE_WIDTH = 1000;
  public final static int SCENE_HEIGHT = 700;
  private static final String BUTTONS_CSS_PATH = "/edu/ntnu/bidata/idatt/styles/ButtonsStyles.css";
  private static Stage primaryStage;
  private static Scene landingScene;
  private static Scene boardGameSelectionScene;
  private static Scene playerSelectionScene;
  private static Scene boardGameScene;

  public SceneManager(Stage primaryStage) throws IOException {
    SceneManager.primaryStage = primaryStage;
  }

  public static void showLandingScene() {
    landingScene = loadBtnCss(new LandingScene().getScene());
    primaryStage.setScene(landingScene);
  }

  public static void showBoardGameSelectionScene() {
    boardGameSelectionScene = loadBtnCss(new BoardGameSelectionScene().getScene());
    primaryStage.setScene(boardGameSelectionScene);
  }

  public static void showPlayerSelectionScene() throws IOException {
    playerSelectionScene = loadBtnCss(new PlayerSelectionScene().getScene());
    primaryStage.setScene(playerSelectionScene);
  }

  public static void showBoardGameScene() throws IOException {
    boardGameScene = loadBtnCss(new BoardGameScene().getScene());
    primaryStage.setScene(boardGameScene);
  }

  public static BorderPane getRootPane() {
    BorderPane rootPane = new BorderPane();
    rootPane.setBackground(new Background(BackgroundImageView.getBackgroundImage()));
    return rootPane;
  }

  private static Scene loadBtnCss(Scene scene) {
    scene.getStylesheets().add(String.valueOf(SceneManager.class.getResource(BUTTONS_CSS_PATH)));
    return scene;
  }
}
