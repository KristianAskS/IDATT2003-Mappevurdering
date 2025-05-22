package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.model.entity.Player;
import java.util.function.Consumer;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

/**
 * <p>A card UI component that displays a {@link Player}'s
 * token and name, and invokes a callback function when selected.</p>
 *
 * <p>This card is fully transparent except for its contents and expands horizontally
 * {@code onSelect} callback with the associated player.</p>
 *
 * @author Kristian Ask Selmer
 * @since 1.0
 */
public class AvailablePlayerCard extends HBox {

  /**
   * Constructs a new AvailablePlayerCard for the given player.
   *
   * @param player   the {@link Player} to visualize
   * @param onSelect a callback invoked when this card is clicked, receiving the player
   */
  public AvailablePlayerCard(Player player, Consumer<Player> onSelect) {
    super(10);
    setAlignment(Pos.CENTER_LEFT);
    setMaxWidth(Double.MAX_VALUE);
    setCursor(Cursor.HAND);

    getStyleClass().add("available-card");

    Label nameLbl = new Label(player.getName());
    nameLbl.getStyleClass().add("label-listview");
    nameLbl.setMinWidth(90);
    nameLbl.setWrapText(true);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    Node tokenPreview = TokenPreview.create(player.getToken());

    getChildren().addAll(tokenPreview, nameLbl, spacer);
    setOnMouseClicked(e -> onSelect.accept(player));
  }
}
