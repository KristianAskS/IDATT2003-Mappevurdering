package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.model.entity.Player;
import java.util.function.Consumer;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * <p>A card UI component that displays a selected {@link Player}</p>
 * <p>Shows the player’s token, name, and an 'X' button.</p>
 *
 * @author Kristian Ask Selmer
 * @since 1.0
 */
public class SelectedPlayerCard extends HBox {

  /**
   * Constructs a new SelectedPlayerCard.
   *
   * @param player   the {@link Player}
   * @param onDelete a callback executed when the delete button is pressed,
   *                 receiving the player as argument
   */
  public SelectedPlayerCard(Player player, Consumer<Player> onDelete) {
    super(10);
    setAlignment(Pos.CENTER_LEFT);
    setMaxWidth(Double.MAX_VALUE);

    getStyleClass().add("selected-card");

    Label nameLbl = new Label(player.getName());
    nameLbl.getStyleClass().add("label-listview");
    nameLbl.setMinWidth(90);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    Button deleteBtn = new Button("❌");
    deleteBtn.getStyleClass().add("delete-btn");
    deleteBtn.setStyle("-fx-background-color: transparent;");
    deleteBtn.setOnAction(e -> onDelete.accept(player));

    Node tokenPreview = TokenPreview.create(player.getToken());

    getChildren().addAll(tokenPreview, nameLbl, spacer, deleteBtn);
  }
}
