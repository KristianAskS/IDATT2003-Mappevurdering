package edu.ntnu.bidata.idatt.view;

import edu.ntnu.bidata.idatt.view.scenes.BoardGameScene;
import edu.ntnu.bidata.idatt.view.scenes.BoardGameSelectionScene;
import edu.ntnu.bidata.idatt.view.scenes.LandingScene;
import edu.ntnu.bidata.idatt.view.scenes.PlayerSelectionScene;
import java.io.IOException;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
  public final static int SCENE_WIDTH = 1000;
  public final static int SCENE_HEIGHT = 700;
  private static Stage primaryStage;
  private static Scene landingScene;
  private static Scene boardGameSelectionScene;
  private static Scene playerSelectionScene;
  private static Scene boardGameScene;

  public SceneManager(Stage primaryStage) throws IOException {
    SceneManager.primaryStage = primaryStage;
    landingScene = new LandingScene().getScene();
    boardGameSelectionScene = new BoardGameSelectionScene().getScene();
    playerSelectionScene = new PlayerSelectionScene().getScene();
    boardGameScene = new BoardGameScene().getScene();
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
}
