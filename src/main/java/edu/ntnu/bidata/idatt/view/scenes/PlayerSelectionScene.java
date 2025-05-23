package edu.ntnu.bidata.idatt.view.scenes;

import edu.ntnu.bidata.idatt.controller.SceneManager;
import edu.ntnu.bidata.idatt.model.entity.Board;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Token;
import edu.ntnu.bidata.idatt.model.service.PlayerService;
import edu.ntnu.bidata.idatt.utils.exceptions.GameUIException;
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
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
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
import javafx.scene.Node;
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
import javafx.scene.control.ScrollPane;
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

/**
 * Manages the player selection screen for the game.
 * Allows users to create new players, select existing players,
 * and define the total number of players for the upcoming game.
 * Extends {@link BaseScene} for common scene setup.
 */
public class PlayerSelectionScene extends BaseScene {

  private static final Logger logger = Logger.getLogger(PlayerSelectionScene.class.getName());
  private static final TableView<Player> playerTable = new TableView<>();
  private static final ObservableList<Player> selectedPlayers = FXCollections.observableArrayList();
  private static final Label playersCountLabel = new Label("Players: 0/0");
  private static final String IMG_DIR = "data/games/tokenimages";
  private static Integer totalPlayerCount = null;
  private final String selectedGame = GameSelectionScene.getSelectedGame();
  private final Board selectedBoard = BoardSelectionScene.getSelectedBoard();
  private final PlayerService playerService = new PlayerService();
  private final int PANEL_WIDTH = 300;
  TextField nameInput = new TextField();
  private File selectedImage;
  private ColorPicker playerColorPicker;

  /**
   * Displays a dialog to select the total number of players for the game.
   * The number of choices depends on the selected game type (Ludo or other)
   * and specific board configurations (e.g., Ludo Mega).
   */
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

  /**
   * Gets the total number of players selected for the game.
   *
   * @return The total player count, or 0 if not yet set.
   */
  public static int getTotalPlayerCount() {
    return totalPlayerCount == null ? 0 : totalPlayerCount;
  }

  /**
   * Gets the observable list of players currently selected for the game.
   *
   * @return An {@link ObservableList} of {@link Player} objects.
   */
  public static ObservableList<Player> getSelectedPlayers() {
    return selectedPlayers;
  }

  /**
   * Updates the label displaying the current number of selected players versus the total required.
   */
  private static void updatePlayersCountLabel() {
    playersCountLabel.setText("Players: " + selectedPlayers.size() + "/" + getTotalPlayerCount());
  }

  /**
   * Initializes the player selection scene UI.
   * Sets up panels for player input, selected players, and available players.
   *
   * @throws IOException If stylesheet resources cannot be loaded.
   */
  @Override
  protected void initialize() throws IOException {
    scene.setFill(Color.LIGHTBLUE);
    scene.getStylesheets().add(
        Objects.requireNonNull(getClass()
                .getResource("/edu/ntnu/bidata/idatt/styles/PlayerSelectionSceneStyles.css"))
            .toExternalForm()
    );

    BorderPane rootPane = this.root;
    rootPane.setPadding(Insets.EMPTY);

    HBox centerLayout = new HBox(20);
    centerLayout.setPadding(new Insets(10));
    centerLayout.setAlignment(Pos.CENTER);

    VBox inputPanel = createPlayerInputPanel();
    VBox tablePanel = createPlayerTablePanel();
    VBox availablePanel = createAvailablePlayersPanel();

    centerLayout.getChildren().addAll(inputPanel, tablePanel, availablePanel);
    rootPane.setCenter(centerLayout);

    HBox bottom = createBottomButtonContainer();
    BorderPane.setMargin(bottom, new Insets(10));
    rootPane.setBottom(bottom);

    playersCountLabel.getStyleClass().add("label-count");
    addScaleAnimation(playersCountLabel, 1.1, Duration.millis(300));
    logger.log(Level.INFO,
        "PlayerSelectionScene initialised for game: {0}",
        selectedGame);
  }

  /**
   * Creates the panel for manual player input (name, shape, color, image, DOB).
   *
   * @return A {@link VBox} containing player input fields and an add button.
   */
  private VBox createPlayerInputPanel() {
    VBox inputPanel = createPanel("Add players manually");
    inputPanel.setMaxWidth(PANEL_WIDTH);

    Label nameLabel = new Label("Enter Name");
    nameLabel.getStyleClass().add("label-sublabel");
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
    playerColorPicker = new ColorPicker();

    Label dobLabel = new Label("Birthday");
    dobLabel.getStyleClass().add("label-sublabel");
    DatePicker dobPicker = new DatePicker();
    dobPicker.getStyleClass().add("calendar-view");
    addScaleAnimation(dobPicker, 1.02, Duration.millis(200));

    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);

    addPlayerBtn.setOnAction(e -> {
      try {
        handleAddPlayer(nameInput, shapeComboBox, dobPicker);
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    });

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

  /**
   * Handles saving the currently selected players to persistent storage.
   */
  private void handleSavePlayers() {
    String name = nameInput.getText();
    if (selectedPlayers.isEmpty()) {
      showAlert(Alert.AlertType.INFORMATION,
          "Nothing to save", "No players have been added yet.");
      return;
    }
    try {
      playerService.addPlayers(new ArrayList<>(selectedPlayers));
      showAlert(Alert.AlertType.INFORMATION,
          "Player saved",
          "Player(s) saved!");
    } catch (RuntimeException runtimeException) {
      logger.log(Level.SEVERE, "CSV append failed", runtimeException);
      showAlert(Alert.AlertType.ERROR,
          "Save failed",
          "Could not save the players.");
    }
  }

  /**
   * Handles the action of Browse for a player token image.
   *
   * @param shapeComboBox The ComboBox for token shape selection.
   * @param imgPath       The Label to display the selected image path.
   */
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

  /**
   * Creates the panel to display players currently added to the game.
   * Shows selected player cards and options to edit total player count or save players.
   *
   * @return A {@link VBox} displaying selected players.
   */
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

    Button savePlayerBtn = Buttons.getEditBtn("Save Player");
    savePlayerBtn.setStyle("-fx-background-color: #008000");
    savePlayerBtn.setOnAction(e -> handleSavePlayers());

    tablePanel.getChildren()
        .addAll(playersBox, spacer, playersCountLabel, editCountBtn, savePlayerBtn);
    return tablePanel;
  }

  /**
   * Creates the panel displaying available players loaded from storage.
   * Allows users to select existing players or import players from a CSV file.
   *
   * @return A {@link VBox} displaying available players.
   * @throws IOException If there's an error reading player data.
   */
  private VBox createAvailablePlayersPanel() throws IOException {
    String heading = "LUDO".equalsIgnoreCase(selectedGame)
        ? "Saved Ludo players"
        : "Existing players";
    VBox availablePanel = createPanel(heading);
    availablePanel.setPrefWidth(PANEL_WIDTH);

    ObservableList<Player> availablePlayers = FXCollections.observableArrayList(
        playerService.readPlayersFromFile(PlayerService.PLAYER_FILE_PATH)
    );

    VBox playersBox = new VBox(5);
    playersBox.setFillWidth(true);
    playersBox.setStyle("-fx-background-color: transparent;");

    Function<Player, AvailablePlayerCard> cardFactory = player -> {
      return new AvailablePlayerCard(player, this::addSelectedPlayer);
    };

    playersBox.getChildren().setAll(
        availablePlayers.stream()
            .map(cardFactory)
            .toList()
    );

    availablePlayers.addListener((ListChangeListener<Player>) c ->
        playersBox.getChildren().setAll(
            availablePlayers.stream()
                .map(cardFactory)
                .toList()
        )
    );

    Button importCsvBtn = Buttons.getEditBtn("Import CSV");
    importCsvBtn.setOnAction(e -> handleImportCsv(availablePlayers));

    ScrollPane scroll = createTransparentScroll(playersBox, 250, 650);
    VBox.setVgrow(scroll, Priority.ALWAYS);
    availablePanel.getChildren().add(scroll);
    availablePanel.getChildren().add(importCsvBtn);
    return availablePanel;
  }

  /**
   * Handles importing player data from a CSV file.
   * Replaces the current list of available players with those from the CSV.
   *
   * @param availablePlayers The observable list to update with imported players.
   */
  private void handleImportCsv(ObservableList<Player> availablePlayers) {
    FileChooser chooser = new FileChooser();
    chooser.setTitle("Import players from CSV");
    chooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("CSV files", "*.csv")
    );
    File file = chooser.showOpenDialog(scene.getWindow());
    if (file == null) {
      return;
    }

    List<Player> imported;
    try {
      imported = playerService.readPlayersFromFile(file.getAbsolutePath());
    } catch (IllegalArgumentException | IOException iae) {
      logger.log(Level.WARNING, "CSV parse error", iae);
      showAlert(
          Alert.AlertType.ERROR,
          "Oops! We couldn’t read that CSV.",
          "Each player needs five comma‑separated values, in this order:\n" +
              "  1. Name (e.g. Kristian)\n" +
              "  2. Color as 8‑digit hex (e.g. #FF0000FF)\n" +
              "  3. Shape (circle, square, or triangle)\n" +
              "  4. Birthdate (YYYY‑MM‑DD)\n" +
              "  5. Image path (can be left blank)\n\n" +
              "Make sure there’s *no* header row and nothing extra.\n" +
              "Example:\n" +
              "Alice,#FF0000FF,circle,2010-05-15,"
      );
      return;
    }

    if (imported.isEmpty()) {
      showAlert(Alert.AlertType.INFORMATION,
          "No players found",
          "The CSV did not contain any valid player rows.");
      return;
    }

    availablePlayers.setAll(imported);
  }

  /**
   * Adds a chosen available player to the selectedPlayers list and checking limits.
   */
  private void addSelectedPlayer(Player chosen) {
    if (isAtMaxPlayers()) {
      showAlert(Alert.AlertType.WARNING,
          "Maximum players reached",
          "Limit: " + getTotalPlayerCount());
      return;
    }
    TokenView tokenView = new TokenView(Token.token(
        chosen.getToken().getTokenColor(),
        chosen.getToken().getTokenShape(),
        chosen.getToken().getImagePath()
    ));
    selectedPlayers.add(new Player(chosen.getName(), tokenView));
    updatePlayersCountLabel();
  }

  /**
   * Creates a transparent ScrollPane around the given content.
   */
  private ScrollPane createTransparentScroll(Node content, double prefHeight, double maxHeight) {
    ScrollPane scroll = new ScrollPane(content);
    scroll.getStyleClass().add("transparent-scroll");
    scroll.setFitToWidth(true);
    scroll.setPrefHeight(prefHeight);
    scroll.setMaxHeight(maxHeight);
    scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    scroll.setStyle("-fx-background-color: transparent");
    return scroll;
  }

  /**
   * Creates the HBox container for bottom navigation buttons (Back, Start Game, To Main Page).
   *
   * @return A configured {@link HBox} with navigation buttons.
   */
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
          throw new GameUIException("Failed to load board game scene", exception);
        }
      }
    });

    Button mainPageBtn = Buttons.getExitBtn("To Main Page");
    mainPageBtn.setOnAction(event -> SceneManager.showLandingScene());

    container.getChildren().addAll(backBtn, spacerLeft, startGameBtn, spacerRight, mainPageBtn);
    return container;
  }

  /**
   * Resets the selected image for the player token and updates UI elements.
   *
   * @param shapeComboBox       The ComboBox for token shape, re-enabled if an image is removed.
   * @param imgPathDisplayLabel The Label displaying the image path, cleared on reset.
   */
  private void resetSelectedImage(ComboBox<String> shapeComboBox, Label imgPathDisplayLabel) {
    this.selectedImage = null;
    imgPathDisplayLabel.setText("");

    shapeComboBox.setDisable(false);
  }

  /**
   * Handles adding a new player based on input fields.
   * Validates inputs, processes optional image selection, creates a new player object,
   * and adds it to the selected players list.
   *
   * @param nameField     The TextField for player name.
   * @param shapeComboBox The ComboBox for token shape.
   * @param dobPicker     The DatePicker for player date of birth.
   */
  private void handleAddPlayer(TextField nameField, ComboBox<String> shapeComboBox,
                               DatePicker dobPicker) {
    String name = nameField.getText();
    String shapeValue = shapeComboBox.getValue();
    Color color = playerColorPicker.getValue();

    if (isAtMaxPlayers()) {
      showAlert(Alert.AlertType.WARNING, "Maximum players reached",
          "Limit: " + getTotalPlayerCount());
      return;
    }

    if (name == null || name.isBlank() || color == null) {
      showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill out Name and Color fields.");
      return;
    }

    String runtimeUri = null;
    String finalShape = shapeValue;

    if (this.selectedImage != null) {
      try {
        String uniqueName = "image-" + this.selectedImage.getName();
        String storedImgPath = IMG_DIR + "/" + uniqueName;
        Path targetDir = Paths.get(IMG_DIR);
        Files.createDirectories(targetDir);
        Path destination = targetDir.resolve(uniqueName);
        Files.copy(this.selectedImage.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
        runtimeUri = destination.toUri().toString();

        finalShape = "circle";

      } catch (IOException ex) {
        logger.log(Level.SEVERE, "Error copying image file", ex);
        showAlert(Alert.AlertType.ERROR, "Image Error",
            "Could not save the selected image. Please try again or proceed without an image.");
        return;
      }
    } else {
      if (finalShape == null) {
        finalShape = "circle";
      }
      logger.log(Level.INFO, "No image selected. Player will use shape: " + finalShape);
    }

    TokenView token = new TokenView(Token.token(color, finalShape, runtimeUri));

    LocalDate dob = dobPicker.getValue();
    if (dob == null) {
      dob = randomBirthDate();
    }

    int age = Period.between(dob, LocalDate.now()).getYears();
    if (age < 3) {
      showAlert(Alert.AlertType.INFORMATION, "Too Young",
          "This game is designed for players 3 years and above.");
      return;
    }

    Player newPlayer = new Player(name, token, dob);
    selectedPlayers.add(newPlayer);
    updatePlayersCountLabel();

    resetInputs(nameField, shapeComboBox, dobPicker);

    logger.log(Level.INFO, "Added player: {0} (Shape: {1}, Image: {2})",
        new Object[] {name, finalShape, (runtimeUri != null ? "Custom" : "None")});
  }

  /**
   * Generates a random birth date within a predefined range.
   *
   * @return A random {@link LocalDate}.
   */
  private LocalDate randomBirthDate() {
    long start = LocalDate.of(1980, 1, 1).toEpochDay();
    long end = LocalDate.of(2003, 1, 1).toEpochDay();
    long randomDay = ThreadLocalRandom.current().nextLong(start, end);
    return LocalDate.ofEpochDay(randomDay);
  }


  /**
   * Resets the player input fields to their default states.
   *
   * @param nameField     The TextField for player name.
   * @param shapeComboBox The ComboBox for token shape.
   * @param dobPicker     The DatePicker for date of birth.
   */
  private void resetInputs(TextField nameField, ComboBox<String> shapeComboBox,
                           DatePicker dobPicker) {
    nameField.clear();
    if (!"LUDO".equalsIgnoreCase(selectedGame)) {
      shapeComboBox.getSelectionModel().clearSelection();
    }
    shapeComboBox.setDisable(false);

    playerColorPicker.setValue(Color.WHITE);
    dobPicker.setValue(null);

    this.selectedImage = null;
  }

  /**
   * Checks if the number of selected players has reached the maximum allowed.
   *
   * @return True if the maximum number of players is reached, false otherwise.
   */
  private boolean isAtMaxPlayers() {
    return selectedPlayers.size() >= getTotalPlayerCount();
  }

  /**
   * Creates a styled VBox panel with a heading.
   *
   * @param headingText The text for the panel's heading label.
   * @return A configured {@link VBox} panel.
   */
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

  /**
   * Creates a styled heading label.
   *
   * @param text The text for the label.
   * @return A configured {@link Label}.
   */
  private Label createHeadingLabel(String text) {
    Label heading = new Label(text);
    heading.setWrapText(true);
    heading.getStyleClass().add("label-heading");
    addHeadingAnimation(heading);
    return heading;
  }

  /**
   * Adds a hover animation to a label (slight translate and opacity change).
   *
   * @param label The {@link Label} to animate.
   */
  private void addHeadingAnimation(Label label) {
    label.setOnMouseEntered(e -> new Timeline(new KeyFrame(Duration.millis(300),
        new KeyValue(label.translateXProperty(), 5),
        new KeyValue(label.opacityProperty(), 0.9))).play());
    label.setOnMouseExited(e -> new Timeline(new KeyFrame(Duration.millis(300),
        new KeyValue(label.translateXProperty(), 0),
        new KeyValue(label.opacityProperty(), 1.0))).play());
  }

  /**
   * Adds a hover animation to a VBox panel (slight scale and opacity change).
   *
   * @param panel The {@link VBox} panel to animate.
   */
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

  /**
   * Adds a general scale animation on mouse hover to a Node.
   *
   * @param node        The {@link Node} to animate.
   * @param targetScale The target scale factor.
   * @param duration    The duration of the animation.
   */
  private void addScaleAnimation(javafx.scene.Node node, double targetScale, Duration duration) {
    node.setOnMouseEntered(e -> new Timeline(new KeyFrame(duration,
        new KeyValue(node.scaleXProperty(), targetScale),
        new KeyValue(node.scaleYProperty(), targetScale))).play());
    node.setOnMouseExited(e -> new Timeline(new KeyFrame(duration,
        new KeyValue(node.scaleXProperty(), 1),
        new KeyValue(node.scaleYProperty(), 1))).play());
  }

  /**
   * Creates a ListView for displaying players.
   * Each cell shows the player's name, token color, and token shape.
   *
   * @param players An {@link ObservableList} of {@link Player} objects.
   * @return A configured {@link ListView}.
   */
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

  /**
   * Capitalizes the first letter of a string and makes the rest lowercase.
   *
   * @param input The string to capitalize.
   * @return The capitalized string, or an empty string if input is null or blank.
   */
  private String capitalize(String input) {
    if (input == null || input.isBlank()) {
      return "";
    }
    return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
  }

  /**
   * Shows an alert dialog.
   *
   * @param type    The {@link Alert.AlertType} of the alert.
   * @param title   The title of the alert window.
   * @param message The content message of the alert.
   */
  private void showAlert(Alert.AlertType type, String title, String message) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setContentText(message);
    alert.showAndWait();
  }

  /**
   * Gets the JavaFX scene for this player selection view.
   *
   * @return The {@link Scene}.
   */
  public Scene getScene() {
    return scene;
  }
}