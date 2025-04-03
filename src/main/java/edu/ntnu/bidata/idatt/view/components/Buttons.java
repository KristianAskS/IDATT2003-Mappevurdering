package edu.ntnu.bidata.idatt.view.components;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class Buttons {

  private static final double HOVER_SCALE = 1.1;
  private static final double ANIMATION_DURATION = 200;

  private static void applyButtonEffects(Button btn) {

    btn.setOnMouseEntered(e -> {
      ScaleTransition scaleUp = new ScaleTransition(Duration.millis(ANIMATION_DURATION), btn);
      scaleUp.setToX(HOVER_SCALE);
      scaleUp.setToY(HOVER_SCALE);
      scaleUp.play();

      RotateTransition rotate = new RotateTransition(Duration.millis(ANIMATION_DURATION), btn);
      rotate.setFromAngle(0);
      rotate.setToAngle(5);
      rotate.setCycleCount(2);
      rotate.setAutoReverse(true);
      rotate.play();
    });

    btn.setOnMouseExited(e -> {
      ScaleTransition scaleDown = new ScaleTransition(Duration.millis(ANIMATION_DURATION), btn);
      scaleDown.setToX(1.0);
      scaleDown.setToY(1.0);
      scaleDown.play();
    });

    btn.setOnMousePressed(e -> {
      ScaleTransition scaleDown = new ScaleTransition(Duration.millis(ANIMATION_DURATION), btn);
      scaleDown.setToX(0.95);
      scaleDown.setToY(0.95);
      scaleDown.play();

      TranslateTransition bounce = new TranslateTransition(Duration.millis(ANIMATION_DURATION), btn);
      bounce.setByY(3);
      bounce.setCycleCount(2);
      bounce.setAutoReverse(true);
      bounce.play();
    });

    btn.setOnMouseReleased(e -> {
      ScaleTransition scaleUp = new ScaleTransition(Duration.millis(ANIMATION_DURATION), btn);
      scaleUp.setToX(btn.isHover() ? HOVER_SCALE : 1.0);
      scaleUp.setToY(btn.isHover() ? HOVER_SCALE : 1.0);
      scaleUp.play();
    });
  }

  public static Button getBackBtn(String text) {
    Button backBtn = new Button(text);
    backBtn.getStyleClass().addAll("button", "back-btn");
    applyButtonEffects(backBtn);
    return backBtn;
  }

  public static Button getExitBtn(String text) {
    Button exitBtn = new Button(text);
    exitBtn.getStyleClass().addAll("button", "exit-btn");
    applyButtonEffects(exitBtn);
    return exitBtn;
  }

  public static Button getPrimaryBtn(String text) {
    Button primaryBtn = new Button(text);
    primaryBtn.getStyleClass().addAll("button", "primary-btn");
    primaryBtn.setPrefSize(400, 120);
    applyButtonEffects(primaryBtn);
    return primaryBtn;
  }

  public static Button getSecondaryBtn(String text) {
    Button secondaryBtn = new Button(text);
    secondaryBtn.getStyleClass().addAll("button", "secondary-btn");
    secondaryBtn.setMinSize(300, 80);
    applyButtonEffects(secondaryBtn);
    return secondaryBtn;
  }
}