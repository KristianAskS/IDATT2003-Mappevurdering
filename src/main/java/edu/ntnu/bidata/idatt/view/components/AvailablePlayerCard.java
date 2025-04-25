package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.model.entity.Player;
import java.util.function.Consumer;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Compact, fully–transparent card that visualises a {@link Player}.
 */
public class AvailablePlayerCard extends HBox {

  private static final int COLOR_BOX_SIZE = 18;

  public AvailablePlayerCard(Player player, Consumer<Player> onSelect) {
    super(10);
    setAlignment(Pos.CENTER_LEFT);
    setMaxWidth(Double.MAX_VALUE);

    getStyleClass().add("available-card");

    setCursor(Cursor.HAND);

    Rectangle colorBox =
        new Rectangle(COLOR_BOX_SIZE, COLOR_BOX_SIZE, player.getToken().getTokenColor());
    colorBox.setStroke(Color.BLACK);

    Label nameLbl = new Label(player.getName());
    nameLbl.getStyleClass().add("label-listview");
    nameLbl.setMinWidth(90);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    getChildren().addAll(colorBox, nameLbl, spacer);

    setOnMouseClicked(e -> onSelect.accept(player));
  }
}