package edu.ntnu.bidata.idatt.view.scenes;

import static edu.ntnu.bidata.idatt.controller.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.controller.SceneManager.SCENE_WIDTH;
import static edu.ntnu.bidata.idatt.view.components.BoardViewUtils.VISUAL_CORRECTION;
import static edu.ntnu.bidata.idatt.view.components.TileView.TILE_SIZE_LADDER;
import static edu.ntnu.bidata.idatt.view.components.TileView.TILE_SIZE_LUDO;

import edu.ntnu.bidata.idatt.controller.GameController;
import edu.ntnu.bidata.idatt.controller.SceneManager;
import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEvent;
import edu.ntnu.bidata.idatt.controller.patterns.observer.interfaces.BoardGameObserver;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.logic.action.BackToStartAction;
import edu.ntnu.bidata.idatt.model.logic.action.LadderAction;
import edu.ntnu.bidata.idatt.model.logic.action.SnakeAction;
import edu.ntnu.bidata.idatt.model.logic.action.TileAction;
import edu.ntnu.bidata.idatt.utils.exceptions.GameUIException;
import edu.ntnu.bidata.idatt.view.components.Buttons;
import edu.ntnu.bidata.idatt.view.components.DiceView;
import edu.ntnu.bidata.idatt.view.components.LadderView;
import edu.ntnu.bidata.idatt.view.components.LaddersBoardView;
import edu.ntnu.bidata.idatt.view.components.LudoBoardView;
import edu.ntnu.bidata.idatt.view.components.SnakeView;
import edu.ntnu.bidata.idatt.view.components.TileView;
import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;


/**
 * Represents the game scene for both Ludo and Snakes & Ladders.
 * <p>
 * Responsible for rendering the board, tokens, dice controls, event log,
 * and reacting to {@link BoardGameEvent}s from the {@link GameController}.
 * </p>
 */
public class BoardGameScene implements BoardGameObserver {
  private static final Logger logger = Logger.getLogger(BoardGameScene.class.getName());
  private final Scene scene;
  private final TextArea eventLog = new TextArea("Game started! \n");
  private final Label currentPlayerLabel = new Label("Current player: -");
  private final DiceView diceView;
  private final GridPane boardGridPane;
  private final Pane tokenLayerPane = new Pane();
  private final GameController gameController;
  private final boolean isLudo =
      "LUDO".equalsIgnoreCase(String.valueOf(GameSelectionScene.getSelectedGame()));
  private final Board selectedBoardModel = BoardSelectionScene.getSelectedBoard();
  private List<Player> players = PlayerSelectionScene.getSelectedPlayers();
  private HBox stagingArea;
  private Button rollBtn;

  /**
   * Instantiates a new Board game scene.
   *
   * @param controller the controller
   */
  public BoardGameScene(GameController controller) {
    diceView = new DiceView();
    BorderPane rootPane = createRootPane();
    VBox ioContainer = createIOContainer();
    rootPane.setLeft(ioContainer);

    this.gameController = controller;

    final Board boardForGuiSetup = gameController.getBoard();

    if (isLudo) {
      this.boardGridPane = new LudoBoardView().createBoardGUI(boardForGuiSetup);
    } else {
      this.boardGridPane = new LaddersBoardView().createBoardGUI(boardForGuiSetup);
    }
    boardGridPane.setId("boardGridPane");

    tokenLayerPane.setPickOnBounds(false);
    tokenLayerPane.setMouseTransparent(true);
    tokenLayerPane.prefWidthProperty().bind(boardGridPane.widthProperty());
    tokenLayerPane.prefHeightProperty().bind(boardGridPane.heightProperty());

    Pane overlay = new Pane();
    overlay.setPickOnBounds(false);
    overlay.setMouseTransparent(true);
    overlay.prefWidthProperty().bind(boardGridPane.widthProperty());
    overlay.prefHeightProperty().bind(boardGridPane.heightProperty());

    StackPane boardStack = new StackPane(boardGridPane, overlay, tokenLayerPane);
    tokenLayerPane.toFront();
    rootPane.setCenter(boardStack);

    if (!isLudo) {
      Platform.runLater(() -> {
        Board boardForDrawing = gameController.getBoard();

        highlightActionTiles(boardForDrawing, boardGridPane);
        highlightEndTile(boardForDrawing, boardGridPane);

        LadderView.drawLadders(boardForDrawing, boardGridPane, overlay, gameController);
        SnakeView.drawSnakes(boardForDrawing, boardGridPane, overlay, gameController);
      });
    }

    StackPane container = new StackPane(rootPane);
    scene = new Scene(container, SCENE_WIDTH, SCENE_HEIGHT, Color.PINK);

    logger.log(Level.INFO, "BoardGameScene initialized. Game = {0}",
        GameSelectionScene.getSelectedGame());
  }

  /**
   * Returns hardcoded offset positions for placing multiple {@code tokenCount} tokens
   * within a single tile
   *
   * @param tokenCount number of tokens to position
   * @return array of {@link TileView} coords for each token
   */
  private static double[][] getTokenOffsets(int tokenCount) {
    return switch (tokenCount) {
      case 2 -> new double[][] {{0.2, 0.5}, {0.8, 0.5}};
      case 3 -> new double[][] {{0.2, 0.2}, {0.5, 0.5}, {0.8, 0.8}};
      case 4 -> new double[][] {{0.2, 0.2}, {0.8, 0.2}, {0.2, 0.8}, {0.8, 0.8}};
      case 5 -> new double[][] {{0.2, 0.2}, {0.8, 0.2}, {0.2, 0.8}, {0.8, 0.8}, {0.5, 0.5}};
      default -> new double[][] {{0.5, 0.5}};
    };
  }

  /**
   * Calculates the center point of a tile and applying visual correction.
   *
   * @param bounds the bounding box of the tile
   * @return an 2d array
   */
  @SuppressWarnings("unused")
  public static double[] getTileCenter(Bounds bounds) {
    double x = bounds.getMinX() + bounds.getWidth() * 0.5 + VISUAL_CORRECTION;
    double y = bounds.getMinY() + bounds.getHeight() * 0.5;
    logger.info(() -> "Center:(" + x + "," + y + ")");
    return new double[] {x, y};
  }

  /**
   * Initializes the staging area and resets each player’s position to start.
   *
   * @param players list of players to display in the UI
   */
  public void setupPlayersUI(List<Player> players) {
    this.players = players;
    players.forEach(player -> player.setCurrentTileId(0));
    stagingArea.getChildren().clear();
    Label lbl = new Label("Waiting to start");
    lbl.setWrapText(true);
    stagingArea.getChildren().add(lbl);
    players.forEach(player -> stagingArea.getChildren().add(player.getToken()));
  }

  /**
   * Highlights the board’s final tile by coloring it gold and adding a finish flag.
   *
   * @param board         the game board model
   * @param boardGridPane the GridPane containing tile views
   */
  private void highlightEndTile(Board board, GridPane boardGridPane) {
    TileView tileView = (TileView) boardGridPane.lookup("#tile" + board.getTiles().size());
    tileView.setStyle("-fx-background-color: #FFD700");
    Label lbl = new Label("🏁");
    lbl.setStyle("-fx-text-fill: black");
    lbl.setFont(Font.font("Arial", 35));
    tileView.getChildren().add(lbl);
  }

  /**
   * Marks action tiles with a background color and symbol.
   *
   * @param board         the game board model containing tiles and actions
   * @param boardGridPane the GridPane of tile views to update
   */
  private void highlightActionTiles(Board board, GridPane boardGridPane) {
    board.getTiles().values().forEach(tile -> {
      TileAction action = tile.getLandAction();
      if (action == null
          || action instanceof LadderAction
          || action instanceof SnakeAction) {
        return;
      }

      TileView tileView = (TileView) boardGridPane.lookup("#tile" + tile.getTileId());
      if (tileView == null) {
        return;
      }

      String bgColor = action instanceof BackToStartAction ? "#808080" : "#FFFFFF";
      tileView.setStyle("-fx-background-color: " + bgColor + ";");

      String text = action instanceof BackToStartAction ? "⏮" : "🛑";
      Label lbl = new Label(text);
      lbl.setStyle("-fx-text-fill: black");
      lbl.setFont(Font.font("Arial", 28));
      tileView.getChildren().add(lbl);
    });
  }

  /**
   * Arranges all token nodes within the given tile view using offsets coords.
   *
   * @param tile the {@link TileView} containing token nodes
   */
  public void setTokenPositionOnTile(TileView tile) {
    List<Node> tokens = tile.getChildren().stream()
        .filter(node -> node instanceof TokenView).toList();
    double[][] offsets = getTokenOffsets(tokens.size());
    int tileSize = isLudo ? TILE_SIZE_LUDO : TILE_SIZE_LADDER;
    IntStream.range(0, tokens.size())
        .forEach(i -> {
          Node tokenNode = tokens.get(i);
          double x = (tileSize - offsets[i][0] * tileSize * 2) / 2;
          double y = (tileSize - offsets[i][1] * tileSize * 2) / 2;
          tokenNode.setTranslateX(x);
          tokenNode.setTranslateY(y);
        });
  }

  /**
   * Reposition token on tile based on offset coords
   */
  public void repositionTokenOnTile() {
    players.forEach(player -> {
      int tileId = player.getCurrentTileId();
      if (tileId > 0) {
        var tileView = (TileView) scene.lookup("#tile" + tileId);
        if (tileView != null) {
          setTokenPositionOnTile(tileView);
        }
      }
    });
  }

  /**
   * Processes a {@link BoardGameEvent} on the Application Thread:
   * updates the event log, repositions tokens and handles game start and finish transitions.
   *
   * @param event the board game event containing the player, old/new tiles, and event type
   */
  @Override
  public void onEvent(BoardGameEvent event) {
    Platform.runLater(() -> {
      Player player = event.player();

      int oldId = event.oldTile() != null ? event.oldTile().getTileId() : 0;
      int newId = event.newTile() != null ? event.newTile().getTileId() : 0;

      switch (event.eventType()) {
        case PLAYER_MOVED -> {
          eventLog.appendText(
              String.format("%s moved from %d to %d%n", player.getName(), oldId, newId)
          );
          repositionTokenOnTile();
        }
        case PLAYER_LADDER_ACTION -> {
          eventLog.appendText(
              String.format("%s climbed a ladder from %d to %d%n", player.getName(), oldId, newId)
          );
          repositionTokenOnTile();
        }
        case PLAYER_BACK_START_ACTION -> {
          eventLog.appendText(
              String.format("%s hit “Back to Start” on %d and jumped to %d%n",
                  player.getName(), oldId, newId)
          );
          repositionTokenOnTile();
        }
        case PLAYER_SKIP_TURN_ACTION -> {
          eventLog.appendText(
              String.format("%s landed on “Skip Turn” at %d and will skip next turn%n",
                  player.getName(), oldId)
          );
          repositionTokenOnTile();
        }
        case PLAYER_FINISHED -> {
          eventLog.appendText(
              String.format("Player %s finished!%n", player.getName())
          );
          repositionTokenOnTile();
        }
        case GAME_FINISHED -> {
          eventLog.appendText("Game over!");
          List<Player> podium = event.finalRanking();
          Platform.runLater(() -> {
            PodiumGameScene.setFinalRanking(podium);
            SceneManager.showPodiumGameScene();
          });
        }
        case GAME_STARTED -> {
          setupPlayersUI(players);
        }
        case CURRENT_PLAYER_CHANGED -> {
          setCurrentPlayer(event.player());
        }
        default -> repositionTokenOnTile();
      }
      if (rollBtn.isDisabled() &&
          switch (event.eventType()) {
            case PLAYER_MOVED, PLAYER_LADDER_ACTION,
                 PLAYER_FINISHED, GAME_FINISHED,
                 PLAYER_BACK_START_ACTION, PLAYER_SKIP_TURN_ACTION -> true;
            default -> false;
          }) {
        rollBtn.setDisable(false);
      }
    });
  }

  /**
   * Gets scene.
   *
   * @return the scene
   */
  public Scene getScene() {
    return scene;
  }

  /**
   * Gets token layer.
   *
   * @return the token layer
   */
  public Pane getTokenLayer() {
    return tokenLayerPane;
  }

  /**
   * Creates the root BorderPane with the scene background style applied.
   *
   * @return a styled BorderPane for the main layout
   */
  private BorderPane createRootPane() {
    BorderPane rootBorderPane = new BorderPane();
    rootBorderPane.setStyle("-fx-background-color: #1A237E;");
    return rootBorderPane;
  }

  /**
   * Builds the staging area HBox for displaying tokens before game start.
   *
   * @return an HBox with padding, alignment, and translucent background
   */
  private HBox createStagingArea() {
    HBox stagingArea = new HBox(10);
    stagingArea.setPadding(new Insets(10));
    stagingArea.setAlignment(Pos.CENTER);
    stagingArea.setStyle(
        "-fx-background-color: rgba(255,255,255,0.2);"
            + "-fx-border-color: white; -fx-border-radius: 8;"
    );
    return stagingArea;
  }

  /**
   * Configures the event log TextArea: non-editable, monospace font, and dark theme.
   *
   * @return the initialized TextArea used for game output
   */
  private TextArea createOutputArea() {
    eventLog.setEditable(false);
    eventLog.setWrapText(true);
    eventLog.setScrollTop(Double.MAX_VALUE);
    eventLog.setFont(Font.font("monospace", FontWeight.BOLD, 16));
    eventLog.setStyle("-fx-control-inner-background: black; -fx-text-fill: white;");
    eventLog.setPrefHeight(200);
    return eventLog;
  }

  /**
   * Assembles the I/O container VBox, including dice controls, current player label,
   * output area, staging area, and navigation buttons.
   *
   * @return a VBox containing all side-panel UI elements
   */
  private VBox createIOContainer() {
    VBox ioContainer = new VBox();
    ioContainer.setPrefWidth(250);
    ioContainer.setPadding(new Insets(30));
    ioContainer.setSpacing(15);
    ioContainer.setAlignment(Pos.TOP_CENTER);
    ioContainer.setStyle(
        "-fx-background-color: #7DBED7;"
            + "-fx-background-radius: 0 40 40 0;"
            + "-fx-border-radius: 0 40 40 0;"
            + "-fx-border-color: black;"
            + "-fx-border-width: 1;"
    );
    DropShadow dropShadow = new DropShadow(10, Color.color(0, 0, 0, 0.3));
    ioContainer.setEffect(dropShadow);

    HBox diceBox = diceView.getDiceImageHBox();
    diceBox.setAlignment(Pos.CENTER);
    StackPane diceContainer = new StackPane(diceBox);
    diceContainer.setAlignment(Pos.CENTER);
    ioContainer.getChildren().add(diceContainer);

    rollBtn = diceView.getRollDiceBtn();
    ioContainer.getChildren().add(rollBtn);
    rollBtn.setOnAction(event -> {
      //Sett den til false om du skal teste
      rollBtn.setDisable(true);
      Timeline timeline = diceView.createRollDiceAnimation(() -> {
        int result = diceView.rollResultProperty().get();
        gameController.handlePlayerTurn(result);
      });
      timeline.play();
    });

    Label rollLbl = new Label();
    rollLbl.setFont(Font.font("monospace", FontWeight.BOLD, 16));
    rollLbl.textProperty().bind(
        diceView.rollResultProperty().asString("Roll result: %d")
    );
    ioContainer.getChildren().add(rollLbl);

    currentPlayerLabel.setFont(Font.font("monospace", FontWeight.BOLD, 16));
    currentPlayerLabel.setTextFill(Color.BLACK);
    ioContainer.getChildren().add(currentPlayerLabel);

    Label outputLbl = new Label("Output");
    outputLbl.setFont(Font.font("monospace", FontWeight.BOLD, 16));
    outputLbl.setTextFill(Color.BLACK);
    ioContainer.getChildren().add(outputLbl);
    ioContainer.getChildren().add(createOutputArea());

    var spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);
    ioContainer.getChildren().add(spacer);

    this.stagingArea = createStagingArea();
    ioContainer.getChildren().add(stagingArea);

    Button back = Buttons.getBackBtn("Back");
    back.setOnAction(event -> {
      try {
        SceneManager.showPlayerSelectionScene();
      } catch (IOException exception) {
        throw new GameUIException("Failed to navigate to player selection scene", exception);
      }
    });
    ioContainer.getChildren().add(back);

    return ioContainer;
  }

  /**
   * Sets current player.
   *
   * @param player the player
   */
  public void setCurrentPlayer(Player player) {
    String displayedText =
        (player == null) ? "Current player: " : "Current player: " + player.getName();
    currentPlayerLabel.setText(displayedText);
    if (rollBtn != null && rollBtn.isDisabled()) {
      rollBtn.setDisable(false);
    }
  }

  /**
   * Gets board.
   *
   * @return the board
   */
  public Board getBoard() {
    return selectedBoardModel;
  }
}
