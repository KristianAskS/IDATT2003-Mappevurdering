package edu.ntnu.bidata.idatt.view.components;

import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_HEIGHT;
import static edu.ntnu.bidata.idatt.view.SceneManager.SCENE_WIDTH;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


public class SnakesAndLaddersView extends Application {
  private Ladders ladders;
  private Snakes snakes;

  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    Group root = new Group();
    Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT, Color.LIGHTSKYBLUE);
    Stage stage = new Stage();

    ladders.drawLadder();
    snakes.drawSnake();

    Rectangle rectangle = new Rectangle();
    rectangle.setX(100);
    rectangle.setY(100);
    rectangle.setWidth(100);
    rectangle.setHeight(100);
    rectangle.setFill(Color.BLUE);
    rectangle.setStrokeWidth(5);
    rectangle.setStroke(Color.BLACK);

    Polygon triangle = new Polygon();
    triangle.getPoints().setAll(
        200.0, 200.0,
        300.0, 300.0,
        200.0, 300.0
    );
    triangle.setFill(Color.YELLOW);

    Circle circle = new Circle();
    circle.setCenterX(350);
    circle.setCenterY(350);
    circle.setRadius(50);
    circle.setFill(Color.ORANGE);

    root.getChildren().add(rectangle);
    root.getChildren().add(triangle);
    root.getChildren().add(circle);
    stage.setScene(scene);
    stage.show();
  }
}
