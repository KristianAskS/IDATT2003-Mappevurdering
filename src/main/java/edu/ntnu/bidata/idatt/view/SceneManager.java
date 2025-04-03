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
  private static Stage primaryStage;
  private static Scene landingScene;
  private static Scene boardGameSelectionScene;
  private static Scene playerSelectionScene;
  private static Scene boardGameScene;
  private static final String BUTTONS_CSS_PATH ="/edu/ntnu/bidata/idatt/styles/ButtonsStyles.css";

  public SceneManager(Stage primaryStage) throws IOException {
    SceneManager.primaryStage = primaryStage;
    landingScene = loadBtnCss(new LandingScene().getScene());
    boardGameSelectionScene = loadBtnCss(new BoardGameSelectionScene().getScene());
    playerSelectionScene = loadBtnCss(new PlayerSelectionScene().getScene());
    boardGameScene = loadBtnCss(new BoardGameScene().getScene());
  }

  public static void showLandingScene() {
    primaryStage.setScene(landingScene);
  }

  public static void showBoardGameSelectionScene() {
    primaryStage.setScene(boardGameSelectionScene);
  }

  public static void showPlayerSelectionScene() {
    primaryStage.setScene(playerSelectionScene);
  }

  public static void showBoardGameScene() {
    primaryStage.setScene(boardGameScene);
  }

  public static BorderPane getRootPane() {
    BorderPane rootPane = new BorderPane();
    rootPane.setBackground(new Background(BackgroundImageView.getBackgroundImage()));
    return rootPane;
  }

  private Scene loadBtnCss(Scene scene){
    scene.getStylesheets().add(String.valueOf(getClass().getResource(BUTTONS_CSS_PATH)));
    return scene;
  }
}
