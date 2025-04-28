package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.model.entity.Player;
import java.util.function.Consumer;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

    Label nameLbl = new Label(player.getName());
    nameLbl.getStyleClass().add("label-listview");
    nameLbl.setMinWidth(90);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    Node tokenPreview;
    if (player.getToken().getTokenShape() != null && player.getToken().getImagePath() == null) {
      tokenPreview = new Rectangle(COLOR_BOX_SIZE, COLOR_BOX_SIZE,
          player.getToken().getTokenColor());
      ((Rectangle) tokenPreview).setStroke(Color.BLACK);
    } else {
      ImageView iv = new ImageView(new Image(player.getToken().getImagePath(), 18, 18, true, true));
      tokenPreview = iv;
    }

    getChildren().addAll(nameLbl, spacer, tokenPreview);

    setOnMouseClicked(e -> onSelect.accept(player));
  }
}