// src/main/java/edu/ntnu/bidata/idatt/view/scenes/BoardGameScene.java

package edu.ntnu.bidata.idatt.view.scenes;

import static edu.ntnu.bidata.idatt.controller.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.controller.SceneManager.SCENE_WIDTH;
import static edu.ntnu.bidata.idatt.model.entity.Ladder.VISUAL_CORRECTION;
import static edu.ntnu.bidata.idatt.view.components.TileView.TILE_SIZE;

import edu.ntnu.bidata.idatt.controller.BoardGameController;
import edu.ntnu.bidata.idatt.controller.SceneManager;
import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEvent;
import edu.ntnu.bidata.idatt.controller.patterns.observer.interfaces.BoardGameObserver;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.view.components.BoardView;
import edu.ntnu.bidata.idatt.view.components.Buttons;
import edu.ntnu.bidata.idatt.view.components.DiceView;
import edu.ntnu.bidata.idatt.view.components.LadderView;
import edu.ntnu.bidata.idatt.view.components.TileView;
import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.io.IOException;
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
import javafx.scene.layout.HBox;
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
  private final DiceView diceView;
  private final GridPane boardGridPane;
  private final Pane tokenLayerPane = new Pane();
  private final ObservableList<XYChart.Series<Number, Number>> dataSeries =
      FXCollections.observableArrayList();
  private final BoardGameController boardGameController;
  private List<Player> players = PlayerSelectionScene.getSelectedPlayers();
  private HBox stagingArea;
  private boolean isGameFinished = false;

  public BoardGameScene() throws IOException {
    diceView = new DiceView();

    BorderPane rootPane = createRootPane();
    VBox ioContainer = createIOContainer();
    rootPane.setLeft(ioContainer);

    Board board = BoardGameSelectionScene.getSelectedBoard();
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

    StackPane boardStack = new StackPane(boardGridPane, ladderOverlay, tokenLayerPane);
    tokenLayerPane.toFront();
    rootPane.setCenter(boardStack);

    Platform.runLater(() ->
        LadderView.drawLadders(board, boardGridPane, ladderOverlay)
    );

    int numbOfDice = 1;  // should be an argument value or static
    boardGameController = new BoardGameController(this, board, numbOfDice);

    StackPane container = new StackPane(rootPane);
    scene = new Scene(container, SCENE_WIDTH, SCENE_HEIGHT, Color.PINK);

    boardGameController.initializePlayers(players);

    logger.log(Level.INFO, "BoardGameScene initialized");
  }

  private static double[][] getTokenOffsets(int tokenCount) {
    return switch (tokenCount) {
      case 2 -> new double[][] {{0.2, 0.5}, {0.8, 0.5}};
      case 3 -> new double[][] {{0.2, 0.2}, {0.5, 0.5}, {0.8, 0.8}};
      case 4 -> new double[][] {{0.2, 0.2}, {0.8, 0.2}, {0.2, 0.8}, {0.8, 0.8}};
      case 5 -> new double[][] {{0.2, 0.2}, {0.8, 0.2}, {0.2, 0.8}, {0.8, 0.8}, {0.5, 0.5}};
      default -> new double[][] {{0.5, 0.5}};
    };
  }

  @SuppressWarnings("unused")
  public static double[] getTileCenter(Bounds bounds) {
    double x = bounds.getMinX() + bounds.getWidth() * 0.5 + VISUAL_CORRECTION;
    double y = bounds.getMinY() + bounds.getHeight() * 0.5;
    logger.info(() -> "Center:(" + x + "," + y + ")");
    return new double[] {x, y};
  }


  public void setupPlayersUI(List<Player> players) {
    this.players = players;
    players.forEach(player -> player.setCurrentTileId(0));
    stagingArea.getChildren().clear();
    Label lbl = new Label("Waiting to start");
    lbl.setWrapText(true);
    stagingArea.getChildren().add(lbl);
    players.forEach(player -> stagingArea.getChildren().add(player.getToken()));
  }


  public void setTokenPositionOnTile(TileView tile) {
    List<Node> tokens = tile.getChildren().stream()
        .filter(node -> node instanceof TokenView).toList();
    double[][] offsets = getTokenOffsets(tokens.size());
    IntStream.range(0, tokens.size())
        .forEach(i -> {
          Node tokenNode = tokens.get(i);
          double x = (TILE_SIZE - offsets[i][0] * TILE_SIZE * 2) / 2;
          double y = (TILE_SIZE - offsets[i][1] * TILE_SIZE * 2) / 2;
          tokenNode.setTranslateX(x);
          tokenNode.setTranslateY(y);
        });
  }


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

  @Override
  public void onEvent(BoardGameEvent event) {
    Platform.runLater(() -> {
      var pl = event.player();
      //int rolled = boardGameController.getLastRolledValue();
      //eventLog.appendText(String.format("%s rolled a %d%n", pl.getName(), rolled));

      int oldId = event.oldTile() != null ? event.oldTile().getTileId() : 0;
      int newId = event.newTile() != null ? event.newTile().getTileId() : 0;

      switch (event.eventType()) {
        case PLAYER_MOVED -> {
          eventLog.appendText(
              String.format("%s moved from %d to %d%n", pl.getName(), oldId, newId)
          );
          repositionTokenOnTile();
        }
        case PLAYER_LADDER_ACTION -> {
          eventLog.appendText(
              String.format("%s climbed a ladder from %d to %d%n", pl.getName(), oldId, newId)
          );
          repositionTokenOnTile();
        }
        case PLAYER_FINISHED -> {
          eventLog.appendText(
              String.format("Player %s finished!%n", pl.getName())
          );
          repositionTokenOnTile();
        }
        case GAME_FINISHED -> {
          if (!isGameFinished) {
            isGameFinished = true;
            SceneManager.showPodiumGameScene();
          }
        }
        default -> repositionTokenOnTile();
      }
    });
  }

  public Scene getScene() {
    return scene;
  }

  public Pane getTokenLayer() {
    return tokenLayerPane;
  }

  private BorderPane createRootPane() {
    BorderPane rootBorderPane = new BorderPane();
    rootBorderPane.setStyle("-fx-background-color: #1A237E;");
    return rootBorderPane;
  }

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

  private TextArea createOutputArea() {
    eventLog.setEditable(false);
    eventLog.setWrapText(true);
    eventLog.setScrollTop(Double.MAX_VALUE);
    eventLog.setFont(Font.font("monospace", FontWeight.BOLD, 16));
    eventLog.setStyle("-fx-control-inner-background: black; -fx-text-fill: white;");
    eventLog.setPrefHeight(200);
    return eventLog;
  }

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

    ioContainer.getChildren().add(diceView.getDiceImageView());
    Button rollBtn = diceView.getRollDiceBtn();
    ioContainer.getChildren().add(rollBtn);
    rollBtn.setOnAction(event -> {
      //Sett den til false om du skal teste
      rollBtn.setDisable(true);
      Timeline timeline = diceView.createRollDiceAnimation(() -> {
        int result = diceView.rollResultProperty().get();
        boardGameController.handlePlayerTurn(result);
        rollBtn.setDisable(false);
      });
      timeline.play();
    });

    Label rollLbl = new Label();
    rollLbl.setFont(Font.font("monospace", FontWeight.BOLD, 16));
    rollLbl.textProperty().bind(
        diceView.rollResultProperty().asString("Roll result: %d")
    );
    ioContainer.getChildren().add(rollLbl);


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
    back.setOnAction(e -> {
      try {
        SceneManager.showPlayerSelectionScene();
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      }
    });
    ioContainer.getChildren().add(back);

    return ioContainer;
  }

  @SuppressWarnings("unused")
  private VBox createPerformanceMeter() {
    VBox container = new VBox();
    container.setFillWidth(true);

    NumberAxis xAxis = new NumberAxis();
    xAxis.setLabel("Turn");
    xAxis.setTickUnit(1);
    xAxis.setAnimated(true);
    xAxis.setMinorTickVisible(false);

    NumberAxis yAxis = new NumberAxis("Dice Number", 0, 7, 1);
    yAxis.setAnimated(true);
    yAxis.setMinorTickVisible(false);

    LineChart<Number, Number> chart = new LineChart<Number, Number>(xAxis, yAxis);
    chart.setTitle("Performance Meter");
    chart.setMaxWidth(300);
    chart.setData(dataSeries);
    chart.setMaxHeight(225);
    chart.setAnimated(true);

    container.getChildren().add(chart);
    return container;
  }
}
