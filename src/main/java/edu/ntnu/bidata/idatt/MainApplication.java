package edu.ntnu.bidata.idatt;

import edu.ntnu.bidata.idatt.view.SceneManager;
import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    new SceneManager(primaryStage);
    ;
    SceneManager.showLandingScene();
    primaryStage.setTitle("Board Games");
    primaryStage.show();
  }
}