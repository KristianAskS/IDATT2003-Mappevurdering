package edu.ntnu.bidata.idatt.view.scenes;

import static edu.ntnu.bidata.idatt.controller.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.controller.SceneManager.SCENE_WIDTH;

import edu.ntnu.bidata.idatt.controller.SceneManager;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Token;
import edu.ntnu.bidata.idatt.model.service.PlayerService;
import edu.ntnu.bidata.idatt.view.components.AvailablePlayerCard;
import edu.ntnu.bidata.idatt.view.components.Buttons;
import edu.ntnu.bidata.idatt.view.components.SelectedPlayerCard;
import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
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
import javafx.scene.control.DatePicker;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.util.Duration;

public class PlayerSelectionScene {

  private static final Logger logger = Logger.getLogger(PlayerSelectionScene.class.getName());

  private static final TableView<Player> playerTable = new TableView<>();
  private static final ObservableList<Player> selectedPlayers = FXCollections.observableArrayList();
  private static final Label playersCountLabel = new Label("Players: 0/0");
  private static final String IMG_DIR = "data/games/tokenimages";
  private static Integer totalPlayerCount = null;
  private static ColorPicker playerColorPicker;
  private final String selectedGame = GameSelectionScene.getSelectedGame();
  private final Board selectedBoard = BoardSelectionScene.getSelectedBoard();
  private final PlayerService playerService = new PlayerService();
  private final Scene scene;
  private final int PANEL_WIDTH = 300;
  private File selectedImage;

  public PlayerSelectionScene() throws IOException {
    BorderPane rootPane = SceneManager.getRootPane();
    scene = new Scene(rootPane, SCENE_WIDTH, SCENE_HEIGHT, Color.LIGHTBLUE);
    scene.getStylesheets().add(
        Objects.requireNonNull(getClass().getResource(
            "/edu/ntnu/bidata/idatt/styles/PlayerSelectionSceneStyles.css")).toExternalForm());

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

    logger.log(Level.INFO, "PlayerSelectionScene initialised for game: {0}", selectedGame);
  }

  public static void showTotalPlayerSelectionDialog() {
    boolean isLudo = "LUDO".equalsIgnoreCase(GameSelectionScene.getSelectedGame());
    int minPlayers = isLudo ? 2 : 1;
    int maxPlayers;
    if (isLudo) {
      Board board = BoardSelectionScene.getSelectedBoard();
      maxPlayers = (board != null && board.getName().toLowerCase().contains("mega")) ? 8 : 4;
    } else {
      maxPlayers = 5;
    }

    List<Integer> choices = new ArrayList<>();
    IntStream.rangeClosed(minPlayers, maxPlayers).forEach(choices::add);

    Integer defaultValue = (totalPlayerCount != null && choices.contains(totalPlayerCount))
        ? totalPlayerCount : choices.getFirst();

    ChoiceDialog<Integer> dialog = new ChoiceDialog<>(defaultValue, choices);
    dialog.setTitle("Total player selection");
    dialog.setHeaderText("Select total players");
    dialog.setContentText("Choose number:");

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

  public static ObservableList<Player> getSelectedPlayers() {
    return selectedPlayers;
  }

  private static void updatePlayersCountLabel() {
    playersCountLabel.setText("Players: " + selectedPlayers.size() + "/" + getTotalPlayerCount());
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

    Label imgLabel = new Label("Choose Image");
    imgLabel.setWrapText(true);
    imgLabel.setTextAlignment(TextAlignment.CENTER);
    imgLabel.getStyleClass().add("label-sublabel");

    Label imgPath = new Label();

    Button imgBtn = Buttons.getEditBtn("Browse");
    Button imgResetBtn = Buttons.getEditBtn("Remove image");

    imgBtn.setMinWidth(Region.USE_PREF_SIZE);
    imgResetBtn.setMinWidth(Region.USE_PREF_SIZE);

    imgBtn.setOnAction(e -> handleBrowseImage(shapeComboBox, imgPath));
    imgResetBtn.setOnAction(e -> resetSelectedImage(shapeComboBox, imgPath));

    Button addPlayerBtn = Buttons.getEditBtn("Add Player");

    Label dobLabel = new Label("Birthday (optional");
    dobLabel.getStyleClass().add("label-sublabel");
    DatePicker dobPicker = new DatePicker();
    addScaleAnimation(dobPicker, 1.02, Duration.millis(200));

    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);

    addPlayerBtn.setOnAction(e -> handleAddPlayer(nameInput, shapeComboBox, dobPicker));

    inputPanel.getChildren().addAll(
        imgLabel, new HBox(10, imgBtn, imgResetBtn), imgPath,
        nameLabel, nameInput,
        shapeLabel, shapeComboBox,
        colorLabel, playerColorPicker,
        dobLabel, dobPicker,
        spacer,
        addPlayerBtn
    );
    return inputPanel;
  }

  private void handleBrowseImage(ComboBox<String> shapeComboBox, Label imgPath) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg"));
    File file = fileChooser.showOpenDialog(scene.getWindow());
    if (file != null) {
      selectedImage = file;
      imgPath.setText(file.getName());
      //shapeComboBox.getSelectionModel().select("Circle");
      shapeComboBox.setDisable(true);
    }
  }

  private VBox createPlayerTablePanel() {
    VBox tablePanel = createPanel("Players added to the game");
    tablePanel.setPrefWidth(PANEL_WIDTH + 50);

    VBox playersBox = new VBox(5);
    playersBox.setStyle("-fx-background-color: transparent;");
    playersBox.setFillWidth(true);

    Runnable refreshCards = () -> playersBox.getChildren().setAll(
        selectedPlayers.stream()
            .map(player -> new SelectedPlayerCard(player, pl -> {
              selectedPlayers.remove(pl);
              updatePlayersCountLabel();
            }))
            .toList()
    );
    refreshCards.run();
    selectedPlayers.addListener((ListChangeListener<Player>) c -> refreshCards.run());

    playersBox.setPrefHeight(5 * 40);

    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);

    Button editCountBtn = Buttons.getEditBtn("Edit total players");
    editCountBtn.setOnAction(event -> showTotalPlayerSelectionDialog());

    tablePanel.getChildren().addAll(playersBox, spacer, playersCountLabel, editCountBtn);
    return tablePanel;
  }

  private VBox createAvailablePlayersPanel() throws IOException {
    String heading =
        "LUDO".equalsIgnoreCase(selectedGame) ? "Saved Ludo players" : "Existing players";
    VBox availablePanel = createPanel(heading);

    ObservableList<Player> availablePlayers = FXCollections.observableArrayList(
        playerService.readPlayersFromFile(PlayerService.PLAYER_FILE_PATH)
    );

    VBox playersBox = new VBox(5);
    playersBox.setStyle("-fx-background-color: transparent;");
    playersBox.setFillWidth(true);

    availablePlayers.forEach(player -> playersBox.getChildren().add(
        new AvailablePlayerCard(player, chosenPlayer -> {
          if (isAtMaxPlayers()) {
            showAlert(Alert.AlertType.WARNING, "Maximum players reached",
                "Limit: " + getTotalPlayerCount());
            return;
          }
          selectedPlayers.add(new Player(chosenPlayer.getName(),
              new TokenView(Token.token(
                  chosenPlayer.getToken().getTokenColor(),
                  chosenPlayer.getToken().getTokenShape(),
                  chosenPlayer.getToken().getImagePath()))));
          updatePlayersCountLabel();
        })));

    VBox.setVgrow(playersBox, Priority.ALWAYS);
    availablePanel.getChildren().add(playersBox);
    return availablePanel;
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
    backBtn.setOnAction(event -> SceneManager.showBoardSelectionScene());

    Button startGameBtn = Buttons.getSmallPrimaryBtn(
        "Start " + ("LUDO".equalsIgnoreCase(selectedGame) ? "Ludo" : "Game"));
    startGameBtn.setOnAction(event -> {
      if (totalPlayerCount == null) {
        showTotalPlayerSelectionDialog();
      } else if (selectedPlayers.size() < getTotalPlayerCount()) {
        showAlert(Alert.AlertType.WARNING, "Not enough players",
            "You need " + getTotalPlayerCount() + " players. You have " + selectedPlayers.size());
      } else {
        try {
          SceneManager.showBoardGameScene();
        } catch (IOException exception) {
          throw new RuntimeException(exception);
        }
      }
    });

    Button mainPageBtn = Buttons.getExitBtn("To Main Page");
    mainPageBtn.setOnAction(event -> SceneManager.showLandingScene());

    container.getChildren().addAll(backBtn, spacerLeft, startGameBtn, spacerRight, mainPageBtn);
    return container;
  }

  private void resetSelectedImage(ComboBox<String> shapeComboBox, Label imgPathLabel) {
    selectedImage = null;
    imgPathLabel.setText("");
    if (!"LUDO".equalsIgnoreCase(selectedGame)) {
      shapeComboBox.setDisable(false);
      shapeComboBox.getSelectionModel().clearSelection();
    }
  }

  private void handleAddPlayer(TextField nameField, ComboBox<String> shapeComboBox,
      DatePicker dobPicker) {
    String name = nameField.getText();
    String shape = shapeComboBox.getValue();
    Color color = playerColorPicker.getValue();
    File selectedImage = this.selectedImage;

    if (isAtMaxPlayers()) {
      showAlert(Alert.AlertType.WARNING, "Maximum players reached",
          "Limit: " + getTotalPlayerCount());
      resetInputs(nameField, shapeComboBox);
      return;
    }

    if (shape == null || selectedImage != null) {
      shape = "circle";
    }

    if (name == null || name.isBlank() || color == null) {
      showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill out all fields.");
      return;
    }

    String storedImgPath = null;
    try {
      if (selectedImage != null) {
        Path targetDir = Paths.get(IMG_DIR);
        Files.createDirectories(targetDir);

        String uniqueName = "image-" + selectedImage.getName();
        Path destination = targetDir.resolve(uniqueName);

        Files.copy(selectedImage.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
        storedImgPath = destination.toUri().toString();
        logger.log(Level.INFO, "Token image saved to {0}", destination);
      }
    } catch (IOException exception) {
      logger.log(Level.SEVERE, "Failed to copy token image: {0}", exception.getMessage());
      showAlert(Alert.AlertType.ERROR, "Image error", "Could not save the chosen image.");
      return;
    }

    TokenView token = new TokenView(Token.token(color, shape, storedImgPath));

    LocalDate dob = dobPicker.getValue();
    if (dob == null) {
      dob = randomBirthDate();
    }

    Player newPlayer = new Player(name, token, dob);

    selectedPlayers.add(newPlayer);
    updatePlayersCountLabel();
    resetInputs(nameField, shapeComboBox);
    this.selectedImage = null;
    logger.log(Level.INFO, "Added player: {0} (img={1})", new Object[]{name, storedImgPath});
  }

  private LocalDate randomBirthDate() {
    long start = LocalDate.of(1980, 1, 1).toEpochDay();
    long end = LocalDate.of(2003, 1, 1).toEpochDay();
    long randomDay = ThreadLocalRandom.current().nextLong(start, end);
    return LocalDate.ofEpochDay(randomDay);
  }

  private void resetInputs(TextField nameField, ComboBox<String> shapeComboBox) {
    nameField.clear();
    if (!"LUDO".equalsIgnoreCase(selectedGame)) {
      shapeComboBox.getSelectionModel().clearSelection();
    }
    playerColorPicker.setValue(Color.WHITE);
  }

  private boolean isAtMaxPlayers() {
    return selectedPlayers.size() >= getTotalPlayerCount();
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
    label.setOnMouseEntered(e -> new Timeline(new KeyFrame(Duration.millis(300),
        new KeyValue(label.translateXProperty(), 5),
        new KeyValue(label.opacityProperty(), 0.9))).play());
    label.setOnMouseExited(e -> new Timeline(new KeyFrame(Duration.millis(300),
        new KeyValue(label.translateXProperty(), 0),
        new KeyValue(label.opacityProperty(), 1.0))).play());
  }

  private void addPanelHoverAnimation(VBox panel) {
    panel.setOnMouseEntered(e -> new Timeline(new KeyFrame(Duration.millis(300),
        new KeyValue(panel.scaleXProperty(), 1.02),
        new KeyValue(panel.scaleYProperty(), 1.02),
        new KeyValue(panel.opacityProperty(), 0.90))).play());
    panel.setOnMouseExited(e -> new Timeline(new KeyFrame(Duration.millis(300),
        new KeyValue(panel.scaleXProperty(), 1.0),
        new KeyValue(panel.scaleYProperty(), 1.0),
        new KeyValue(panel.opacityProperty(), 1.0))).play());
  }

  private void addScaleAnimation(javafx.scene.Node node, double targetScale, Duration duration) {
    node.setOnMouseEntered(e -> new Timeline(new KeyFrame(duration,
        new KeyValue(node.scaleXProperty(), targetScale),
        new KeyValue(node.scaleYProperty(), targetScale))).play());
    node.setOnMouseExited(e -> new Timeline(new KeyFrame(duration,
        new KeyValue(node.scaleXProperty(), 1),
        new KeyValue(node.scaleYProperty(), 1))).play());
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

  public Scene getScene() {
    return scene;
  }
}