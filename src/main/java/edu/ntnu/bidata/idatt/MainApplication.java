package edu.ntnu.bidata.idatt;

import java.io.IOException;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * <p>Main application entry point that launches the board game interface.</p>
 *
 * <p>This class extends {@link Application} and help set up the {@link BoardGameInterface}.</p>
 *
 * @author Tri Tac Le
 * @version 2.0
 * @since 2.0
 */
public class MainApplication extends Application {

  /**
   * <p>Application entry method which launches the JavaFX runtime.</p>
   *
   * @param args command-line arguments
   */
  public static void main(String[] args) {
    launch();
  }

  /**
   * <p>Called by JavaFX to start the application.</p>
   * <p>Initializes and displays the {@link BoardGameInterface} on the primary stage.</p>
   *
   * @param primaryStage the primary stage provided by the framework
   * @throws IOException          if an input or output error happens during initialization
   * @throws InterruptedException if initialization is interrupted
   */
  @Override
  public void start(Stage primaryStage) throws IOException, InterruptedException {
    BoardGameInterface boardGameInterface = new BoardGameInterface(primaryStage);
    boardGameInterface.init();
    boardGameInterface.start();
  }
}
