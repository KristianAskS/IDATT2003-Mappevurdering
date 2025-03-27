package edu.ntnu.bidata.idatt.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class SceneManager {
  private Stage primaryStage;
  private Scene landingScene;
  private Scene boardGameSelectionScene;
  private Scene playerSelectionScene;
  private Scene gameScene;

  public SceneManager(Stage primaryStage){
    this.primaryStage = primaryStage;
    initializeScenes();
  }
  public void initializeScenes(){
    BorderPane landingRoot = new BorderPane();
    Button playBtn = new Button("Press to play!");
    landingRoot.setCenter(playBtn);
    landingScene = new Scene(landingRoot, 800, 600);

    BorderPane gameSelectionRoot = new BorderPane();
    Button toPlayerSelectionBtn = new Button("Select Board Game");
    gameSelectionRoot.setCenter(toPlayerSelectionBtn);
    boardGameSelectionScene = new Scene(gameSelectionRoot, 800, 600);

    BorderPane playerSelectionRoot = new BorderPane();
    Button toGameBtn = new Button("Select Players and Tokens");
    playerSelectionRoot.setCenter(toGameBtn);
    playerSelectionScene = new Scene(playerSelectionRoot, 800, 600);

    BoardGameGUI boardGameGUI = new BoardGameGUI();
    gameScene = boardGameGUI.getScene();

    playBtn.setOnAction(e -> primaryStage.setScene(boardGameSelectionScene));
    toPlayerSelectionBtn.setOnAction(e -> primaryStage.setScene(playerSelectionScene));
    toGameBtn.setOnAction(e -> primaryStage.setScene(gameScene));
  }

  public void showLandingScene(){
    primaryStage.setScene(landingScene);
  }


}
