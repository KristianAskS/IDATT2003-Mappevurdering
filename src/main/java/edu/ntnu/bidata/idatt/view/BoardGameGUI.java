package edu.ntnu.bidata.idatt.view;

import edu.ntnu.bidata.idatt.model.Board;
import edu.ntnu.bidata.idatt.patterns.factory.BoardGameFactory;
import edu.ntnu.bidata.idatt.patterns.factory.BoardGameGUIFactory;
import edu.ntnu.bidata.idatt.patterns.observer.BoardGameEvent;
import edu.ntnu.bidata.idatt.patterns.observer.BoardGameObserver;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
  private final Scene scene;
  private final Label statusLabel = new Label();

  public BoardGameGUI() {
    BorderPane rootPane = new BorderPane();
    rootPane.setStyle("-fx-background-color: #600E50;");
    rootPane.setLeft(createIOContainer());

    Board board = BoardGameFactory.createClassicBoard();
    GridPane boardPane = BoardGameGUIFactory.createBoardGUI(board);
    rootPane.setCenter(boardPane);
    scene = new Scene(rootPane, 1000, 700);
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

    Button leaderboardBtn = new Button("Show Leaderboard");
    leaderboardBtn.setStyle(
        "-fx-background-color: #7DBED7;"
            + "-fx-border-color: black;"
            + "-fx-border-width: 2;"
            + "-fx-border-radius: 10;"
            + "-fx-background-radius: 10;"
    );
    container.getChildren().add(leaderboardBtn);

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
