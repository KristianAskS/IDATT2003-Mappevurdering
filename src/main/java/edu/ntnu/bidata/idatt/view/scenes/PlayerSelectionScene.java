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
import java.util.Arrays;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
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
  private static final TableView<Player> playerTable = new TableView<>();
  private static final ObservableList<Player> selectedPlayers = FXCollections.observableArrayList();
  private static Optional<Integer> totalPlayerCount = Optional.empty();
  private static ColorPicker playerColorPicker;
  private final Scene scene;
  private final PlayerService playerService = new PlayerService();
  private final int PANEL_WIDTH = 300;
  private final TableColumn<Player, String> nameColumn = new TableColumn<>("Name");
  private final TableColumn<Player, String> tokenColumn = new TableColumn<>("Token");

  public PlayerSelectionScene() throws IOException {
    BorderPane rootPane = SceneManager.getRootPane();
    scene = new Scene(rootPane, SCENE_WIDTH, SCENE_HEIGHT, Color.LIGHTBLUE);

    HBox centerLayout = new HBox(20);
    centerLayout.setPadding(new Insets(10));
    centerLayout.setAlignment(Pos.CENTER);

    centerLayout.getChildren().addAll(
        createPlayerInputPanel(),
        createPlayerTablePanel(),
        createAvailablePlayersPanel()
    );
    rootPane.setCenter(centerLayout);
    rootPane.setBottom(createBottomButtonBar());

    logger.log(Level.INFO, "PlayerSelectionScene initialized");
  }

  public static void showTotalPlayerSelectionDialog() {
    List<Integer> choices = new ArrayList<>();
    IntStream.range(1, 6).forEach(choices::add);

    ChoiceDialog<Integer> dialog = new ChoiceDialog<>(5, choices);
    dialog.setTitle("Total player selection");
    dialog.setHeaderText("Select total players");
    dialog.setContentText("Choose your number:");

    totalPlayerCount = dialog.showAndWait();
  }

  public static Optional<Integer> getPlacerSelectionResult() {
    return totalPlayerCount;
  }

  public static Color getColorPicker() {
    return playerColorPicker.getValue();
  }

  private VBox createPlayerInputPanel() {
    VBox inputPanel = new VBox(10);
    inputPanel.setPadding(new Insets(10));
    inputPanel.setMaxWidth(PANEL_WIDTH);
    inputPanel.setAlignment(Pos.TOP_CENTER);
    inputPanel.setStyle(getPanelStyle());

    Label heading = new Label("Add players manually");
    heading.setFont(Font.font("monospace", FontWeight.BOLD, 18));
    heading.setTextFill(Color.FIREBRICK);

    TextField nameInput = new TextField();
    ComboBox<String> shapeComboBox = new ComboBox<>();
    shapeComboBox.getItems().addAll("Circle", "Square", "Triangle");

    playerColorPicker = new ColorPicker();

    Button addPlayerBtn = Buttons.getEditBtn("Add Player");
    addPlayerBtn.setOnAction(e -> handleAddPlayer(nameInput, shapeComboBox));

    inputPanel.getChildren().addAll(
        heading,
        new Label("Enter Name"), nameInput,
        new Label("Choose Shape"), shapeComboBox,
        new Label("Choose Color"), playerColorPicker,
        new Region(), addPlayerBtn
    );
    VBox.setVgrow(inputPanel.getChildren().get(6), Priority.ALWAYS);
    return inputPanel;
  }

  private VBox createPlayerTablePanel() {
    VBox tablePanel = new VBox(10);
    tablePanel.setPadding(new Insets(10));
    tablePanel.setAlignment(Pos.TOP_CENTER);
    tablePanel.setPrefWidth(PANEL_WIDTH);
    tablePanel.setStyle(getPanelStyle());

    Label heading = new Label("Players added to the game");
    heading.setFont(Font.font("monospace", FontWeight.BOLD, 18));
    heading.setTextFill(Color.FIREBRICK);

    nameColumn.setCellFactory(col -> new TableCell<>() {
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || getTableRow().getItem() == null) {
          setText(null);
          setGraphic(null);
          return;
        }
        Player player = getTableRow().getItem();
        setText(player.getName());
      }
    });
    tokenColumn.setCellFactory(col -> new TableCell<>() {
      @Override
      protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || getTableRow().getItem() == null) {
          setGraphic(null);
          setText(null);
          return;
        }
        Player player = getTableRow().getItem();
        TokenView token = player.getToken();
        if (token == null) {
          return;
        }

        Rectangle colorBox = new Rectangle(15, 15, token.getTokenColor());
        colorBox.setStroke(Color.BLACK);
        Label shapeLabel = new Label(capitalize(token.getTokenShape()));

        HBox layout = new HBox(10, colorBox, shapeLabel);
        layout.setAlignment(Pos.CENTER_LEFT);
        setGraphic(layout);
      }
    });

    playerTable.setItems(selectedPlayers);
    playerTable.getColumns().addAll(Arrays.asList(nameColumn, tokenColumn));

    Button editCountBtn = Buttons.getEditBtn("Edit total players");
    editCountBtn.setOnAction(e -> showTotalPlayerSelectionDialog());

    tablePanel.getChildren().addAll(heading, playerTable, editCountBtn);
    return tablePanel;
  }

  private VBox createAvailablePlayersPanel() throws IOException {
    VBox rightPanel = new VBox(10);
    rightPanel.setPadding(new Insets(10));
    rightPanel.setPrefWidth(PANEL_WIDTH);
    rightPanel.setAlignment(Pos.TOP_CENTER);
    rightPanel.setStyle(getPanelStyle());

    Label title = new Label("Players");
    title.setFont(Font.font("monospace", FontWeight.BOLD, 18));
    title.setTextFill(Color.FIREBRICK);

    ObservableList<Player> availablePlayers = FXCollections.observableArrayList(
        playerService.readPlayersFromFile(PlayerService.PLAYER_FILE_PATH));
    ListView<Player> playerListView = createAvailablePlayersList(availablePlayers);

    rightPanel.getChildren().addAll(title, playerListView);
    return rightPanel;
  }

  private ListView<Player> createAvailablePlayersList(ObservableList<Player> players) {
    final ListView<Player> listView =
        getPlayerListView(players);
    listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {
      if (selected != null) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Add Player");
        confirm.setHeaderText("Add " + selected.getName() + "?");
        confirm.setContentText("Do you want to add this player to the game?");

        confirm.showAndWait().ifPresent(response -> {
          if (response == ButtonType.OK) {
            selectedPlayers.add(new Player(
                selected.getName(),
                new TokenView(selected.getToken().getTokenColor(),
                    selected.getToken().getTokenShape())
            ));
          }
        });
      }
    });
    return listView;
  }

  private ListView<Player> getPlayerListView(ObservableList<Player> players) {
    ListView<Player> listView = new ListView<>(players);
    listView.setCellFactory(list -> new ListCell<>() {
      @Override
      protected void updateItem(Player player, boolean empty) {
        super.updateItem(player, empty);
        if (empty || player == null) {
          setGraphic(null);
          return;
        }
        Label name = new Label(player.getName());
        name.setPrefWidth(70);

        Rectangle colorBox = new Rectangle(15, 15, player.getToken().getTokenColor());
        colorBox.setStroke(Color.BLACK);
        colorBox.setWidth(70);

        Label shape = new Label(capitalize(player.getToken().getTokenShape()));

        HBox cell = new HBox(10, name, colorBox, shape);
        cell.setAlignment(Pos.CENTER_LEFT);
        setGraphic(cell);
      }
    });
    return listView;
  }

  private void handleAddPlayer(TextField nameField, ComboBox<String> shapeComboBox) {
    String name = nameField.getText();
    String shape = shapeComboBox.getValue();
    Color color = playerColorPicker.getValue();

    if (name == null || name.isBlank() || shape == null || color == null) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Input Error");
      alert.setContentText("Please fill out all fields.");
      alert.showAndWait();
      return;
    }

    TokenView token = new TokenView(color, shape);
    Player newPlayer = new Player(name, token);
    selectedPlayers.add(newPlayer);
    logger.log(Level.INFO, "Added player: " + name + " with color and shape");
  }

  private HBox createBottomButtonBar() {
    HBox bar = new HBox();
    bar.setAlignment(Pos.CENTER);

    Region spacerLeft = new Region();
    Region spacerRight = new Region();
    HBox.setHgrow(spacerLeft, Priority.ALWAYS);
    HBox.setHgrow(spacerRight, Priority.ALWAYS);

    Button backBtn = Buttons.getBackBtn("Back");
    backBtn.setOnAction(e -> SceneManager.showBoardGameSelectionScene());

    Button startGameBtn = Buttons.getSmallPrimaryBtn("Start Game!");
    startGameBtn.setOnAction(e -> {
      if (totalPlayerCount.isEmpty()) {
        showTotalPlayerSelectionDialog();
        return;
      }
      SceneManager.showBoardGameScene();
    });

    Button mainPageBtn = Buttons.getExitBtn("To Main Page");
    mainPageBtn.setOnAction(e -> SceneManager.showLandingScene());

    bar.getChildren().addAll(backBtn, spacerLeft, startGameBtn, spacerRight, mainPageBtn);
    return bar;
  }

  public Scene getScene() {
    return scene;
  }

  private String capitalize(String input) {
    return input == null || input.isBlank() ? "" :
        input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
  }

  private String getPanelStyle() {
    return "-fx-background-color: #C4ADAD; -fx-border-width: 1; -fx-border-radius: 5; " +
        "-fx-background-radius: 5; -fx-effect: dropshadow(gaussian, gray, 0.5, 0.1, 0, 2);";
  }
}
