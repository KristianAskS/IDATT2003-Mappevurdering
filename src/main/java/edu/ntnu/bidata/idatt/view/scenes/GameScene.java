package edu.ntnu.bidata.idatt.view.scenes;

import edu.ntnu.bidata.idatt.logic.ConsoleBoardGameObserver;
import edu.ntnu.bidata.idatt.view.BoardGameGUI;
import edu.ntnu.bidata.idatt.view.SceneManager;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
public class GameScene {
  private static final Logger logger = Logger.getLogger(ConsoleBoardGameObserver.class.getName());
  private final Scene scene;
  public GameScene() {
    BoardGameGUI boardGameGUI = new BoardGameGUI();
    scene = boardGameGUI.getScene();
  }

  public Scene getScene(){
    return scene;
  }
}
