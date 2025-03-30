package edu.ntnu.bidata.idatt.view.scenes;

import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_WIDTH;
import static edu.ntnu.bidata.idatt.view.components.TileView.TILE_SIZE;

import edu.ntnu.bidata.idatt.controller.BoardGameController;
import edu.ntnu.bidata.idatt.controller.patterns.factory.BoardGameFactory;
import edu.ntnu.bidata.idatt.controller.patterns.factory.PlayerFactory;
import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEvent;
import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameObserver;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.service.BoardService;
import edu.ntnu.bidata.idatt.model.service.PlayerService;
import edu.ntnu.bidata.idatt.view.SceneManager;
import edu.ntnu.bidata.idatt.view.components.BoardView;
import edu.ntnu.bidata.idatt.view.components.Buttons;
import edu.ntnu.bidata.idatt.view.components.TileView;
import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class BoardGameScene implements BoardGameObserver {
  private static final Logger logger = Logger.getLogger(BoardGameScene.class.getName());
  private final Scene scene;
  private final Label statusLabel = new Label();
  private final PlayerService playerService = new PlayerService();
  private final BoardGameController boardGameController;

  public BoardGameScene() throws IOException {
    BorderPane rootPane = createRootPane();
    rootPane.setLeft(createIOContainer());

    BoardService boardService = new BoardService();
    List<Board> boards = boardService.getBoards();
    //Hent et brett (brukeren velger ikke enda) og skriv det til fil (midlertidig løsning)
    Board board = BoardGameFactory.createClassicBoard();
    boards.add(board);
    boardService.setBoard(board);
    boardService.writeBoardToFile(boards, "data/games/laddersAndSnakes.json");

    boardGameController = new BoardGameController(this, boardService, playerService, board);

    GridPane boardPane = BoardView.createBoardGUI(board);
    rootPane.setCenter(boardPane);
    scene = new Scene(rootPane, SCENE_WIDTH, SCENE_HEIGHT, Color.PINK);

    //Initialiser spillere og plasser tokenene på starttilen (midlertidig løsning)
    initializePlayers(board);

    logger.log(Level.INFO, "BoardGameGUI created");
  }

  /**
   * Returns hardcoded offset-coordinates based on total tokens on a tile.
   *
   * @param tokenCount total number of tokens
   * @return 2D-array of offsets
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

  private BorderPane createRootPane() {
    BorderPane root = new BorderPane();
    root.setStyle("-fx-background-color: #1A237E;");
    return root;
  }

  private void initializePlayers(Board board) {
    List<Player> players = PlayerFactory.createPlayersDummies();
    playerService.setPlayers(players);

    //Legg alle spilleres token på starttile (midlertidig løsning). Spillerne skal egt starte utanfor brettet
    TileView startTile = (TileView) scene.lookup("#tile1");
    if (startTile != null) {
      List<Node> playerTokens = new ArrayList<>();
      players.stream()
          .filter(player -> player.getToken() != null)
          .forEach(player -> playerTokens.add(player.getToken()));

      startTile.getChildren().addAll(playerTokens);
      setTokenPositionOnTile(startTile);

      //Eksempel: Flytt spiller 3 med 4 steg
      boardGameController.movePlayer(players.get(2), 4);
    }
  }

  public void setTokenPositionOnTile(TileView tile) {
    //Filtrer ut kun noder fra tile
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

    Label pressToRoll = new Label("Press to Roll");
    pressToRoll.setFont(Font.font("monospace", FontWeight.BOLD, 16));
    pressToRoll.setWrapText(true);
    container.getChildren().add(pressToRoll);

    ImageView imageViewDice = new ImageView(new Image(Objects.requireNonNull(
        getClass().getResourceAsStream("/edu/ntnu/bidata/idatt/dicePlaceholder.png"))));
    imageViewDice.setFitHeight(100);
    imageViewDice.setFitWidth(100);
    container.getChildren().add(imageViewDice);

    Label rollResult = new Label("Roll result:");
    rollResult.setFont(Font.font("monospace", FontWeight.BOLD, 16));
    container.getChildren().add(rollResult);

    ImageView imageViewResult = new ImageView(new Image(Objects.requireNonNull(
        getClass().getResourceAsStream("/edu/ntnu/bidata/idatt/rollResultPlaceholder.png"))));
    imageViewResult.setFitHeight(100);
    imageViewResult.setFitWidth(100);
    container.getChildren().add(imageViewResult);

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
    //midlertidig løsning
    backBtn.setOnAction(e -> SceneManager.showPlayerSelectionScene());

    return container;
  }

  private VBox createOutputArea() {
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

    Label roundLabel = new Label("Round number 1");
    roundLabel.setFont(Font.font("monospace", FontWeight.BOLD, 16));
    roundLabel.setTextFill(Color.WHITE);
    outputArea.getChildren().add(roundLabel);

    Label example1 = new Label("Player1 on tile 12");
    example1.setFont(Font.font("monospace", FontWeight.BOLD, 12));
    example1.setTextFill(Color.WHITE);
    outputArea.getChildren().add(example1);

    Label example2 = new Label("Player2 moves to tile 18");
    example2.setFont(Font.font("monospace", FontWeight.BOLD, 12));
    example2.setTextFill(Color.WHITE);
    outputArea.getChildren().add(example2);

    return outputArea;
  }

  @Override
  public void onEvent(BoardGameEvent eventType) {
    switch (eventType.eventType()) {
      case PLAYER_MOVED -> statusLabel.setText(
          eventType.player().getName() + " moved from "
              + eventType.oldTile().getTileId()
              + " to " + eventType.newTile().getTileId());
      case GAME_FINISHED -> statusLabel.setText(eventType.player().getName() + " won the game!");
      default -> statusLabel.setText("Unknown event type: " + eventType.eventType());
    }
  }
}
