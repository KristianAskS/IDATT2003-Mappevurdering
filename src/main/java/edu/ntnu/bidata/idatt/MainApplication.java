package edu.ntnu.bidata.idatt;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage primaryStage) throws IOException {
    BoardGameInterface boardGameInterface = new BoardGameInterface(primaryStage);
    boardGameInterface.init();
    boardGameInterface.start();
  }
}