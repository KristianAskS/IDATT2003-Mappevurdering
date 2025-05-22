package edu.ntnu.bidata.idatt.view.scenes;

import static edu.ntnu.bidata.idatt.controller.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.controller.SceneManager.SCENE_WIDTH;

import edu.ntnu.bidata.idatt.controller.SceneManager;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.view.components.Buttons;
import java.util.List;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * <p>Represents the podium scene displaying the top three players.</p>
 *
 * @author Kristian Ask Selmer
 */
public class PodiumGameScene {

  private static List<Player> finalRanking;
  private final Scene scene;

  /**
   * <p>Constructs the podium scene, applying styling,
   * creating the podium layout, and setting up button event handlers.</p>
   *
   * @throws NullPointerException if the CSS stylesheet resource cannot be found
   */
  public PodiumGameScene() {
    BorderPane root = SceneManager.getRootPane();
    root.setPadding(Insets.EMPTY);
    root.setStyle("-fx-font-family: 'monospace';");

    root.setBackground(
        new Background(new BackgroundFill(Color.web("#263238"), CornerRadii.EMPTY, Insets.EMPTY)));

    scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);

    Label title = new Label("Congratulations:");
    title.getStyleClass().add("podium-title");

    HBox podium = createPodium(finalRanking);
    podium.getStyleClass().add("podium-container");

    Button playAgain = Buttons.getSmallPrimaryBtn("Play again");
    playAgain.setOnAction(event -> SceneManager.showBoardSelectionScene());

    Button mainMenu = Buttons.getExitBtn("Main menu");
    mainMenu.setOnAction(event -> SceneManager.showLandingScene());

    HBox buttons = new HBox(30, playAgain, mainMenu);
    buttons.setAlignment(Pos.CENTER);

    VBox center = new VBox(40, title, podium, buttons);
    center.setAlignment(Pos.CENTER);
    root.setCenter(center);

    scene.getStylesheets().add(
        Objects.requireNonNull(
                getClass().getResource("/edu/ntnu/bidata/idatt/styles/PodiumScene.css"))
            .toExternalForm());
  }

  /**
   * Sets the list of players to be displayed in final ranking order.
   *
   * @param ranking the list of players where index 0 = first place, 1 = second, 2 = third
   */
  public static void setFinalRanking(List<Player> ranking) {
    finalRanking = ranking;
  }

  /**
   * Returns the scene for this podium display.
   *
   * @return the configured scene object
   */
  public Scene getScene() {
    return scene;
  }

  /**
   * <p>HBox containing styled tiers for the top three players.</p>
   *
   * @param ranking the list of players in final order
   * @return an HBox containing the second, first, and third place tiers
   */
  private HBox createPodium(List<Player> ranking) {
    Player first = (ranking != null && !ranking.isEmpty()) ? ranking.get(0) : null;
    Player second = (ranking != null && ranking.size() > 1) ? ranking.get(1) : null;
    Player third = (ranking != null && ranking.size() > 2) ? ranking.get(2) : null;

    VBox firstBox = createTierBox(1, first);
    VBox secondBox = createTierBox(2, second);
    VBox thirdBox = createTierBox(3, third);

    firstBox.getStyleClass().add("pillar-first");
    secondBox.getStyleClass().add("pillar-second");
    thirdBox.getStyleClass().add("pillar-third");

    HBox h = new HBox(40, secondBox, firstBox, thirdBox);
    h.setAlignment(Pos.BOTTOM_CENTER);
    return h;
  }

  /**
   * <p>Creates a single podium box with place label and player name.</p>
   *
   * @param place the rank position (1 for first, 2 for second, 3 for third)
   * @param player the Player object to display
   * @return a VBox styled as a podium pillar containing the place and name
   */
  private VBox createTierBox(int place, Player player) {
    Label placeLbl = new Label(String.valueOf(place));
    placeLbl.getStyleClass().add("pillar-place");

    Label nameLbl = new Label(player != null ? player.getName() : "—");
    nameLbl.getStyleClass().add("pillar-name");

    VBox pillar = new VBox(10, placeLbl, nameLbl);
    pillar.setAlignment(Pos.TOP_CENTER);
    pillar.getStyleClass().add("pillar");
    return pillar;
  }
}
