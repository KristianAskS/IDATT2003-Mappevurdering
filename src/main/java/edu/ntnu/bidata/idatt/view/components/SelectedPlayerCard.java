package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.model.entity.Player;
import java.util.function.Consumer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Compact, fully–transparent card that visualises a {@link Player}.
 */
public class SelectedPlayerCard extends HBox {

  private static final int COLOR_BOX_SIZE = 18;

  /**
   * @param player   the player to visualise
   * @param onDelete callback executed when the remove button is pressed
   */
  public SelectedPlayerCard(Player player, Consumer<Player> onDelete) {
    super(10);
    setAlignment(Pos.CENTER_LEFT);
    // setStyle("-fx-background-color: transparent;");
    setMaxWidth(Double.MAX_VALUE);

    getStyleClass().add("selected-card");

    Rectangle colorBox =
        new Rectangle(COLOR_BOX_SIZE, COLOR_BOX_SIZE, player.getToken().getTokenColor());
    colorBox.setStroke(Color.BLACK);

    Label nameLbl = new Label(player.getName());
    nameLbl.getStyleClass().add("label-listview");
    nameLbl.setMinWidth(90);

    String shape = player.getToken().getTokenShape();

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    Button deleteBtn = new Button("❌");
    deleteBtn.getStyleClass().add("delete-btn");
    deleteBtn.setStyle("-fx-background-color: transparent;");
    deleteBtn.setOnAction(e -> onDelete.accept(player));

    getChildren().addAll(colorBox, nameLbl, spacer, deleteBtn);
  }
}