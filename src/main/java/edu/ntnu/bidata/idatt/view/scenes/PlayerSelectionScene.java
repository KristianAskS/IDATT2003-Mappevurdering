package edu.ntnu.bidata.idatt.view.scenes;

import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_WIDTH;

import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.service.PlayerService;
import edu.ntnu.bidata.idatt.view.SceneManager;
import edu.ntnu.bidata.idatt.view.components.Buttons;
import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PlayerSelectionScene {
  private static final Logger logger = Logger.getLogger(PlayerSelectionScene.class.getName());
  private static Optional<Integer> placerSelectionResult;
  private static ColorPicker colorPicker;
  private final Scene scene;
  private final PlayerService playerService = new PlayerService();
  private final int VBOX_WIDTH = 300;

  public PlayerSelectionScene() throws IOException {
    BorderPane rootPane = SceneManager.getRootPane();
    scene = new Scene(rootPane, SCENE_WIDTH, SCENE_HEIGHT, Color.LIGHTBLUE);
    HBox centerBox = new HBox(20);
    centerBox.setPadding(new Insets(10));
    centerBox.setAlignment(Pos.CENTER);

    centerBox.getChildren().addAll(createLeftPanel(), createMiddlePanel(), createRightPanel());
    rootPane.setCenter(centerBox);

    HBox bottomContainer = createBottomContainer();
    rootPane.setBottom(bottomContainer);
    BorderPane.setAlignment(bottomContainer, Pos.BOTTOM_LEFT);

    logger.log(Level.INFO, "PlayerSelectionScene initialized");
  }

  public static void showTotalPlayerSelectionDialog() {
    List<Integer> choices = new ArrayList<>();
    IntStream.range(1, 6).forEach(choices::add);

    ChoiceDialog<Integer> dialog = new ChoiceDialog<>(5, choices);
    dialog.setTitle("Total player selection");
    dialog.setHeaderText("Select total players");
    dialog.setContentText("Choose your number:");

    placerSelectionResult = dialog.showAndWait();
    placerSelectionResult.ifPresent(
        letter -> logger.log(Level.INFO, "The user's choice: " + placerSelectionResult));
  }

  public static Optional<Integer> getPlacerSelectionResult() {
    return placerSelectionResult;
  }

  public static Color getColorPicker() {
    return colorPicker.getValue();
  }

  private static ListView<Player> getPlayerListView(ObservableList<Player> players) {
    ListView<Player> playerListView = new ListView<>(players);
    playerListView.setCellFactory(listView -> new ListCell<>() {
      protected void updateItem(Player player, boolean empty) {
        super.updateItem(player, empty);
        if (empty || player == null) {
          setText(null);
          setGraphic(null);
        } else {
          HBox cellLayout = new HBox(10);
          cellLayout.setAlignment(Pos.CENTER_LEFT);

          Label nameLabel = new Label(player.getName());
          nameLabel.setPrefWidth(100);

          Rectangle colorBox = new Rectangle(15, 15, player.getToken().getTokenColor());
          colorBox.setStroke(Color.BLACK);

          String shapeText = capitalizeFirstLetter(player.getToken().getTokenShape());
          Label shapeLabel = new Label(shapeText);

          cellLayout.getChildren().addAll(nameLabel, colorBox, shapeLabel);
          setGraphic(cellLayout);
        }
      }
    });
    return playerListView;
  }

  private static String capitalizeFirstLetter(String input) {
    return input == null || input.isBlank() ? input :
        input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
  }

  private String getVBoxPanelStyles() {
    return ("-fx-background-color: #C4ADAD ; -fx-border-width: 1;-fx-border-radius: 5; -fx-background-radius: 5; -fx-effect: dropshadow(gaussian, gray, 0,5, 0.1, 0, 2);");
  }

  private VBox createLeftPanel() {
    VBox leftPanel = new VBox(10);
    leftPanel.setPadding(new Insets(10));
    leftPanel.setMaxWidth(VBOX_WIDTH);
    leftPanel.setAlignment(Pos.TOP_CENTER);
    leftPanel.setStyle(getVBoxPanelStyles());

    Label headingLabel = new Label("Add players manually");
    headingLabel.setFont(Font.font("monospace", FontWeight.BOLD, 18));
    headingLabel.setStyle("-fx-text-overrun: ellipsis");
    headingLabel.setTextFill(Color.FIREBRICK);

    Label enterName = new Label("Enter Name");
    TextField inputName = new TextField();
    Button confirmNameBtn = Buttons.getEditBtn("Enter");

    Label shapeLabel = new Label("Chose shape");
    ComboBox<String> shapes = new ComboBox<>();
    shapes.getItems().addAll("Circle", "Square", "triangle");

    Label colorLabel = new Label("Choose Color");
    colorPicker = new ColorPicker();

    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);

    leftPanel.getChildren()
        .addAll(headingLabel, enterName, inputName, confirmNameBtn, shapeLabel, shapes, colorLabel,
            colorPicker,
            spacer, getAddPlayerBtn(inputName, shapes, colorPicker));
    return leftPanel;
  }

  private VBox createMiddlePanel() {
    VBox middlePanel = new VBox(10);
    middlePanel.setPadding(new Insets(10));
    middlePanel.setAlignment(Pos.TOP_CENTER);
    middlePanel.setPrefWidth(VBOX_WIDTH);
    middlePanel.setStyle(getVBoxPanelStyles());

    Label headingLabel = new Label("Players added to the game");
    headingLabel.setFont(Font.font("monospace", FontWeight.BOLD, 18));
    headingLabel.setTextFill(Color.FIREBRICK);

    TableView<Player> playersTableView = new TableView<>();
    TableColumn<Player, String> nameColumn = new TableColumn<>("Name");
    TableColumn<Player, String> tokenColumn = new TableColumn<>("Token");
    playersTableView.getColumns().addAll(List.of(nameColumn, tokenColumn));

    Button totalPlayerchoice = Buttons.getEditBtn("Edit total players");
    totalPlayerchoice.setOnAction(e -> {
      showTotalPlayerSelectionDialog();
    });

    middlePanel.getChildren().addAll(headingLabel, playersTableView, totalPlayerchoice);
    return middlePanel;
  }

  private VBox createRightPanel() throws IOException {
    VBox rightPanel = new VBox(10);
    rightPanel.setPadding(new Insets(10));
    rightPanel.setPrefWidth(VBOX_WIDTH);
    rightPanel.setAlignment(Pos.TOP_CENTER);
    rightPanel.setStyle(getVBoxPanelStyles());

    Label playersLabel = new Label("Players");
    playersLabel.setFont(Font.font("monospace", FontWeight.BOLD, 18));
    playersLabel.setTextFill(Color.FIREBRICK);

    ObservableList<Player> players = FXCollections.observableArrayList(
        playerService.readPlayersFromFile(PlayerService.PLAYER_FILE_PATH));
    final ListView<Player> playerListView = getPlayerListView(players);

    rightPanel.getChildren().addAll(playersLabel, playerListView);
    return rightPanel;
  }

  private HBox createBottomContainer() {
    HBox bottomBtns = new HBox();
    bottomBtns.setAlignment(Pos.CENTER);

    Region leftSpacer = new Region();
    Region rightSpacer = new Region();
    HBox.setHgrow(leftSpacer, Priority.ALWAYS);
    HBox.setHgrow(rightSpacer, Priority.ALWAYS);

    Button backBtn = getBackBtn();
    Button startGameBtn = getStartGameBtn();
    Button toMainBtn = getLandingPageBtn();
    HBox.setMargin(backBtn, new Insets(10));
    HBox.setMargin(startGameBtn, new Insets(10));
    HBox.setMargin(toMainBtn, new Insets(10));

    bottomBtns.getChildren().addAll(backBtn, leftSpacer, startGameBtn, rightSpacer, toMainBtn);
    bottomBtns.setAlignment(Pos.CENTER);

    return bottomBtns;
  }

  private Button getStartGameBtn() {
    Button startGameBtn = Buttons.getSmallPrimaryBtn("Start Game!");
    startGameBtn.setOnAction(p -> {
      if (getPlacerSelectionResult().isEmpty()) {
        showTotalPlayerSelectionDialog();
        return;
      }
      SceneManager.showBoardGameScene();
      logger.log(Level.INFO, "Start game btn pressed, showBoardGameScene initialized");
    });
    return startGameBtn;
  }

  private Button getBackBtn() {
    Button backBtn = Buttons.getBackBtn("Back");
    backBtn.setOnAction(p -> {
      SceneManager.showBoardGameSelectionScene();
      logger.log(Level.INFO, "Back btn pressed, showBoardGameSelectionScene initialized");
    });
    return backBtn;
  }

  private Button getLandingPageBtn() {
    Button mainPageBtn = Buttons.getExitBtn("To Main Page");
    mainPageBtn.setOnAction(p -> {
      SceneManager.showLandingScene();
      logger.log(Level.INFO, "main page btn pressed, showLandingScene initialized");
    });
    return mainPageBtn;
  }

  private Button getAddPlayerBtn(TextField nameInput, ComboBox<String> shapeComboBox,
                                 ColorPicker colorPicker) {
    Button addPlayerBtn = Buttons.getEditBtn("Add Player");
    addPlayerBtn.setOnAction(p -> {
      String name = nameInput.getText();
      String shape = shapeComboBox.getValue();
      Color color = colorPicker.getValue();
      if (name == null || name.isBlank() || shape == null || color == null) {
        throw new IllegalArgumentException("Please fill out all fields.");
      }
      TokenView tokenView = new TokenView(color, shape);
      Player player = new Player(name, tokenView);
      player.setTokenView(tokenView);

      logger.log(Level.INFO, "Added player: " + name + " with color and shape");
    });
    return addPlayerBtn;
  }

  public Scene getScene() {
    return scene;
  }
}
