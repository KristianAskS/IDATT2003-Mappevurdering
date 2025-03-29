package edu.ntnu.bidata.idatt;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main class for running the application
 *
 * @author Tri Le
 * @author Kristian Ask
 * @version 2.0
 * @since 2.0
 */
public class MainApplication extends Application {

  public static void main(String[] args) {
    launch();
  }

  @Override
  public void start(Stage primaryStage) throws IOException, InterruptedException {
    BoardGameInterface boardGameInterface = new BoardGameInterface(primaryStage);
    boardGameInterface.init();
    boardGameInterface.start();
  }
}