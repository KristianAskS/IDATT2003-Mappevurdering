package edu.ntnu.bidata.idatt.model;

import edu.ntnu.bidata.idatt.entity.Player;
import edu.ntnu.bidata.idatt.logic.action.TileAction;
import java.util.Stack;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * The type Tile.
 */
public class Tile extends StackPane {
  private int tileId;
  private int nextTileId;
  private Tile nextTile;
  private TileAction landAction;
  private double x;
  private double y;

  public Tile() {
  }

  public Tile(int tileId) {
    this.tileId = tileId;
    this.nextTileId = -1;
    Rectangle rectangle = new Rectangle(70, 70);

    rectangle.setFill(null);
    rectangle.setStroke(Color.BLACK);

    Text numb = new Text(String.valueOf(tileId));
    numb.setFont(Font.font(10));

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

  public TileAction getLandAction() {
    return landAction;
  }

  public void setLandAction(TileAction landAction) {
    this.landAction = landAction;
  }


  public void landPlayer(Player player) {

  }

  public void leavePlayer(Player player) {

  }

  public Tile getNextTile() {
    return nextTile;
  }

  public void setNextTile(Tile nextTile) {
    this.nextTile = nextTile;
  }

  public int getTileId() {
    return tileId;
  }

  public void setTileId(int tileId) {
    this.tileId = tileId;
  }

  public int getNextTileId() {
    return nextTileId;
  }

  public void setNextTileId(int nextTileId) {
    this.nextTileId = nextTileId;
  }
}
