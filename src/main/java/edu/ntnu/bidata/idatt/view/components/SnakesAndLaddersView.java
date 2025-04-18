package edu.ntnu.bidata.idatt.view.components;

import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_WIDTH;
import static edu.ntnu.bidata.idatt.view.components.TileView.TILE_SIZE;

import edu.ntnu.bidata.idatt.controller.patterns.factory.BoardGameFactory;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.service.BoardService;
import java.util.List;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class SnakesAndLaddersView extends Application {

  private static final Logger logger = Logger.getLogger(SnakesAndLaddersView.class.getName());
  private Ladders ladders;
  private Snakes snakes;

  public static void main(String[] args) {
    launch(args);
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

    int tileSize = TILE_SIZE;
    int boardSize = board.getTiles().size();

    int startCol = 1;
    int startRow = 1;
    int endCol = 3;
    int endRow = 3;

    double startX = startCol * tileSize + tileSize;
    double startY = startRow * tileSize + tileSize;
    double endX = endCol * tileSize + tileSize;
    double endY = endRow * tileSize + tileSize;

    Line ladder = new Line(startX, startY, endX, endY);
    ladder.setStroke(Color.BROWN);
    ladder.setStrokeWidth(4);

    //ladders = new Ladders();
    //snakes = new Snakes();
    //ladders.drawLadder();
    //snakes.drawSnake();

    StackPane container = new StackPane();
    container.getChildren().addAll(borderPane, ladder);
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
    return container;
  }

}
