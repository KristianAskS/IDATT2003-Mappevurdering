package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.model.entity.Tile;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TileView extends StackPane {
  public static final int TILE_SIZE = 73;
  private TileView tileView;

  public TileView(Tile tile, int TILE_SIZE) {
    this.setId("tile" + tile.getTileId());

    int tileId = tile.getTileId();
    tile.setNextTileId(-1);
    Rectangle rectangle = new Rectangle(TILE_SIZE, TILE_SIZE);

    rectangle.setFill(null);
    rectangle.setStroke(Color.BLACK);

    Text numb = new Text(String.valueOf(tileId));
    numb.setFont(Font.font(15));

    StackPane.setAlignment(numb, Pos.BOTTOM_RIGHT);
    StackPane.setMargin(numb, new javafx.geometry.Insets(0, 5, 5, 0));

    DropShadow dropShadow = new DropShadow();
    dropShadow.setRadius(10.0);
    dropShadow.setOffsetX(10.0);
    dropShadow.setOffsetY(10.0);
    dropShadow.setColor(Color.color(0, 0, 0.6, 0.9));
    rectangle.setEffect(dropShadow);

    getChildren().addAll(rectangle, numb);
  }

  public void addTileActionViewLbl(String description, Color color) {
    Label tileActionViewLbl = new Label(description);
    tileActionViewLbl.setTextFill(color);
    tileActionViewLbl.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");
    this.getChildren().add(tileActionViewLbl);
  }
}
