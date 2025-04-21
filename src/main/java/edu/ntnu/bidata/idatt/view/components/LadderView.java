package edu.ntnu.bidata.idatt.view.components;

import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_WIDTH;

import edu.ntnu.bidata.idatt.controller.patterns.factory.BoardGameFactory;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.model.service.BoardService;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LadderView extends Application {
  private static final Logger logger = Logger.getLogger(LadderView.class.getName());
  static ArrayList<Integer> tileIdsWithLadders = new ArrayList<>();


  public static void main(String[] args) {
    launch(args);
  }

  public static ArrayList<Integer> getTileIdsWithLadders() {
    return tileIdsWithLadders;
  }

  public static int[] tileToGridPosition(Tile tile, Board board) {
    int totalTiles = board.getTiles().size();
    int tileId = tile.getTileId();
    int columns = 10;
    int rows = (int) Math.ceil(totalTiles / (double) columns);

    int row = (tileId - 1) / columns;
    int col = (tileId - 1) % columns;
    if (row % 2 == 1) {
      col = columns - col - 1;
    }

    row = rows - 1 - row;
    return new int[] {row, col};
  }

  @Override
  public void start(Stage primaryStage) {
    BoardService boardService = new BoardService();
    List<Board> boards = boardService.getBoards();
    Board board = BoardGameFactory.createClassicBoard();
    boards.add(board);
    boardService.setBoard(board);

    BorderPane borderPane = new BorderPane();
    GridPane boardGrid = BoardView.createBoardGUI(board);
    borderPane.setCenter(boardGrid);
    borderPane.setLeft(createIOContainer());

    Pane overlayPane = new Pane();
    overlayPane.prefWidthProperty().bind(boardGrid.widthProperty());
    overlayPane.prefHeightProperty().bind(boardGrid.heightProperty());

    Platform.runLater(() -> {
      int totalLadders = 5; // Starts from 0

      for (int ladders = 0; ladders <= totalLadders - 1; ladders++) {
        int startId = (int) (Math.random() * 88) + 1;
        int endId = startId + 1 + (int) (Math.random() * (88 - startId) + 1);

        boolean check = true;

        int firstDigitStart = Integer.parseInt(Integer.toString(startId).substring(0, 1));
        int firstDigitEnd = Integer.parseInt(Integer.toString(endId).substring(0, 1));

        for (Integer tileIdsWithLadder : tileIdsWithLadders) {

          if (startId == tileIdsWithLadder || endId == tileIdsWithLadder ||
              firstDigitStart == firstDigitEnd) {
            check = false;
            ladders--;
            break;
          }
        }
        if (check) {
          Tile start = board.getTile(startId);
          Tile end = board.getTile(endId);
          tileIdsWithLadders.add(startId);
          tileIdsWithLadders.add(endId);
          Ladder ladder = new Ladder(start, end, board, boardGrid);
          overlayPane.getChildren().addAll(ladder.getLadders());
        }
      }


    });

    StackPane boardWithOverlays = new StackPane(boardGrid, overlayPane);
    borderPane.setCenter(boardWithOverlays);

    StackPane container = new StackPane(borderPane);
    Scene scene = new Scene(container, SCENE_WIDTH, SCENE_HEIGHT, Color.LIGHTSKYBLUE);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  private VBox createIOContainer() {
    VBox container = new VBox();
    container.setPrefWidth(250);
    container.setPadding(new Insets(30));
    container.setSpacing(15);
    container.setAlignment(Pos.TOP_CENTER);
    container.setStyle(
        "-fx-background-color: #7DBED7;" +
            "-fx-background-radius: 0 40 40 0;" +
            "-fx-border-radius: 0 40 40 0;" +
            "-fx-border-color: black;" +
            "-fx-border-width: 1;"
    );

    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(10.0);
    dropShadow.setOffsetX(10.0);
    dropShadow.setOffsetY(10.0);
    dropShadow.setColor(Color.color(0, 0, 0, 0.3));
    container.setEffect(dropShadow);

    return container;
  }
}
