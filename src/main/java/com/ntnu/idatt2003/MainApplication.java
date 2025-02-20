package com.ntnu.idatt2003;

import com.ntnu.idatt2003.model.Game;
import com.ntnu.idatt2003.view.GameView;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {
  @Override
  public void start(Stage stage) {
    Game game = new Game(2);
    new GameView(game, stage);
  }
  
  public static void main(String[] args) {
    launch();
  }
}