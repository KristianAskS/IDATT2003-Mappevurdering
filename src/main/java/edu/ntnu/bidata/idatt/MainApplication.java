package edu.ntnu.bidata.idatt;

import edu.ntnu.bidata.idatt.view.BoardGameGUI;
import edu.ntnu.bidata.idatt.view.SceneManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApplication extends Application {
  static BorderPane rootPane = new BorderPane();

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    SceneManager.showLandingScene();
    primaryStage.setTitle("Board Games");
    primaryStage.show();
  }
}