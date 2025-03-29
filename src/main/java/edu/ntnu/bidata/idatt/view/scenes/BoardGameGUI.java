package edu.ntnu.bidata.idatt.view.scenes;

import static edu.ntnu.bidata.idatt.view.components.TileView.TILE_SIZE;

import edu.ntnu.bidata.idatt.entity.Player;
import edu.ntnu.bidata.idatt.entity.TokenType;
import edu.ntnu.bidata.idatt.model.Board;
import edu.ntnu.bidata.idatt.patterns.factory.BoardGameFactory;
import edu.ntnu.bidata.idatt.patterns.factory.PlayerFactory;
import edu.ntnu.bidata.idatt.patterns.observer.BoardGameEvent;
import edu.ntnu.bidata.idatt.patterns.observer.BoardGameObserver;
import edu.ntnu.bidata.idatt.service.BoardService;
import edu.ntnu.bidata.idatt.service.PlayerService;
import edu.ntnu.bidata.idatt.view.components.BoardView;
import edu.ntnu.bidata.idatt.view.components.Buttons;
import edu.ntnu.bidata.idatt.view.components.TileView;
import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
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

public class BoardGameGUI implements BoardGameObserver {
  private static final Logger logger = Logger.getLogger(BoardGameGUI.class.getName());
  private final Scene scene;
  private final Label statusLabel = new Label();
  PlayerService playerService = new PlayerService();
  BoardService boardService = new BoardService();

  public BoardGameGUI() throws IOException {
    BorderPane rootPane = new BorderPane();
    rootPane.setStyle("-fx-background-color: #600E50;");
    rootPane.setStyle("-fx-background-color: #1A237E;");

    rootPane.setLeft(createIOContainer());
    List<Board> boards = boardService.getBoards();

    //midlertidig ----
    Board board = BoardGameFactory.createClassicBoard();
    boards.add(board);
    boardService.setBoard(board);
    boardService.writeBoardToFile(boards, "data/games/laddersAndSnakes.json");
    //midlertidig ----

    GridPane boardPane = BoardView.createBoardGUI(board);
    rootPane.setCenter(boardPane);
    scene = new Scene(rootPane, 1000, 700);

    List<Player> players = PlayerFactory.createPlayersDummies();
    playerService.setPlayers(players);
    TileView tile = (TileView) scene.lookup("#tile" + 1);
    if (tile != null) {
      List<Node> playerTokens = new ArrayList<>();
      for(Player player : players) {
        playerTokens.add(player.getToken());
      }
      tile.getChildren().addAll(playerTokens);
      setTokenPositionOnTile(tile);
      movePlayer(players.get(2), 4, board);
    }
    logger.log(Level.INFO, "BoardGameGUI created");
  }
  public void movePlayer(Player player, int steps, Board board) {
    int nextTileId = player.getCurrentTileId() + steps;
    if (nextTileId > board.getTiles().size()) {
      nextTileId = board.getTiles().size();
    }

    TileView nextTileView = (TileView) scene.lookup("#tile" + nextTileId);
    if(nextTileView != null) {
      nextTileView.getChildren().add(player.getToken());
      setTokenPositionOnTile(nextTileView);
    }
  }
  private static final double[][] tokenOFFSETS = {
      {0.2, 0.2},
      {0.5, 0.5},
      {0.8, 0.8},
      {0.2, 0.8},
      {0.8, 0.2}
  };
  private void setTokenPositionOnTile(TileView tile) {
    //hent kunt tokenViews noder
    List<Node> tokens = tile.getChildren().stream()
        .filter(node -> node instanceof TokenView)
        .toList();
    for (int i = 0; i < tokens.size() && i < tokenOFFSETS.length; i++) {
      Node token = tokens.get(i);
      double x = (TILE_SIZE - tokenOFFSETS[i][0] * TILE_SIZE * 2) / 2;
      double y = (TILE_SIZE - tokenOFFSETS[i][1] * TILE_SIZE * 2) / 2;
      token.setTranslateX(x);
      token.setTranslateY(y);
    }
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
    container.getChildren().addAll(pressToRoll);

    ImageView imageViewDice = new ImageView(new Image(Objects.requireNonNull(
        getClass().getResourceAsStream("/edu/ntnu/bidata/idatt/dicePlaceholder.png"))));
    imageViewDice.setFitHeight(100);
    imageViewDice.setFitWidth(100);
    container.getChildren().add(imageViewDice);

    Label rollResult = new Label("Roll result:");
    rollResult.setFont(Font.font("monospace", FontWeight.BOLD, 16));
    container.getChildren().addAll(rollResult);

    ImageView imageViewResult = new ImageView(new Image(Objects.requireNonNull(
        getClass().getResourceAsStream("/edu/ntnu/bidata/idatt/rollResultPlaceholder.png"))));
    imageViewResult.setFitHeight(100);
    imageViewResult.setFitWidth(100);
    container.getChildren().add(imageViewResult);

    Label outputLabel = new Label("Output");
    outputLabel.setFont(Font.font("monospace", FontWeight.BOLD, 16));
    outputLabel.setTextFill(Color.BLACK);
    container.getChildren().add(outputLabel);

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

    container.getChildren().add(outputArea);

    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);
    container.getChildren().add(spacer);

    Button backBtn = Buttons.getBackBtn("Back");
    container.getChildren().add(backBtn);

    return container;
  }

  @Override
  public void onEvent(BoardGameEvent eventType) {
    switch (eventType.eventType()) {
      case PLAYER_MOVED -> {
        statusLabel.setText(
            eventType.player().getName() + " moved from "
                + eventType.oldTile().getTileId()
                + " to " + eventType.newTile().getTileId());
        break;
      }
      case GAME_FINISHED -> {
        statusLabel.setText(eventType.player().getName() + " won the game!");
        break;
      }
      default -> {
        statusLabel.setText("Unknown event type: " + eventType.eventType());
        break;
      }
    }
  }
}
