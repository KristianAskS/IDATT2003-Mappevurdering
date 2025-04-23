package edu.ntnu.bidata.idatt.view;

import edu.ntnu.bidata.idatt.view.components.BackgroundImageView;
import edu.ntnu.bidata.idatt.view.scenes.BoardGameScene;
import edu.ntnu.bidata.idatt.view.scenes.BoardGameSelectionScene;
import edu.ntnu.bidata.idatt.view.scenes.LandingScene;
import edu.ntnu.bidata.idatt.view.scenes.PlayerSelectionScene;
import edu.ntnu.bidata.idatt.view.scenes.PodiumGameScene;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SceneManager {

  public final static int SCENE_WIDTH = 1200;
  public final static int SCENE_HEIGHT = 800;
  private static final Logger logger = Logger.getLogger(SceneManager.class.getName());
  private static final String BUTTONS_CSS_PATH = "/edu/ntnu/bidata/idatt/styles/ButtonsStyles.css";
  private static Stage primaryStage;

  public SceneManager(Stage primaryStage) {
    SceneManager.primaryStage = primaryStage;
  }

  public static void showLandingScene() {
    Scene landingScene = loadBtnCss(new LandingScene().getScene());
    primaryStage.setScene(landingScene);
    logger.log(Level.INFO, "Switch scene to landing scene");
  }

  public static void showBoardGameSelectionScene() {
    Scene boardGameSelectionScene = loadBtnCss(new BoardGameSelectionScene().getScene());
    primaryStage.setScene(boardGameSelectionScene);
    logger.log(Level.INFO, "Switch scene to boardGameSelectionScene");
  }

  public static void showPlayerSelectionScene() throws IOException {
    Scene playerSelectionScene = loadBtnCss(new PlayerSelectionScene().getScene());
    primaryStage.setScene(playerSelectionScene);
    logger.log(Level.INFO, "Switch scene to showPlayerSelectionScene");
  }

  public static void showBoardGameScene() throws IOException {
    Scene boardGameScene = loadBtnCss(new BoardGameScene().getScene());
    primaryStage.setScene(boardGameScene);
    logger.log(Level.INFO, "Switch scene to showBoardGameScene");
  }

  public static void showPodiumGameScene() {
    Scene podiumGameScene = loadBtnCss(new PodiumGameScene().getScene());
    primaryStage.setScene(podiumGameScene);
    logger.log(Level.INFO, "Switch scene to showPodiumGameScene");
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
