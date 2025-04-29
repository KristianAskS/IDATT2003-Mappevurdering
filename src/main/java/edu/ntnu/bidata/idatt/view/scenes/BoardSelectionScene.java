package edu.ntnu.bidata.idatt.view.scenes;

import static edu.ntnu.bidata.idatt.controller.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.controller.SceneManager.SCENE_WIDTH;

import edu.ntnu.bidata.idatt.controller.SceneManager;
import edu.ntnu.bidata.idatt.controller.patterns.factory.BoardGameFactory;
import edu.ntnu.bidata.idatt.controller.patterns.observer.ConsoleBoardGameObserver;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.service.BoardService;
import edu.ntnu.bidata.idatt.view.components.BackgroundImageView;
import edu.ntnu.bidata.idatt.view.components.Buttons;
import java.io.IOException;
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

public class BoardSelectionScene {
  private static final Logger logger =
      Logger.getLogger(ConsoleBoardGameObserver.class.getName());

  private static Board selectedBoard;

  private final Scene scene;
  private final BoardService boardService = new BoardService();
  private final String selectedGame = GameSelectionScene.getSelectedGame();
  private Label detailsTitle;
  private Label detailsDescription;

  public BoardSelectionScene() {
    BorderPane root = SceneManager.getRootPane();
    root.setPadding(Insets.EMPTY);
    root.setStyle("-fx-font-family: 'monospace';");

    scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, Color.PINK);
    root.setBackground(new Background(BackgroundImageView.getBackgroundImage()));

    VBox main = new VBox(20);
    main.setAlignment(Pos.CENTER);
    main.setPadding(new Insets(20));

    HBox menu = new HBox(20);
    menu.setAlignment(Pos.CENTER);

    VBox selectionBox = createSelectionContainer();
    VBox detailsBox = createDetailsContainer();

    menu.getChildren().addAll(selectionBox, detailsBox);
    main.getChildren().add(menu);
    root.setCenter(main);

    HBox bottom = createBottomContainer();
    root.setBottom(bottom);
    BorderPane.setMargin(bottom, new Insets(10));
    BorderPane.setAlignment(bottom, Pos.BOTTOM_LEFT);

    logger.log(Level.INFO, "BoardSelectionScene initialised for game: {0}", selectedGame);
  }

  public static Board getSelectedBoard() {
    return selectedBoard;
  }

  public Scene getScene() {
    return scene;
  }

  private VBox createSelectionContainer() {
    VBox box = new VBox(15);
    box.setAlignment(Pos.CENTER);
    box.setPadding(new Insets(20));
    box.setStyle(
        "-fx-font-family: 'monospace';" +
            "-fx-border-color: white;" +
            "-fx-border-width: 2;" +
            "-fx-background-color: rgba(0,0,0,0.5);" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;"
    );

    Label title = new Label("Select a Board Variant");
    title.setStyle(
        "-fx-font-size: 24px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.75), 4, 0, 2, 2);"
    );

    if ("LUDO".equalsIgnoreCase(selectedGame)) {
      addLudoButtons(box);
    } else {
      addSnakesAndLaddersButtons(box);
    }

    box.getChildren().add(0, title);
    return box;
  }

  private void addLudoButtons(VBox parent) {
    Button classic = Buttons.getSecondaryBtn("Classic Ludo");
    classic.setOnAction(e -> load(BoardGameFactory.createLudoClassicBoard()));

    Button quick = Buttons.getSecondaryBtn("Quick Ludo");
    quick.setOnAction(e -> load(BoardGameFactory.createLudoQuickBoard()));

    Button mega = Buttons.getSecondaryBtn("Mega Ludo (8 players)");
    mega.setOnAction(e -> load(BoardGameFactory.createLudoMegaBoard()));

    parent.getChildren().addAll(classic, quick, mega);
  }

  private void addSnakesAndLaddersButtons(VBox parent) {
    Button small = Buttons.getSecondaryBtn("Small Board");
    small.setOnAction(e -> load(BoardGameFactory.createSmallBoard()));

    Button classic = Buttons.getSecondaryBtn("Classic Board");
    classic.setOnAction(e -> load(BoardGameFactory.createClassicBoard()));

    Button none = Buttons.getSecondaryBtn("No Snakes / Ladders");
    none.setOnAction(e -> load(BoardGameFactory.createBoardNoLaddersAndSnakes()));

    parent.getChildren().addAll(small, classic, none);
  }

  private VBox createDetailsContainer() {
    VBox box = new VBox(10);
    box.setAlignment(Pos.TOP_LEFT);
    box.setPadding(new Insets(20));
    box.setPrefSize(300, 200);
    box.setStyle(
        "-fx-font-family: 'monospace';" +
            "-fx-border-color: white;" +
            "-fx-border-width: 2;" +
            "-fx-background-color: rgba(0,0,0,0.5);" +
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

    box.getChildren().addAll(detailsTitle, detailsDescription);
    return box;
  }

  private HBox createBottomContainer() {
    HBox bottomContainer = new HBox(20);
    bottomContainer.setPadding(new Insets(10, 20, 10, 20));
    bottomContainer.setAlignment(Pos.BOTTOM_CENTER);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    Button backBtn = Buttons.getBackBtn("Back");
    backBtn.setOnAction(e -> {
      resetBoardSelection();
      SceneManager.showGameSelectionScene();
    });

    Button playBtn = Buttons.getPrimaryBtn("Play");
    playBtn.setOnAction(e -> {
      if (selectedBoard != null) {
        try {
          SceneManager.showPlayerSelectionScene();
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
        PlayerSelectionScene.showTotalPlayerSelectionDialog();
      } else {
        new Alert(Alert.AlertType.WARNING, "Please select a board variant before playing.")
            .showAndWait();
      }
    });

    bottomContainer.getChildren().addAll(backBtn, spacer, playBtn);
    return bottomContainer;
  }

  private void load(Board board) {
    boardService.setBoard(board);
    updateDetails(board);
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
}
