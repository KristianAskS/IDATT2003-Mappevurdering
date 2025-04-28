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
 * Compact, fully–transparent card that visualises a {@link Player}.
 */
public class AvailablePlayerCard extends HBox {


  public AvailablePlayerCard(Player player, Consumer<Player> onSelect) {
    super(10);
    setAlignment(Pos.CENTER_LEFT);
    setMaxWidth(Double.MAX_VALUE);
    setCursor(Cursor.HAND);

    getStyleClass().add("available-card");
    Label nameLbl = new Label(player.getName());
    nameLbl.getStyleClass().add("label-listview");
    nameLbl.setMinWidth(90);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    Node tokenPreview = TokenPreview.create(player.getToken());

    getChildren().addAll(tokenPreview, nameLbl, spacer);
    setOnMouseClicked(e -> onSelect.accept(player));
  }
}