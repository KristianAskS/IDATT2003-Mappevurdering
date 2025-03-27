package edu.ntnu.bidata.idatt;

import edu.ntnu.bidata.idatt.view.BoardGameGUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MainApplication extends Application {

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage primaryStage) {
    BoardGameGUI boardGameGUI = new BoardGameGUI();
    BorderPane root = boardGameGUI.getRootPane();
    Scene scene = new Scene(root, 800, 600);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Board Game");
    primaryStage.show();
  }
}