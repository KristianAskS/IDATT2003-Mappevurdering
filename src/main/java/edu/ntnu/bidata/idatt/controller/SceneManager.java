package edu.ntnu.bidata.idatt.controller;

import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.view.components.BackgroundImageView;
import edu.ntnu.bidata.idatt.view.components.GameUiAnimator;
import edu.ntnu.bidata.idatt.view.scenes.BoardGameScene;
import edu.ntnu.bidata.idatt.view.scenes.BoardSelectionScene;
import edu.ntnu.bidata.idatt.view.scenes.GameSelectionScene;
import edu.ntnu.bidata.idatt.view.scenes.LandingScene;
import edu.ntnu.bidata.idatt.view.scenes.PlayerSelectionScene;
import edu.ntnu.bidata.idatt.view.scenes.PodiumGameScene;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * <p>Class that handles switching between the scenes.</p>
 *
 * <p>Holds a reference to the primary {@link Stage}.</p>
 *
 * @author Tri Tac Le
 * @since 1.0
 */
public class SceneManager {
  /**
   * The fixed width (in pixels) for all scenes.
   */
  public static final int SCENE_WIDTH = 1200;
  /**
   * The fixed height (in pixels) for all scenes.
   */
  public static final int SCENE_HEIGHT = 800;
  private static final Logger logger = Logger.getLogger(SceneManager.class.getName());
  private static final String BUTTONS_CSS_PATH =
      "/edu/ntnu/bidata/idatt/styles/ButtonsStyles.css";
  private static Stage primaryStage;
  private GameUiAnimator animator;

  /**
   * <p>Initializes the SceneManager with the given primary stage.</p>
   *
   * @param primaryStage the JavaFX {@link Stage}
   */
  public SceneManager(Stage primaryStage) {
    SceneManager.primaryStage = primaryStage;
  }

  /**
   * Displays the landing scene.
   */
  public static void showLandingScene() {
    Scene scene = loadBtnCss(new LandingScene().getScene());
    primaryStage.setScene(scene);
    logger.log(Level.INFO, "Switched to LandingScene");
  }

  /**
   * Displays the game selection scene.
   */
  public static void showGameSelectionScene() {
    Scene scene = loadBtnCss(new GameSelectionScene().getScene());
    primaryStage.setScene(scene);
    logger.log(Level.INFO, "Switched to GameSelectionScene");
  }

  /**
   * Displays the board selection scene.
   */
  public static void showBoardSelectionScene() {
    Scene scene = loadBtnCss(new BoardSelectionScene().getScene());
    primaryStage.setScene(scene);
    logger.log(Level.INFO, "Switched to BoardSelectionScene");
  }

  /**
   * Displays the player selection scene.
   *
   * @throws IOException if loading required resources fails
   */
  public static void showPlayerSelectionScene() throws IOException {
    Scene scene = loadBtnCss(new PlayerSelectionScene().getScene());
    primaryStage.setScene(scene);
    logger.log(Level.INFO, "Switched to PlayerSelectionScene");
  }

  /**
   * Displays the main board game scene.
   *
   * @throws IOException if loading required resources fails
   */
  public static void showBoardGameScene() throws IOException {
    boolean isLudo = "LUDO".equalsIgnoreCase(
        String.valueOf(GameSelectionScene.getSelectedGame()));
    Board selectedBoard = BoardSelectionScene.getSelectedBoard();
    List<Player> players = PlayerSelectionScene.getSelectedPlayers();
    int diceCount = 2;

    GameController controller;
    if (isLudo) {
      controller = new LudoGameController(selectedBoard, diceCount);
    } else {
      controller = new LaddersController(selectedBoard, diceCount);
    }

    BoardGameScene scene = new BoardGameScene(controller);
    controller.addObserver(scene);

    GameUiAnimator animator = new GameUiAnimator(scene);
    controller.setAnimator(animator);

    controller.initializePlayers(players);

    primaryStage.setScene(loadBtnCss(scene.getScene()));
    logger.log(Level.INFO, "Switched to BoardGameScene");
  }

  /**
   * Displays the podium scene.
   */
  public static void showPodiumGameScene() {
    Scene scene = loadBtnCss(new PodiumGameScene().getScene());
    primaryStage.setScene(scene);
    logger.log(Level.INFO, "Switched to PodiumGameScene");
  }

  /**
   * <p>Creates and returns a new {@link BorderPane} with the
   * application background image.</p>
   *
   * @return a styled {@link BorderPane} to serve as the root for scenes
   */
  public static BorderPane getRootPane() {
    BorderPane root = new BorderPane();
    root.setBackground(new Background(BackgroundImageView.getBackgroundImage()));
    return root;
  }

  /**
   * <p>Adds the button stylesheet to the given scene.</p>
   *
   * @param scene the {@link Scene} to style
   * @return the same {@code scene} instance with button CSS applied
   */
  private static Scene loadBtnCss(Scene scene) {
    scene.getStylesheets()
        .add(Objects.requireNonNull(SceneManager.class.getResource(BUTTONS_CSS_PATH))
            .toExternalForm());
    return scene;
  }
}
