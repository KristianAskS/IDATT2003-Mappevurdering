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
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.util.Duration;

public class PlayerSelectionScene {
  private static final Logger logger = Logger.getLogger(PlayerSelectionScene.class.getName());

  private static final TableView<Player> playerTable = new TableView<>();
  private static final ObservableList<Player> selectedPlayers = FXCollections.observableArrayList();
  private static final Label playersCountLabel = new Label("Players: 0/0");
  private static Optional<Integer> totalPlayerCount = Optional.empty();
  private static ColorPicker playerColorPicker;
  private final PlayerService playerService = new PlayerService();
  private final Scene scene;
  private final int PANEL_WIDTH = 300;
  private final TableColumn<Player, String> nameColumn = new TableColumn<>("Name");
  private final TableColumn<Player, String> tokenColumn = new TableColumn<>("Token");

  public PlayerSelectionScene() throws IOException {
    BorderPane rootPane = SceneManager.getRootPane();
    scene = new Scene(rootPane, SCENE_WIDTH, SCENE_HEIGHT, Color.LIGHTBLUE);

    scene.getStylesheets().add(
        getClass().getResource("/edu/ntnu/bidata/idatt/styles/PlayerSelectionSceneStyles.css")
            .toExternalForm()
    );

    HBox centerLayout = new HBox(20);
    centerLayout.setPadding(new Insets(10));
    centerLayout.setAlignment(Pos.CENTER);

    VBox inputPanel = createPlayerInputPanel();
    VBox tablePanel = createPlayerTablePanel();
    VBox availablePanel = createAvailablePlayersPanel();

    centerLayout.getChildren().addAll(inputPanel, tablePanel, availablePanel);
    rootPane.setCenter(centerLayout);

    HBox bottomContainer = createBottomButtonContainer();
    BorderPane.setMargin(bottomContainer, new Insets(10));
    rootPane.setBottom(bottomContainer);

    playersCountLabel.getStyleClass().add("label-count");
    playersCountLabel.setOnMouseEntered(e -> {
      Timeline anim = new Timeline(
          new KeyFrame(Duration.millis(300),
              new KeyValue(playersCountLabel.scaleXProperty(), 1.1),
              new KeyValue(playersCountLabel.scaleYProperty(), 1.1)
          )
      );
      anim.play();
    });
    playersCountLabel.setOnMouseExited(e -> {
      Timeline anim = new Timeline(
          new KeyFrame(Duration.millis(300),
              new KeyValue(playersCountLabel.scaleXProperty(), 1.0),
              new KeyValue(playersCountLabel.scaleYProperty(), 1.0)
          )
      );
      anim.play();
    });

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
    playersCountLabel.setText(
        "Players: " + selectedPlayers.size() + "/" + totalPlayerCount.orElse(0));
  }

  public static int getTotalPlayerCount() {
    return totalPlayerCount.orElse(0);
  }

  public static Color getSelectedColor() {
    return playerColorPicker.getValue();
  }

  private void addPanelHoverAnimation(VBox panel) {
    panel.setOnMouseEntered(e -> {
      Timeline enterAnim = new Timeline(
          new KeyFrame(Duration.millis(300),
              new KeyValue(panel.scaleXProperty(), 1.05),
              new KeyValue(panel.scaleYProperty(), 1.05),
              new KeyValue(panel.opacityProperty(), 0.95)
          )
      );
      enterAnim.play();
    });
    panel.setOnMouseExited(e -> {
      Timeline exitAnim = new Timeline(
          new KeyFrame(Duration.millis(300),
              new KeyValue(panel.scaleXProperty(), 1.0),
              new KeyValue(panel.scaleYProperty(), 1.0),
              new KeyValue(panel.opacityProperty(), 1.0)
          )
      );
      exitAnim.play();
    });
  }

  private VBox createPlayerInputPanel() {
    VBox inputPanel = new VBox(10);
    inputPanel.setPadding(new Insets(10));
    inputPanel.setMaxWidth(PANEL_WIDTH);
    inputPanel.setAlignment(Pos.TOP_CENTER);
    inputPanel.getStyleClass().add("vbox-panel");

    addPanelHoverAnimation(inputPanel);

    Label heading = new Label("Add players manually");
    heading.setWrapText(true);
    heading.getStyleClass().add("label-heading");
    heading.setOnMouseEntered(e -> {
      Timeline anim = new Timeline(
          new KeyFrame(Duration.millis(300),
              new KeyValue(heading.translateXProperty(), 5),
              new KeyValue(heading.opacityProperty(), 0.9)
          )
      );
      anim.play();
    });
    heading.setOnMouseExited(e -> {
      Timeline anim = new Timeline(
          new KeyFrame(Duration.millis(300),
              new KeyValue(heading.translateXProperty(), 0),
              new KeyValue(heading.opacityProperty(), 1.0)
          )
      );
      anim.play();
    });

    Label nameLabel = new Label("Enter Name");
    nameLabel.getStyleClass().add("label-sublabel");
    TextField nameInput = new TextField();
    nameInput.getStyleClass().add("text-field-combobox");
    addHoverAnimation(nameInput);

    Label shapeLabel = new Label("Choose Shape");
    shapeLabel.getStyleClass().add("label-sublabel");
    ComboBox<String> shapeComboBox = new ComboBox<>();
    shapeComboBox.getItems().addAll("Circle", "Square", "Triangle");
    shapeComboBox.getStyleClass().add("text-field-combobox");
    addHoverAnimation(shapeComboBox);

    Label colorLabel = new Label("Choose Color");
    colorLabel.getStyleClass().add("label-sublabel");
    playerColorPicker = new ColorPicker();
    playerColorPicker.getStyleClass().add("colorpicker");
    addHoverAnimation(playerColorPicker);

    Button addPlayerBtn = Buttons.getEditBtn("Add Player");
    addPlayerBtn.setOnAction(e -> handleAddPlayer(nameInput, shapeComboBox));

    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);

    inputPanel.getChildren().addAll(
        heading,
        nameLabel, nameInput,
        shapeLabel, shapeComboBox,
        colorLabel, playerColorPicker,
        spacer,
        addPlayerBtn
    );

    return inputPanel;
  }

  private VBox createPlayerTablePanel() {
    VBox tablePanel = new VBox(10);
    tablePanel.setPadding(new Insets(10));
    tablePanel.setAlignment(Pos.TOP_CENTER);
    tablePanel.setPrefWidth(PANEL_WIDTH + 50);
    tablePanel.getStyleClass().add("vbox-panel");
    addPanelHoverAnimation(tablePanel);

    Label heading = new Label("Players added to the game");
    heading.setWrapText(true);
    heading.getStyleClass().add("label-heading");
    heading.setOnMouseEntered(e -> {
      Timeline anim = new Timeline(
          new KeyFrame(Duration.millis(300),
              new KeyValue(heading.translateXProperty(), 5),
              new KeyValue(heading.opacityProperty(), 0.9)
          )
      );
      anim.play();
    });
    heading.setOnMouseExited(e -> {
      Timeline anim = new Timeline(
          new KeyFrame(Duration.millis(300),
              new KeyValue(heading.translateXProperty(), 0),
              new KeyValue(heading.opacityProperty(), 1.0)
          )
      );
      anim.play();
    });

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
    playerTable.getColumns().setAll(nameColumn, tokenColumn);

    Button editCountBtn = Buttons.getEditBtn("Edit total players");
    editCountBtn.setOnAction(e -> showTotalPlayerSelectionDialog());

    tablePanel.getChildren().addAll(heading, playerTable, playersCountLabel, editCountBtn);
    return tablePanel;
  }

  private VBox createAvailablePlayersPanel() throws IOException {
    VBox rightPanel = new VBox(10);
    rightPanel.setPadding(new Insets(10));
    rightPanel.setPrefWidth(PANEL_WIDTH);
    rightPanel.setAlignment(Pos.TOP_CENTER);
    rightPanel.getStyleClass().add("vbox-panel");
    addPanelHoverAnimation(rightPanel);

    Label heading = new Label("Players");
    heading.getStyleClass().add("label-heading");
    heading.setOnMouseEntered(e -> {
      Timeline anim = new Timeline(
          new KeyFrame(Duration.millis(300),
              new KeyValue(heading.translateXProperty(), 5),
              new KeyValue(heading.opacityProperty(), 0.9)
          )
      );
      anim.play();
    });
    heading.setOnMouseExited(e -> {
      Timeline anim = new Timeline(
          new KeyFrame(Duration.millis(300),
              new KeyValue(heading.translateXProperty(), 0),
              new KeyValue(heading.opacityProperty(), 1.0)
          )
      );
      anim.play();
    });

    ObservableList<Player> availablePlayers = FXCollections.observableArrayList(
        playerService.readPlayersFromFile(PlayerService.PLAYER_FILE_PATH)
    );

    ListView<Player> playerListView = createAvailablePlayersList(availablePlayers);
    VBox.setVgrow(playerListView, Priority.ALWAYS);
    rightPanel.getChildren().addAll(heading, playerListView);
    return rightPanel;
  }

  private ListView<Player> createAvailablePlayersList(ObservableList<Player> players) {
    ListView<Player> listView = getPlayerListView(players);
    listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {
      if (selected != null) {
        if (selectedPlayers.size() >= getTotalPlayerCount()) {
          showAlert(Alert.AlertType.WARNING, "Maximum of players",
              "Exceeded maximum players: " + getTotalPlayerCount());
        } else {
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
              playersCountLabel.setText(
                  "Players: " + selectedPlayers.size() + "/" + totalPlayerCount.orElse(0));
            }
          });
        }
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
        name.setWrapText(true);
        name.getStyleClass().add("label-listview");
        name.setPrefWidth(70);

        Rectangle colorBox = new Rectangle(15, 15, player.getToken().getTokenColor());
        colorBox.setStroke(Color.BLACK);
        colorBox.setWidth(60);

        Label shape = new Label(capitalize(player.getToken().getTokenShape()));
        shape.getStyleClass().add("label-listview");

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

    if (selectedPlayers.size() >= getTotalPlayerCount()) {
      showAlert(Alert.AlertType.WARNING, "Maximum of players",
          "Exceeded maximum players: " + getTotalPlayerCount());
      resetInputs(nameField, shapeComboBox);
    } else if (name == null || name.isBlank() || shape == null || color == null) {
      showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill out all fields.");
    } else {
      TokenView token = new TokenView(color, shape);
      Player newPlayer = new Player(name, token);
      selectedPlayers.add(newPlayer);
      playersCountLabel.setText(
          "Players: " + selectedPlayers.size() + "/" + totalPlayerCount.orElse(0));
      resetInputs(nameField, shapeComboBox);
      logger.log(Level.INFO, "Added player: " + name + " with color and shape");
    }
  }

  private void resetInputs(TextField nameField, ComboBox<String> shapeComboBox) {
    nameField.clear();
    shapeComboBox.getSelectionModel().clearSelection();
    playerColorPicker.setValue(Color.WHITE);
  }

  private HBox createBottomButtonContainer() {
    HBox container = new HBox(20);
    container.setAlignment(Pos.CENTER);
    container.setPadding(new Insets(10, 20, 10, 20));

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

    container.getChildren().addAll(backBtn, spacerLeft, startGameBtn, spacerRight, mainPageBtn);
    return container;
  }

  public Scene getScene() {
    return scene;
  }

  private String capitalize(String input) {
    if (input == null || input.isBlank()) {
      return "";
    }
    return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
  }

  private void showAlert(Alert.AlertType type, String title, String message) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setContentText(message);
    alert.showAndWait();
  }

  private void addHoverAnimation(javafx.scene.Node node) {
    node.setOnMouseEntered(e -> {
      Timeline hoverAnim = new Timeline(
          new KeyFrame(Duration.millis(200),
              new KeyValue(node.scaleXProperty(), 1.02),
              new KeyValue(node.scaleYProperty(), 1.02)
          )
      );
      hoverAnim.play();
    });
    node.setOnMouseExited(e -> {
      Timeline exitAnim = new Timeline(
          new KeyFrame(Duration.millis(200),
              new KeyValue(node.scaleXProperty(), 1.0),
              new KeyValue(node.scaleYProperty(), 1.0)
          )
      );
      exitAnim.play();
    });
  }
}
