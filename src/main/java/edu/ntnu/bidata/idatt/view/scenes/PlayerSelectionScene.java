package edu.ntnu.bidata.idatt.view.scenes;

import static edu.ntnu.bidata.idatt.controller.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.controller.SceneManager.SCENE_WIDTH;

import edu.ntnu.bidata.idatt.controller.SceneManager;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Token;
import edu.ntnu.bidata.idatt.model.service.PlayerService;
import edu.ntnu.bidata.idatt.view.components.AvailablePlayerCard;
import edu.ntnu.bidata.idatt.view.components.Buttons;
import edu.ntnu.bidata.idatt.view.components.SelectedPlayerCard;
import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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
  private static Integer totalPlayerCount = null;
  private static ColorPicker playerColorPicker;
  private final PlayerService playerService = new PlayerService();
  private final Scene scene;
  private final int PANEL_WIDTH = 300;

  public PlayerSelectionScene() throws IOException {
    BorderPane rootPane = SceneManager.getRootPane();
    scene = new Scene(rootPane, SCENE_WIDTH, SCENE_HEIGHT, Color.LIGHTBLUE);
    scene.getStylesheets().add(
        Objects.requireNonNull(
                getClass().getResource("/edu/ntnu/bidata/idatt/styles/PlayerSelectionSceneStyles.css"))
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
    addScaleAnimation(playersCountLabel, 1.1, Duration.millis(300));

    logger.log(Level.INFO, "PlayerSelectionScene initialized");
  }

  public static void showTotalPlayerSelectionDialog() {
    List<Integer> choices = new ArrayList<>();
    IntStream.range(1, 6).forEach(choices::add);

    Integer defaultValue =
        (totalPlayerCount != null && choices.contains(totalPlayerCount)) ? totalPlayerCount :
            choices.getFirst();

    ChoiceDialog<Integer> dialog = new ChoiceDialog<>(defaultValue, choices);
    dialog.setTitle("Total player selection");
    dialog.setHeaderText("Select total players");
    dialog.setContentText("Choose your number:");

    Optional<Integer> result;
    do {
      result = dialog.showAndWait();
    } while (result.isEmpty());
    totalPlayerCount = result.get();

    while (selectedPlayers.size() > totalPlayerCount) {
      selectedPlayers.removeLast();
    }

    updatePlayersCountLabel();
  }

  public static int getTotalPlayerCount() {
    return totalPlayerCount == null ? 0 : totalPlayerCount;
  }

  public static Color getSelectedColor() {
    return playerColorPicker.getValue();
  }

  private static void updatePlayersCountLabel() {
    playersCountLabel.setText(
        "Players: " + selectedPlayers.size() + "/" + getTotalPlayerCount());
  }

  public static ObservableList<Player> getSelectedPlayers() {
    return selectedPlayers;
  }

  private VBox createPlayerInputPanel() {
    VBox inputPanel = createPanel("Add players manually");
    inputPanel.setMaxWidth(PANEL_WIDTH);

    Label nameLabel = new Label("Enter Name");
    nameLabel.getStyleClass().add("label-sublabel");
    TextField nameInput = new TextField();
    nameInput.getStyleClass().add("text-field-combobox");
    addScaleAnimation(nameInput, 1.02, Duration.millis(200));

    Label shapeLabel = new Label("Choose Shape");
    shapeLabel.getStyleClass().add("label-sublabel");
    ComboBox<String> shapeComboBox = new ComboBox<>();
    shapeComboBox.getItems().addAll("Circle", "Square", "Triangle");
    shapeComboBox.getStyleClass().add("text-field-combobox");
    addScaleAnimation(shapeComboBox, 1.02, Duration.millis(200));

    Label colorLabel = new Label("Choose Color");
    colorLabel.getStyleClass().add("label-sublabel");
    playerColorPicker = new ColorPicker();
    playerColorPicker.getStyleClass().add("colorpicker");
    addScaleAnimation(playerColorPicker, 1.02, Duration.millis(200));

    Button addPlayerBtn = Buttons.getEditBtn("Add Player");
    addPlayerBtn.setOnAction(e -> handleAddPlayer(nameInput, shapeComboBox));

    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);

    inputPanel.getChildren().addAll(
        nameLabel, nameInput,
        shapeLabel, shapeComboBox,
        colorLabel, playerColorPicker,
        spacer,
        addPlayerBtn
    );
    return inputPanel;
  }


  private VBox createPlayerTablePanel() {
    VBox tablePanel = createPanel("Players added to the game");
    tablePanel.setPrefWidth(PANEL_WIDTH + 50);

    VBox playersBox = new VBox(5);
    playersBox.setStyle("-fx-background-color: transparent;");
    playersBox.setFillWidth(true);

    Runnable refreshCards = () -> {
      playersBox.getChildren().setAll(
          selectedPlayers.stream()
              .map(p -> new SelectedPlayerCard(p, pl -> {
                selectedPlayers.remove(pl);
                updatePlayersCountLabel();
              }))
              .toList()
      );
    };
    refreshCards.run();
    selectedPlayers.addListener((ListChangeListener<Player>) c -> refreshCards.run());

    playersBox.setPrefHeight(5 * 40);

    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);

    Button editCountBtn = Buttons.getEditBtn("Edit total players");
    editCountBtn.setOnAction(e -> showTotalPlayerSelectionDialog());

    tablePanel.getChildren().addAll(playersBox, spacer, playersCountLabel, editCountBtn);
    return tablePanel;
  }

  private VBox createAvailablePlayersPanel() throws IOException {
    VBox availablePanel = createPanel("Existing players");

    ObservableList<Player> availablePlayers = FXCollections.observableArrayList(
        playerService.readPlayersFromFile(PlayerService.PLAYER_FILE_PATH)
    );

    VBox playersBox = new VBox(5);
    playersBox.setStyle("-fx-background-color: transparent;");
    playersBox.setFillWidth(true);

    availablePlayers.forEach(p -> playersBox.getChildren().add(
        new AvailablePlayerCard(p, chosen -> {
          if (isAtMaxPlayers()) {
            showAlert(Alert.AlertType.WARNING, "Maximum of players",
                "Exceeded maximum players: " + getTotalPlayerCount());
            return;
          }
          selectedPlayers.add(new Player(
              chosen.getName(),
              new TokenView(Token.token(chosen.getToken().getTokenColor(),
                  chosen.getToken().getTokenShape())))
          );
          updatePlayersCountLabel();
        })
    ));

    VBox.setVgrow(playersBox, Priority.ALWAYS);
    availablePanel.getChildren().add(playersBox);
    return availablePanel;
  }

  private ListView<Player> createAvailablePlayersList(ObservableList<Player> players) {
    ListView<Player> listView = createPlayerListView(players);
    listView.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, selected) -> {
      if (selected != null) {
        if (isAtMaxPlayers()) {
          showAlert(Alert.AlertType.WARNING, "Maximum of players",
              "Exceeded maximum players: " + getTotalPlayerCount());
        } else {
          selectedPlayers.add(new Player(
              selected.getName(),
              new TokenView(Token.token(selected.getToken().getTokenColor(),
                  selected.getToken().getTokenShape()))));
          updatePlayersCountLabel();
        }
      }
    });
    return listView;
  }

  private ListView<Player> createPlayerListView(ObservableList<Player> players) {
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

    if (isAtMaxPlayers()) {
      showAlert(Alert.AlertType.WARNING, "Maximum of players",
          "Exceeded maximum players: " + getTotalPlayerCount());
      resetInputs(nameField, shapeComboBox);
    } else if (name == null || name.isBlank() || shape == null || color == null) {
      showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill out all fields.");
    } else {
      TokenView token = new TokenView(Token.token(color, shape));
      Player newPlayer = new Player(name, token);
      selectedPlayers.add(newPlayer);
      updatePlayersCountLabel();
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
      if (totalPlayerCount == null) {
        showTotalPlayerSelectionDialog();
      } else if (selectedPlayers.size() < getTotalPlayerCount()) {
        showAlert(Alert.AlertType.WARNING, "Not enough players",
            "You need " + getTotalPlayerCount() + " players. You have " + selectedPlayers.size() +
                " players");
      } else {
        try {
          SceneManager.showBoardGameScene();
        } catch (IOException ex) {
          throw new RuntimeException(ex);
        }
      }
    });

    Button mainPageBtn = Buttons.getExitBtn("To Main Page");
    mainPageBtn.setOnAction(e -> SceneManager.showLandingScene());

    container.getChildren().addAll(backBtn, spacerLeft, startGameBtn, spacerRight, mainPageBtn);
    return container;
  }

  public Scene getScene() {
    return scene;
  }

  private VBox createPanel(String headingText) {
    VBox panel = new VBox(10);
    panel.setPadding(new Insets(10));
    panel.setAlignment(Pos.TOP_CENTER);
    panel.getStyleClass().add("vbox-panel");
    addPanelHoverAnimation(panel);

    Label heading = createHeadingLabel(headingText);
    panel.getChildren().add(heading);
    return panel;
  }

  private Label createHeadingLabel(String text) {
    Label heading = new Label(text);
    heading.setWrapText(true);
    heading.getStyleClass().add("label-heading");
    addHeadingAnimation(heading);
    return heading;
  }

  private void addHeadingAnimation(Label label) {
    label.setOnMouseEntered(e -> {
      Timeline anim = new Timeline(
          new KeyFrame(Duration.millis(300),
              new KeyValue(label.translateXProperty(), 5),
              new KeyValue(label.opacityProperty(), 0.9)
          )
      );
      anim.play();
    });
    label.setOnMouseExited(e -> {
      Timeline anim = new Timeline(
          new KeyFrame(Duration.millis(300),
              new KeyValue(label.translateXProperty(), 0),
              new KeyValue(label.opacityProperty(), 1.0)
          )
      );
      anim.play();
    });
  }

  private void addPanelHoverAnimation(VBox panel) {
    panel.setOnMouseEntered(e -> {
      Timeline enterAnim = new Timeline(
          new KeyFrame(Duration.millis(300),
              new KeyValue(panel.scaleXProperty(), 1.02),
              new KeyValue(panel.scaleYProperty(), 1.02),
              new KeyValue(panel.opacityProperty(), 0.90)
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

  private void addScaleAnimation(javafx.scene.Node node, double targetScale,
      Duration duration) {
    node.setOnMouseEntered(e -> {
      Timeline anim = new Timeline(
          new KeyFrame(duration,
              new KeyValue(node.scaleXProperty(), targetScale),
              new KeyValue(node.scaleYProperty(), targetScale)
          )
      );
      anim.play();
    });
    node.setOnMouseExited(e -> {
      Timeline anim = new Timeline(
          new KeyFrame(duration,
              new KeyValue(node.scaleXProperty(), 1),
              new KeyValue(node.scaleYProperty(), 1)
          )
      );
      anim.play();
    });
  }

  private boolean isAtMaxPlayers() {
    return selectedPlayers.size() >= getTotalPlayerCount();
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
}
