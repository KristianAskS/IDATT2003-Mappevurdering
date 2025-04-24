package edu.ntnu.bidata.idatt.view.scenes;

import static edu.ntnu.bidata.idatt.controller.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.controller.SceneManager.SCENE_WIDTH;
import static edu.ntnu.bidata.idatt.model.service.BoardService.BOARD_FILE_PATH;
import static edu.ntnu.bidata.idatt.view.components.TileView.TILE_SIZE;

import edu.ntnu.bidata.idatt.controller.BoardGameController;
import edu.ntnu.bidata.idatt.controller.SceneManager;
import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEvent;
import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEventType;
import edu.ntnu.bidata.idatt.controller.patterns.observer.interfaces.BoardGameObserver;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Tile;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
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

/**
 * Boardgame UI controller. reacts to state changes
 */
public class BoardGameScene implements BoardGameObserver {
  private static final Logger logger = Logger.getLogger(BoardGameScene.class.getName());
  private final Scene scene;
  private final Board board;
  private final TextArea eventLog = new TextArea("Game started! \n");
  private final PlayerService playerService = new PlayerService();
  private final BoardGameController boardGameController;
  private final DiceView diceView;
  private final ObservableList<XYChart.Series<Number, Number>> dataSeries =
      FXCollections.observableArrayList();
  List<Player> players = PlayerSelectionScene.getSelectedPlayers();
  private int roundCounter = 0;

  public BoardGameScene() throws IOException {
    diceView = new DiceView();
    BorderPane rootPane = createRootPane();
    rootPane.setLeft(createIOContainer());

    BoardService boardService = new BoardService();
    List<Board> boards = boardService.getBoards();
    int numbOfDice = 1; // TODO: Make a static
    this.board = BoardGameSelectionScene.getSelectedBoard();
    boards.add(board);
    boardService.setBoard(board);
    boardService.writeBoardToFile(boards, BOARD_FILE_PATH);

    playerService.setPlayers(players);
    boardGameController =
        new BoardGameController(this, boardService, playerService, board, numbOfDice);

    GridPane boardPane = BoardView.createBoardGUI(board);
    Pane ladderOverlay = new Pane();
    ladderOverlay.prefWidthProperty().bind(boardPane.widthProperty());
    ladderOverlay.prefHeightProperty().bind(boardPane.heightProperty());

    Platform.runLater(() -> {
      LadderView.generateLadder(board, boardPane, ladderOverlay);
      for (Integer tileId : LadderView.getTileIdsWithLadders()) {
        TileView tileView = (TileView) boardPane.lookup("#tile" + tileId);
        if (tileView != null) {
          Tile tileWithLadder = board.getTile(tileId);
          if (tileWithLadder.getLandAction() != null) {
            tileView.setStyle("-fx-background-color: #A5D6A7;");
            tileView.addTileActionViewLbl("start", Color.RED);
          } else {
            tileView.setStyle("-fx-background-color: #EF9A9A");
            tileView.addTileActionViewLbl("end", Color.RED);
          }
        }
      }
    });

    StackPane boardWithOverlay = new StackPane(boardPane, ladderOverlay);
    rootPane.setCenter(boardWithOverlay);

    StackPane container = new StackPane(rootPane);
    scene = new Scene(container, SCENE_WIDTH, SCENE_HEIGHT, Color.PINK);

    initializePlayers();
    logger.log(Level.INFO, "BoardGameGUI created");
  }

  private static double[][] getTokenTilePosition(int tokenCount) {
    return switch (tokenCount) {
      case 2 -> new double[][] {{0.2, 0.5}, {0.8, 0.5}};
      case 3 -> new double[][] {{0.2, 0.2}, {0.5, 0.5}, {0.8, 0.8}};
      case 4 -> new double[][] {{0.2, 0.2}, {0.8, 0.2}, {0.2, 0.8}, {0.8, 0.8}};
      case 5 -> new double[][] {{0.2, 0.2}, {0.8, 0.2}, {0.2, 0.8}, {0.8, 0.8}, {0.5, 0.5}};
      default -> new double[][] {{0.5, 0.5}};
    };
  }

  private BorderPane createRootPane() {
    BorderPane root = new BorderPane();
    root.setStyle("-fx-background-color: #1A237E;");
    return root;
  }

  private void initializePlayers() {
    playerService.setPlayers(players);
    TileView startTile = (TileView) scene.lookup("#tile1");
    if (startTile != null) {
      List<Node> playerTokens = new ArrayList<>();
      players.stream().filter(player -> player.getToken() != null)
          .forEach(player -> playerTokens.add(player.getToken()));
      startTile.getChildren().addAll(playerTokens);
      setTokenPositionOnTile(startTile);
    }
  }

  public void setTokenPositionOnTile(TileView tile) {
    List<Node> tokens =
        tile.getChildren().stream().filter(node -> node instanceof TokenView).toList();
    double[][] tokenTilePosition = getTokenTilePosition(tokens.size());
    IntStream.range(0, tokens.size()).forEach(i -> {
      Node token = tokens.get(i);
      double x = (TILE_SIZE - tokenTilePosition[i][0] * TILE_SIZE * 2) / 2;
      double y = (TILE_SIZE - tokenTilePosition[i][1] * TILE_SIZE * 2) / 2;
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
    container.setStyle("-fx-background-color: #7DBED7;" + "-fx-background-radius: 0 40 40 0;" +
        "-fx-border-radius: 0 40 40 0;" + "-fx-border-color: black;" + "-fx-border-width: 1;");

    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(10.0);
    dropShadow.setOffsetX(10.0);
    dropShadow.setOffsetY(10.0);
    dropShadow.setColor(Color.color(0, 0, 0, 0.3));
    container.setEffect(dropShadow);

    container.getChildren().add(diceView.getDiceImageView());

    Button rollDiceBtn = diceView.getRollDiceBtn();
    container.getChildren().add(rollDiceBtn);
    rollDiceBtn.setOnAction(e -> {
      Timeline timeline = diceView.createRollDiceAnimation(() -> {
        boardGameController.handlePlayerTurn();
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
    eventLog.setEditable(false);
    eventLog.setWrapText(true);
    eventLog.setScrollTop(Double.MAX_VALUE);
    eventLog.setFont(Font.font("monospace", FontWeight.BOLD, 16));
    eventLog.setStyle("-fx-control-inner-background: black; -fx-text-fill: white;");
    eventLog.setPrefHeight(200);
    return eventLog;
  }

  @Override
  public void onEvent(BoardGameEvent eventType) {
    Platform.runLater(() -> {
      if (eventType.newTile().getLandAction() != null) {
        eventLog.appendText(
            "Tile action: " + eventType.newTile().getLandAction().getDescription() + "\n");
      }

      if (eventType.eventType() == BoardGameEventType.PLAYER_MOVED) {
        TokenView tokenView = eventType.player().getToken();

        TileView oldTileView = (TileView) scene.lookup("#tile" + eventType.oldTile().getTileId());
        TileView newTileView = (TileView) scene.lookup("#tile" + eventType.newTile().getTileId());

        if (oldTileView != null && newTileView != null && tokenView != null) {
          oldTileView.getChildren().remove(tokenView);
          newTileView.getChildren().addAll(tokenView);
          setTokenPositionOnTile(newTileView);
        }

        String moveText =
            eventType.player().getName() + " moved from " + eventType.oldTile().getTileId() +
                " to " + eventType.newTile().getTileId() + "\n";
        eventLog.appendText(moveText + "\n");
        roundCounter++;
        eventLog.appendText("Round number: " + roundCounter + "\n");
      } else if (eventType.eventType() == BoardGameEventType.GAME_FINISHED) {
        eventLog.appendText(eventType.player().getName() + " won the game!\n");
      } else {
        eventLog.setText("Unknown event type: " + eventType.eventType() + "\n");
      }
    });
  }
}
