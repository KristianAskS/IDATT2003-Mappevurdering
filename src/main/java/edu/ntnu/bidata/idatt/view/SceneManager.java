package edu.ntnu.bidata.idatt.view;

import edu.ntnu.bidata.idatt.view.scenes.BoardGameSelectionScene;
import edu.ntnu.bidata.idatt.view.scenes.GameScene;
import edu.ntnu.bidata.idatt.view.scenes.LandingScene;
import edu.ntnu.bidata.idatt.view.scenes.PlayerSelectionScene;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
  private static Stage primaryStage;
  private static Scene landingScene;
  private static Scene boardGameSelectionScene;
  private static Scene playerSelectionScene;
  private static Scene gameScene;

  public SceneManager(Stage primaryStage) {
    SceneManager.primaryStage = primaryStage;
    landingScene = new LandingScene().getScene();
    boardGameSelectionScene = new BoardGameSelectionScene().getScene();
    playerSelectionScene = new PlayerSelectionScene().getScene();
    gameScene = new GameScene().getScene();
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

  public static void showGameScene() {
    primaryStage.setScene(gameScene);
  }
}
