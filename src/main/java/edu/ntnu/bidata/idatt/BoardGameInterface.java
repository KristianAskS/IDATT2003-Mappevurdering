package edu.ntnu.bidata.idatt;

import edu.ntnu.bidata.idatt.view.SceneManager;
import java.io.IOException;
import javafx.stage.Stage;

public class BoardGameInterface {
  private Stage primaryStage;

  public BoardGameInterface(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  public void init() throws IOException, InterruptedException {
    new SceneManager(primaryStage);
    SceneManager.showLandingScene();
  }

  public void start() {
    primaryStage.show();
  }
}
