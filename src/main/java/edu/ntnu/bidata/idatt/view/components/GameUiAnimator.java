package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEvent;
import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEventType;
import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import edu.ntnu.bidata.idatt.view.components.TileView;
import edu.ntnu.bidata.idatt.view.scenes.BoardGameScene;
import javafx.animation.PathTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

public class GameUiAnimator {
  private static final double TOKEN_PIXELS_PER_SECOND = 400.0;
  private final BoardGameScene scene;

  public GameUiAnimator(BoardGameScene scene) {
    this.scene = scene;
  }

  public void movePlayerAlongTiles(Player player, int steps, Runnable onDone) {
    Node token = player.getToken();
    if (token == null || steps == 0) {
      player.setCurrentTileId((player.getCurrentTileId() + steps - 1) %
          scene.getBoard().getTiles().size() + 1);
      onDone.run();
      return;
    }
    SequentialTransition seq = new SequentialTransition();
    int start = player.getCurrentTileId();
    int boardSize = scene.getBoard().getTiles().size();
    for (int i = 1; i <= steps; i++) {
      int nextId = (start + i) % boardSize;
      if (nextId == 0) nextId = boardSize;
      seq.getChildren().add(getHopTransition(player, nextId, token));
    }
    seq.setOnFinished(e -> {
      Tile landed = scene.getBoard().getTile(
          (start + steps - 1) % boardSize + 1);
      landed.addPlayer(player);
      onDone.run();
    });
    seq.play();
  }

  public void animateLadderMovement(Player player,
                                    int fromTileId,
                                    int toTileId,
                                    Runnable onDone) {
    TileView start = lookupTileView(fromTileId);
    TileView end = lookupTileView(toTileId);
    Node token = player.getToken();

    Point2D startPt = tileCenter(start);
    Pane parent = (Pane) token.getParent();
    parent.getChildren().remove(token);

    Pane overlay = scene.getTokenLayer();
    overlay.getChildren().add(token);
    token.setTranslateX(startPt.getX());
    token.setTranslateY(startPt.getY());

    Point2D endPt = tileCenter(end);
    Path path = new Path(new MoveTo(startPt.getX(), startPt.getY()),
        new LineTo(endPt.getX(), endPt.getY()));
    double dist = startPt.distance(endPt);
    double secs = dist / TOKEN_PIXELS_PER_SECOND;
    PathTransition pt = new PathTransition(Duration.seconds(secs), path, token);
    pt.setOnFinished(e -> {
      overlay.getChildren().remove(token);
      end.getChildren().add(token);
      scene.setTokenPositionOnTile(end);
      onDone.run();
    });
    pt.play();
  }

  private PauseTransition getHopTransition(Player player,
                                           int nextId,
                                           Node token) {
    PauseTransition pause = new PauseTransition(Duration.millis(250));
    pause.setOnFinished(ev -> {
      Pane parent = (Pane) token.getParent();
      parent.getChildren().remove(token);
      TileView tv = lookupTileView(nextId);
      tv.getChildren().add(token);
      scene.setTokenPositionOnTile(tv);
      player.setCurrentTileId(nextId);
    });
    return pause;
  }

  private Point2D tileCenter(TileView tv) {
    Bounds b = tv.localToScene(tv.getBoundsInLocal());
    double x = b.getMinX() + b.getWidth() * 0.5;
    double y = b.getMinY() + b.getHeight() * 0.5;
    return scene.getTokenLayer().sceneToLocal(x, y);
  }

  private TileView lookupTileView(int id) {
    return (TileView) scene.getScene().lookup("#tile" + id);
  }
}