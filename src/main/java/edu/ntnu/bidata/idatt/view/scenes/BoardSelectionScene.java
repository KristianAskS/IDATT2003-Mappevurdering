package edu.ntnu.bidata.idatt.view.scenes;

import edu.ntnu.bidata.idatt.controller.SceneManager;
import edu.ntnu.bidata.idatt.controller.patterns.factory.LadderBoardFactory;
import edu.ntnu.bidata.idatt.controller.patterns.factory.LudoBoardFactory;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.service.BoardService;
import edu.ntnu.bidata.idatt.utils.exceptions.BoardParsingException;
import edu.ntnu.bidata.idatt.utils.exceptions.GameUIException;
import edu.ntnu.bidata.idatt.view.components.BackgroundImageView;
import edu.ntnu.bidata.idatt.view.components.Buttons;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import javafx.stage.FileChooser;

/**
 * Represents the scene where users can select a game board.
 * It allows users to choose from predefined boards or import custom boards from JSON files.
 * Extends {@link BaseScene} for common scene setup.
 */
public class BoardSelectionScene extends BaseScene {
  private static Board selectedBoard;
  private final BoardService boardService = new BoardService();
  private final String selectedGame = GameSelectionScene.getSelectedGame();
  private final LudoBoardFactory ludoBoardFactory = new LudoBoardFactory();
  private final LadderBoardFactory ladderBoardFactory = new LadderBoardFactory();
  private VBox selectionBox;
  private Label detailsTitle;
  private Label detailsDescription;

  /**
   * Constructs a new BoardSelectionScene and initializes its UI components.
   */
  public BoardSelectionScene() {
    super();
    initialize();
  }

  /**
   * Returns the currently selected game board.
   * @return The {@link Board} selected by the user, or null if no board is selected.
   */
  public static Board getSelectedBoard() {
    return selectedBoard;
  }

  /**
   * Initializes the UI layout and components for the board selection scene.
   * Sets up containers for board selection, board details, and navigation buttons.
   */
  @Override
  protected void initialize() {
    scene.setFill(Color.PINK);

    BorderPane root = this.root;
    root.setPadding(Insets.EMPTY);
    root.setStyle("-fx-font-family: 'monospace';");
    root.setBackground(new Background(BackgroundImageView.getBackgroundImage()));

    VBox main = new VBox(20);
    main.setAlignment(Pos.CENTER);
    main.setPadding(new Insets(20));

    HBox menu = new HBox(20);
    menu.setAlignment(Pos.CENTER);

    selectionBox = createSelectionContainer();
    VBox detailsBox = createDetailsContainer();

    menu.getChildren().addAll(selectionBox, detailsBox);
    main.getChildren().add(menu);
    root.setCenter(main);

    HBox bottom = createBottomContainer();
    root.setBottom(bottom);
    BorderPane.setMargin(bottom, new Insets(10));
    BorderPane.setAlignment(bottom, Pos.BOTTOM_LEFT);

    rebuildSelectionPanel();
  }

  /**
   * Returns the JavaFX {@link Scene} for this view.
   * @return The scene object.
   */
  public Scene getScene() {
    return scene;
  }

  /**
   * Creates a VBox container for board selection buttons.
   * @return The styled {@link VBox} for board options.
   */
  private VBox createSelectionContainer() {
    VBox box = new VBox(15);
    box.setAlignment(Pos.CENTER);
    box.setPadding(new Insets(20));
    box.setStyle(
        "-fx-font-family: 'monospace';"
            + "-fx-border-color: white;"
            + "-fx-border-width: 2;"
            + "-fx-background-color: rgba(0,0,0,0.5);"
            + "-fx-border-radius: 10;"
            + "-fx-background-radius: 10;"
    );
    return box;
  }

  /**
   * Creates a VBox container to display details of the selected board.
   * @return The styled {@link VBox} for board details.
   */
  private VBox createDetailsContainer() {
    VBox box = new VBox(10);
    box.setAlignment(Pos.TOP_LEFT);
    box.setPadding(new Insets(20));
    box.setPrefSize(300, 200);
    box.setStyle(
        "-fx-font-family: 'monospace';"
            + "-fx-border-color: white;"
            + "-fx-border-width: 2;"
            + "-fx-background-color: rgba(0,0,0,0.5);"
            + "-fx-border-radius: 10;"
            + "-fx-background-radius: 10;"
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

  /**
   * Creates an HBox container for bottom navigation buttons (Back, Play).
   * @return The styled {@link HBox} with navigation buttons.
   */
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
          throw new GameUIException("Failed to navigate to player selection scene: ", ex);
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

  /**
   * Sets the given board as the current board in the service and updates the details view.
   * @param board The {@link Board} to load.
   */
  private void load(Board board) {
    boardService.setBoard(board);
    updateDetails(board);
  }

  /**
   * Resets the selected board to null and clears the details display.
   */
  private void resetBoardSelection() {
    selectedBoard = null;
    detailsTitle.setText("Board Details");
    detailsDescription.setText("Select a board to see details here.");
  }

  /**
   * Updates the details panel with information from the given board and sets it as selected.
   * @param board The {@link Board} whose details are to be displayed.
   */
  private void updateDetails(Board board) {
    selectedBoard = board;
    detailsTitle.setText(board.getName());
    detailsDescription.setText(board.getDescription());
  }

  /**
   * Creates a button for a given board. When clicked, it loads the board.
   * @param b The {@link Board} for which to create a button.
   * @return A configured {@link Button}.
   */
  private Button makeBoardButton(Board b) {
    Button btn = Buttons.getSecondaryBtn(b.getName());
    btn.setOnAction(e -> {
      boardService.setBoard(b);
      updateDetails(b);
    });
    return btn;
  }

  /**
   * Called when the user clicks “Import From JSON”
   */
  private void handleImportJson() {
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Import Board JSON");
    chooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("JSON files", "*.json")
    );
    File file = chooser.showOpenDialog(scene.getWindow());
    if (file == null) {
      return;
    }

    List<Board> imported;
    try {
      imported = boardService
          .getBoardFileHandler()
          .readFromFile(file.getAbsolutePath());
    } catch (BoardParsingException | IllegalArgumentException ex) {
      showAlert(Alert.AlertType.ERROR,
          "Couldn’t parse that JSON file",
          "Your JSON must define one or more boards in this shape:\n\n" +
              "• A single object **or** an array of objects\n" +
              "• Each board object must have:\n" +
              "    – \"name\": string\n" +
              "    – \"description\": string\n" +
              "    – \"tiles\": array of tile‑objects\n" +
              "• Each tile‑object must have:\n" +
              "    – \"tileId\": integer\n" +
              "    – optional \"nextTileId\": integer\n" +
              "    – optional land‑action props:\n" +
              "        • \"landAction\": class name (SnakeAction, LadderAction…)\n" +
              "        • \"destination tile\": integer\n" +
              "        • \"description\": string\n\n" +
              "Make sure the file is valid JSON and follows this schema exactly.");
      return;

    } catch (IOException ioe) {
      showAlert(Alert.AlertType.ERROR,
          "I/O Error reading file",
          "We couldn’t read “" + file.getName() + "”.\n" +
              "Please check file permissions and try again.");
      return;
    }

    if (imported.isEmpty()) {
      showAlert(Alert.AlertType.INFORMATION,
          "Nothing to import",
          "We didn’t find any valid boards in that file.");
      return;
    }

    boardService.getBoards().addAll(imported);
    rebuildSelectionPanel();
  }

  /**
   * Clears and rebuilds the panel containing board selection buttons.
   * Includes buttons for default boards, imported boards, and the import function.
   */
  private void rebuildSelectionPanel() {
    selectionBox.getChildren().clear();

    Label title = new Label("Select a Board Variant");
    title.setStyle(
        "-fx-font-size: 24px;"
            + "-fx-font-weight: bold;"
            + "-fx-text-fill: #ffffff;"
            + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.75), 4, 0, 2, 2);"
    );

    Button importBtn = Buttons.getSecondaryBtn("Import From JSON");
    importBtn.setOnAction(e -> handleImportJson());

    List<Button> buttons = new ArrayList<>();

    Button classic = Buttons.getSecondaryBtn("CLASSIC");
    if ("LUDO".equalsIgnoreCase(selectedGame)) {
      classic.setOnAction(e -> load(ludoBoardFactory.createDefaultBoard()));
      buttons.add(classic);

    } else {
      classic.setOnAction(e -> load(ladderBoardFactory.createClassicBoard()));
      Button chaos = Buttons.getSecondaryBtn("CHAOS");
      chaos.setOnAction(e -> load(ladderBoardFactory.createChaosBoard()));

      buttons.add(classic);
      buttons.add(chaos);
      buttons.add(importBtn);

      for (Board b : boardService.getBoards()) {
        Button userBtn = Buttons.getSecondaryBtn(b.getName());
        userBtn.setOnAction(evt -> load(b));
        buttons.add(userBtn);
      }
    }

    int mid = (int) Math.ceil(buttons.size() / 2.0);
    VBox col1 = new VBox(10);
    col1.setAlignment(Pos.CENTER);
    col1.getChildren().addAll(buttons.subList(0, mid));

    VBox col2 = new VBox(10);
    col2.setAlignment(Pos.CENTER);
    if (mid < buttons.size()) {
      col2.getChildren().addAll(buttons.subList(mid, buttons.size()));
    }

    HBox columns = new HBox(20, col1, col2);
    columns.setAlignment(Pos.CENTER);

    selectionBox.getChildren().addAll(title, columns);
  }

  /**
   * Displays an alert dialog with the specified type, title, and message.
   * @param type The {@link Alert.AlertType} of the alert.
   * @param title The title of the alert window.
   * @param msg The main message content of the alert.
   */
  private void showAlert(Alert.AlertType type, String title, String msg) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(msg);
    alert.showAndWait();
  }
}