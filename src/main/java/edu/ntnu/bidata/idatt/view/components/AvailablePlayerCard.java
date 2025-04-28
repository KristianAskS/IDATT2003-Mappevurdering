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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
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
    setCursor(Cursor.HAND);

    getStyleClass().add("available-card");
    Label nameLbl = new Label(player.getName());
    nameLbl.getStyleClass().add("label-listview");
    nameLbl.setMinWidth(90);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);

    Node tokenPreview;
    String imgPath = player.getToken().getImagePath();
    String shapeStr = player.getToken().getTokenShape() == null
        ? "" : player.getToken().getTokenShape().toLowerCase();
    Color tokenCol = player.getToken().getTokenColor();

    if (imgPath != null && !imgPath.isBlank()) {
      tokenPreview = new ImageView(
          new Image(imgPath, COLOR_BOX_SIZE, COLOR_BOX_SIZE, true, true)
      );
    } else {
      switch (shapeStr) {
        case "circle" -> {
          Circle c = new Circle(COLOR_BOX_SIZE / 2.0, tokenCol);
          c.setStroke(Color.BLACK);
          tokenPreview = c;
        }
        case "triangle" -> {
          Polygon t = new Polygon(
              COLOR_BOX_SIZE / 2.0, 0,
              COLOR_BOX_SIZE, COLOR_BOX_SIZE,
              0, COLOR_BOX_SIZE
          );
          t.setFill(tokenCol);
          t.setStroke(Color.BLACK);
          tokenPreview = t;
        }
        default -> {
          Rectangle r = new Rectangle(COLOR_BOX_SIZE, COLOR_BOX_SIZE, tokenCol);
          r.setStroke(Color.BLACK);
          tokenPreview = r;
        }
      }
    }

    getChildren().addAll(tokenPreview, nameLbl, spacer);
    setOnMouseClicked(e -> onSelect.accept(player));
  }
}