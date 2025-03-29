package edu.ntnu.bidata.idatt.view.scenes;

import edu.ntnu.bidata.idatt.patterns.observer.ConsoleBoardGameObserver;
import java.io.IOException;
import java.util.logging.Logger;
import javafx.scene.Scene;

/**
 *
 */
public class GameScene {
  private static final Logger logger = Logger.getLogger(ConsoleBoardGameObserver.class.getName());
  private final Scene scene;

  public GameScene() throws IOException, InterruptedException {
    BoardGameGUI boardGameGUI = new BoardGameGUI();
    scene = boardGameGUI.getScene();
  }

  public Scene getScene() {
    return scene;
  }
}
