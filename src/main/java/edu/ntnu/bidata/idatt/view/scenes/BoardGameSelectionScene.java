package edu.ntnu.bidata.idatt.view.scenes;

import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_WIDTH;

import edu.ntnu.bidata.idatt.controller.patterns.factory.BoardGameFactory;
import edu.ntnu.bidata.idatt.controller.patterns.observer.ConsoleBoardGameObserver;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.service.BoardService;
import edu.ntnu.bidata.idatt.view.SceneManager;
import edu.ntnu.bidata.idatt.view.components.BackgroundImageView;
import edu.ntnu.bidata.idatt.view.components.Buttons;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class BoardGameSelectionScene {
  private static final Logger logger = Logger.getLogger(ConsoleBoardGameObserver.class.getName());
  private final Scene scene;

  private final BoardService boardService = new BoardService();
  private Board selectedBoard;
  private Label detailsTitle;
  private Label detailsDescription;

  public BoardGameSelectionScene() {
    BorderPane rootPane = SceneManager.getRootPane();
    rootPane.setPadding(Insets.EMPTY);
    rootPane.setStyle("-fx-font-family: 'monospace';");

    scene = new Scene(rootPane, SCENE_WIDTH, SCENE_HEIGHT, Color.PINK);
    rootPane.setBackground(new Background(BackgroundImageView.getBackgroundImage()));

    VBox mainWrapper = new VBox(20);
    mainWrapper.setAlignment(Pos.CENTER);
    mainWrapper.setPadding(new Insets(20));

    HBox menuContainer = new HBox(20);
    menuContainer.setAlignment(Pos.CENTER);

    VBox selectionContainer = createSelectionContainer();
    VBox detailsContainer = createDetailsContainer();

    menuContainer.getChildren().addAll(selectionContainer, detailsContainer);
    mainWrapper.getChildren().add(menuContainer);

    rootPane.setCenter(mainWrapper);

    HBox bottomContainer = createBottomContainer();
    ;
    rootPane.setBottom(bottomContainer);
    BorderPane.setMargin(bottomContainer, new Insets(10));

    BorderPane.setAlignment(bottomContainer, Pos.BOTTOM_LEFT);

    logger.log(Level.INFO, "BoardGameSelectionScene initialized");
  }

  private VBox createSelectionContainer() {
    VBox container = new VBox(15);
    container.setAlignment(Pos.CENTER);
    container.setPadding(new Insets(20));
    container.setStyle(
        "-fx-font-family: 'monospace';" +
            "-fx-border-color: white;" +
            "-fx-border-width: 2;" +
            "-fx-background-color: rgba(0, 0, 0, 0.5);" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;"
    );

    Label title = new Label("Select a Board Game");
    title.setStyle(
        "-fx-font-size: 24px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.75), 4, 0, 2, 2);"
    );

    Button smallBoardBtn = Buttons.getSecondaryBtn("Small Board");
    smallBoardBtn.setOnAction(e -> {
      Board smallBoard = BoardGameFactory.createSmallBoard();
      boardService.setBoard(smallBoard);
      updateDetails(smallBoard);
    });

    Button classicBoardBtn = Buttons.getSecondaryBtn("Classic Board");
    classicBoardBtn.setOnAction(e -> {
      Board classicBoard = BoardGameFactory.createClassicBoard();
      boardService.setBoard(classicBoard);
      updateDetails(classicBoard);
    });

    Button noLaddersSnakesBtn = Buttons.getSecondaryBtn("No Ladders and Snakes");
    noLaddersSnakesBtn.setOnAction(e -> {
      Board noLaddersAndSnakesBoard = BoardGameFactory.createBoardNoLaddersAndSnakes();
      boardService.setBoard(noLaddersAndSnakesBoard);
      updateDetails(noLaddersAndSnakesBoard);
    });

    container.getChildren().addAll(title, smallBoardBtn, classicBoardBtn, noLaddersSnakesBtn);
    return container;
  }

  private VBox createDetailsContainer() {
    VBox container = new VBox(10);
    container.setAlignment(Pos.TOP_LEFT);
    container.setPadding(new Insets(20));
    container.setPrefSize(300, 200);
    container.setStyle(
        "-fx-font-family: 'monospace';" +
            "-fx-border-color: white;" +
            "-fx-border-width: 2;" +
            "-fx-background-color: rgba(0, 0, 0, 0.5);" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;"
    );

    detailsTitle = new Label("Board Details");
    detailsTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #ffffff;");

    detailsTitle.setWrapText(true);
    detailsTitle.setTextOverrun(OverrunStyle.CLIP);
    detailsTitle.setMaxWidth(280);

    detailsDescription = new Label("Select a board to see details here.");
    detailsDescription.setStyle("-fx-font-size: 16px; -fx-text-fill: #ffffff;");

    detailsDescription.setWrapText(true);
    detailsTitle.setTextOverrun(OverrunStyle.CLIP);
    detailsTitle.setMaxWidth(280);

    container.getChildren().addAll(detailsTitle, detailsDescription);
    return container;
  }

  private HBox createBottomContainer() {
    HBox bottomBox = new HBox(20);
    bottomBox.setPadding(new Insets(10, 20, 10, 20));
    bottomBox.setAlignment(Pos.BOTTOM_CENTER);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    Button backBtn = Buttons.getBackBtn("Back");
    backBtn.setOnAction(e -> {
      resetBoardSelection();
      SceneManager.showLandingScene();
    });

    Button playBtn = Buttons.getPrimaryBtn("Play");
    playBtn.setOnAction(e -> {
      if (selectedBoard != null) {
        SceneManager.showPlayerSelectionScene();
        PlayerSelectionScene.showTotalPlayerSelectionDialog();
      } else {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Board Selected");
        alert.setHeaderText(null);
        alert.setContentText("Please select a board game before playing.");
        alert.showAndWait();
      }
    });

    bottomBox.getChildren().addAll(backBtn, spacer, playBtn);
    return bottomBox;
  }

  private void resetBoardSelection() {
    selectedBoard = null;
    detailsTitle.setText("Board Details");
    detailsDescription.setText("Select a board to see details here.");
  }

  private void updateDetails(Board board) {
    selectedBoard = board;
    detailsTitle.setText(board.getName());
    detailsDescription.setText(board.getDescription());
  }

  public Scene getScene() {
    return scene;
  }
}
