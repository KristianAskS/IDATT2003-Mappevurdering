package edu.ntnu.bidata.idatt.view.scenes;

import static edu.ntnu.bidata.idatt.controller.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.controller.SceneManager.SCENE_WIDTH;
import static edu.ntnu.bidata.idatt.model.entity.Ladder.VISUAL_CORRECTION;
import static edu.ntnu.bidata.idatt.model.service.BoardService.BOARD_FILE_PATH;
import static edu.ntnu.bidata.idatt.view.components.TileView.TILE_SIZE;

import edu.ntnu.bidata.idatt.controller.BoardGameController;
import edu.ntnu.bidata.idatt.controller.SceneManager;
import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEvent;
import edu.ntnu.bidata.idatt.controller.patterns.observer.interfaces.BoardGameObserver;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.service.BoardService;
import edu.ntnu.bidata.idatt.model.service.PlayerService;
import edu.ntnu.bidata.idatt.view.components.BoardView;
import edu.ntnu.bidata.idatt.view.components.Buttons;
import edu.ntnu.bidata.idatt.view.components.DiceView;
import edu.ntnu.bidata.idatt.view.components.LadderView;
import edu.ntnu.bidata.idatt.view.components.TileView;
import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BoardGameScene implements BoardGameObserver {
  private static final Logger logger = Logger.getLogger(BoardGameScene.class.getName());
  private final Scene scene;
  private final TextArea eventLog = new TextArea("Game started! \n");
  private final PlayerService playerService = new PlayerService();
  private final BoardGameController boardGameController;
  private final DiceView diceView;
  private final GridPane boardGridPane;
  private final Pane tokenLayerPane = new Pane();
  private final ObservableList<XYChart.Series<Number, Number>> dataSeries =
      FXCollections.observableArrayList();
  List<Player> players = PlayerSelectionScene.getSelectedPlayers();
  private boolean isGameFinished = false;

  public BoardGameScene() throws IOException {
    diceView = new DiceView();
    BorderPane rootPane = createRootPane();
    rootPane.setLeft(createIOContainer());

    BoardService boardService = new BoardService();

    //Denne må hentes fra forrige scene brukeren velger antall terninger
    int numbOfDice = 1;

    //denne skal ikke brukes da UI ikke skal kommunisere med forettningslogikken
    //Board board = BoardGameFactory.createClassicBoard();
    //Board board = boardService.readBoardFromFile("data/games/laddersAndSnakes.json").get(0);
    Board board = BoardGameSelectionScene.getSelectedBoard();
    boardService.setBoard(board);
    boardService.writeBoardToFile(List.of(board), BOARD_FILE_PATH);

    boardGameController =
        new BoardGameController(this, playerService, board, numbOfDice);

    this.boardGridPane = BoardView.createBoardGUI(board);
    boardGridPane.setId("boardGridPane");

    tokenLayerPane.setPickOnBounds(false);
    tokenLayerPane.setMouseTransparent(true);
    tokenLayerPane.prefWidthProperty().bind(boardGridPane.widthProperty());
    tokenLayerPane.prefHeightProperty().bind(boardGridPane.heightProperty());

    Pane ladderOverlay = new Pane();
    ladderOverlay.setPickOnBounds(false);
    ladderOverlay.setMouseTransparent(true);
    ladderOverlay.prefWidthProperty().bind(boardGridPane.widthProperty());
    ladderOverlay.prefHeightProperty().bind(boardGridPane.heightProperty());

    StackPane boardStackPane = new StackPane(boardGridPane, ladderOverlay, tokenLayerPane);
    tokenLayerPane.toFront();
    rootPane.setCenter(boardStackPane);

    Platform.runLater(() -> {
      LadderView.drawLadders(board, boardGridPane, ladderOverlay);
    });


    // Pakke inn rootPane i en StackPane slik at vi kan skalere rootPane (for senere)
    StackPane container = new StackPane(rootPane);
    scene = new Scene(container, SCENE_WIDTH, SCENE_HEIGHT, Color.PINK);

    // Initialiser spillere og plasser tokenene på starttilen (midlertidig løsning)
    initializePlayers();

    logger.log(Level.INFO, "BoardGameGUI created");
  }

  /**
   * Returnerer hardkodede offset-koordinater basert på antall tokens på en tile.
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

  // Currently not used
  public static double[] getTileCenter(Bounds bounds) {
    double x = bounds.getMinX() + bounds.getWidth() * 0.5 + VISUAL_CORRECTION;
    double y = bounds.getMinY() + bounds.getHeight() * 0.5;
    logger.info(() -> "Center (x,y): (" + x + "," + y + ")");
    return new double[] {x, y};
  }

  private BorderPane createRootPane() {
    BorderPane root = new BorderPane();
    root.setStyle("-fx-background-color: #1A237E;");
    return root;
  }

  private void initializePlayers() {
    players.forEach(player -> player.setCurrentTileId(1));

    playerService.setPlayers(players);

    TileView startTile = (TileView) scene.lookup("#tile1");
    if (startTile != null) {
      List<Node> playerTokens = new ArrayList<>();
      players.stream()
          .filter(player -> player.getToken() != null)
          .forEach(player -> playerTokens.add(player.getToken()));

      startTile.getChildren().addAll(playerTokens);
      setTokenPositionOnTile(startTile);
    }
  }

  public void setTokenPositionOnTile(TileView tile) {
    List<Node> tokens = tile.getChildren().stream()
        .filter(node -> node instanceof TokenView)
        .toList();

    final double[][] offsets = getTokenOffsets(tokens.size());

    IntStream.range(0, tokens.size())
        .forEach(i -> {
          Node token = tokens.get(i);
          double x = (TILE_SIZE - offsets[i][0] * TILE_SIZE * 2) / 2;
          double y = (TILE_SIZE - offsets[i][1] * TILE_SIZE * 2) / 2;
          token.setTranslateX(x);
          token.setTranslateY(y);
        });
  }

  public Scene getScene() {
    return scene;
  }

  private VBox createIOContainer() {
    VBox container = new VBox();
    container.setPrefWidth(250);
    container.setPadding(new Insets(30));
    container.setSpacing(15);
    container.setAlignment(Pos.TOP_CENTER);
    container.setStyle(
        "-fx-background-color: #7DBED7;"
            + "-fx-background-radius: 0 40 40 0;"
            + "-fx-border-radius: 0 40 40 0;"
            + "-fx-border-color: black;"
            + "-fx-border-width: 1;"
    );

    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(10.0);
    dropShadow.setOffsetX(10.0);
    dropShadow.setOffsetY(10.0);
    dropShadow.setColor(Color.color(0, 0, 0, 0.3));
    container.setEffect(dropShadow);

    assert diceView != null;
    container.getChildren().add(diceView.getDiceImageView());

    Button rollDiceBtn = diceView.getRollDiceBtn();
    container.getChildren().add(rollDiceBtn);
    rollDiceBtn.setOnAction(e -> {
      Timeline timeline = diceView.createRollDiceAnimation(() -> {
        int result = diceView.rollResultProperty().get();
        logger.log(Level.INFO, "Passing to controller: " + result);
        boardGameController.handlePlayerTurn(result);
        rollDiceBtn.setDisable(false);
      });
      timeline.play();
    });

    Label rollResultLabel = new Label();
    rollResultLabel.setFont(Font.font("monospace", FontWeight.BOLD, 16));
    rollResultLabel.textProperty().bind(diceView.rollResultProperty().asString("Roll result: %d"));
    container.getChildren().add(rollResultLabel);

    Label outputLabel = new Label("Output");
    outputLabel.setFont(Font.font("monospace", FontWeight.BOLD, 16));
    outputLabel.setTextFill(Color.BLACK);
    container.getChildren().add(outputLabel);

    container.getChildren().add(createOutputArea());

    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);
    container.getChildren().add(spacer);

    Button backBtn = Buttons.getBackBtn("Back");
    container.getChildren().add(backBtn);
    backBtn.setOnAction(e -> {
      try {
        SceneManager.showPlayerSelectionScene();
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    });

    return container;
  }

  private TextArea createOutputArea() {
    /*
    VBox outputArea = new VBox();
    outputArea.setSpacing(5);
    outputArea.setPadding(new Insets(10));
    outputArea.setStyle(
        "-fx-background-color: #2B2B2B;"
            + "-fx-background-radius: 10;"
            + "-fx-border-radius: 10;"
            + "-fx-border-color: black;"
            + "-fx-border-width: 1;"
            + "-fx-effect: dropshadow(three-pass-box, black, 10, 0, 0, 0);"
            + "-fx-padding: 10 20 150 20;"
    );

     */

    eventLog.setEditable(false);
    eventLog.setWrapText(true);
    eventLog.setScrollTop(Double.MAX_VALUE);
    eventLog.setFont(Font.font("monospace", FontWeight.BOLD, 16));
    eventLog.setStyle("-fx-control-inner-background: black; -fx-text-fill: white;");
    eventLog.setPrefHeight(200);
    /*
    outputArea.getChildren().addAll(eventLog, roundLabel);
    return outputArea;

     */
    return eventLog;
  }

  private VBox createPerformanceMeter() {
    VBox graphContainer = new VBox();
    //graphContainer.setAlignment(Pos.CENTER_LEFT);
    graphContainer.setFillWidth(true);

    NumberAxis xAxis = new NumberAxis();
    xAxis.setLabel("Turn");
    xAxis.setTickUnit(1);
    xAxis.setAnimated(true);
    xAxis.setMinorTickCount(0);
    xAxis.setForceZeroInRange(false);
    xAxis.setMinorTickVisible(false);
    NumberAxis yAxis = new NumberAxis("Dice Number", 0, 7, 1);
    yAxis.setMinorTickVisible(false);
    yAxis.setMinorTickCount(0);
    yAxis.setAnimated(true);
    LineChart<Number, Number> performanceGraph = new LineChart<Number, Number>(xAxis, yAxis);
    performanceGraph.setTitle("Performance Meter");
    performanceGraph.setMaxWidth(300);
    performanceGraph.setData(dataSeries);
    performanceGraph.setMaxHeight((double) 550 / 2 - 50);
    performanceGraph.setAnimated(true);
    graphContainer.getChildren().add(performanceGraph);
    return graphContainer;
  }

  @Override
  public void onEvent(BoardGameEvent event) {
    Platform.runLater(() -> {
      switch (event.eventType()) {
        case PLAYER_MOVED -> {
          String text = event.player().getName()
              + " moved from " + event.oldTile().getTileId()
              + " to " + event.newTile().getTileId() + "\n";
          eventLog.appendText(text);
        }
        case PLAYER_LADDER_ACTION -> {
          String text = event.player().getName()
              + " climbed a ladder from " + event.oldTile().getTileId()
              + " to " + event.newTile().getTileId() + "\n";
          eventLog.appendText(text);
        }
        case PLAYER_FINISHED -> {
          eventLog.appendText("Player " + event.player().getName() + " finished!\n");
        }
        case GAME_FINISHED -> {
          if (!isGameFinished) {
            isGameFinished = true;
            SceneManager.showPodiumGameScene();
          }
        }
        default -> {
          //Remove this in release versions
          eventLog.appendText("Testing: Unknown event type: " + event.eventType() + "\n");
        }
      }
    });
  }


  public Pane getTokenLayer() {
    return tokenLayerPane;
  }
}
