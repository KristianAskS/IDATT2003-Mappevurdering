package edu.ntnu.bidata.idatt.view.components;

import javafx.animation.ScaleTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

public class Buttons {

  private static final double HOVER_SCALE = 1.1;
  private static final double ANIMATION_DURATION = 200;

  /**
   * Applies distinct effects for hover, active (pressed), and focused states.
   *
   * @param btn the button to style
   * @param defaultStyle the style for the default state
   * @param hoverStyle the style when the mouse hovers over the button
   * @param activeStyle the style when the button is pressed
   * @param focusedStyle the style when the button is focused
   */
  private static void applyButtonEffects(Button btn, String defaultStyle, String hoverStyle, String activeStyle, String focusedStyle) {
    btn.setOnMouseEntered(e -> {
      btn.setStyle(hoverStyle);
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
      if (!btn.isFocused()) {
        btn.setStyle(defaultStyle);
      }
      ScaleTransition scaleDown = new ScaleTransition(Duration.millis(ANIMATION_DURATION), btn);
      scaleDown.setToX(1.0);
      scaleDown.setToY(1.0);
      scaleDown.play();
    });

    btn.setOnMousePressed((MouseEvent e) -> {
      btn.setStyle(activeStyle);
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

    btn.setOnMouseReleased((MouseEvent e) -> {
      if (btn.isHover()) {
        btn.setStyle(hoverStyle);
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(ANIMATION_DURATION), btn);
        scaleUp.setToX(HOVER_SCALE);
        scaleUp.setToY(HOVER_SCALE);
        scaleUp.play();
      } else {
        btn.setStyle(defaultStyle);
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(ANIMATION_DURATION), btn);
        scaleUp.setToX(1.0);
        scaleUp.setToY(1.0);
        scaleUp.play();
      }
    });

    btn.focusedProperty().addListener((obs, oldVal, newVal) -> {
      if (newVal) {
        btn.setStyle(focusedStyle);
      } else {
        if (btn.isHover()) {
          btn.setStyle(hoverStyle);
        } else {
          btn.setStyle(defaultStyle);
        }
      }
    });
  }

  public static Button getBackBtn(String text) {
    Button backBtn = new Button(text);
    String defaultStyle =
        "-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'monospace'; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-background-color: linear-gradient(to right, #42a5f5, #1e88e5); " +
            "-fx-border-color: #0D47A1; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0.5, 0, 2); " +
            "-fx-padding: 10 20 10 20;";
    String hoverStyle =
        "-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'monospace'; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-background-color: linear-gradient(to right, #64b5f6, #2196f3); " +
            "-fx-border-color: #1565C0; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 12, 0.5, 0, 2); " +
            "-fx-padding: 10 20 10 20;";
    String activeStyle =
        "-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'monospace'; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-background-color: linear-gradient(to right, #1e88e5, #1976d2); " +
            "-fx-border-color: #0D47A1; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 12, 0.5, 0, 2); " +
            "-fx-padding: 10 20 10 20;";
    String focusedStyle =
        "-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'monospace'; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-background-color: linear-gradient(to right, #90caf9, #42a5f5); " +
            "-fx-border-color: #0D47A1; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 12, 0.5, 0, 2); " +
            "-fx-padding: 10 20 10 20;";
    backBtn.setStyle(defaultStyle);
    applyButtonEffects(backBtn, defaultStyle, hoverStyle, activeStyle, focusedStyle);
    return backBtn;
  }

  public static Button getExitBtn(String text) {
    Button exitBtn = new Button(text);
    String defaultStyle =
        "-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'monospace'; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-background-color: linear-gradient(to right, #ef5350, #e53935); " +
            "-fx-border-color: #b71c1c; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0.5, 0, 2); " +
            "-fx-padding: 10 20 10 20;";
    String hoverStyle =
        "-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'monospace'; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-background-color: linear-gradient(to right, #f44336, #d32f2f); " +
            "-fx-border-color: #b71c1c; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 12, 0.5, 0, 2); " +
            "-fx-padding: 10 20 10 20;";
    String activeStyle =
        "-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'monospace'; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-background-color: linear-gradient(to right, #d32f2f, #c62828); " +
            "-fx-border-color: #b71c1c; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.8), 12, 0.5, 0, 2); " +
            "-fx-padding: 10 20 10 20;";
    String focusedStyle =
        "-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'monospace'; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-background-color: linear-gradient(to right, #ff8a80, #ff5252); " +
            "-fx-border-color: #b71c1c; -fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 12, 0.5, 0, 2); " +
            "-fx-padding: 10 20 10 20;";
    exitBtn.setStyle(defaultStyle);
    applyButtonEffects(exitBtn, defaultStyle, hoverStyle, activeStyle, focusedStyle);
    return exitBtn;
  }

  public static Button getPrimaryBtn(String text) {
    Button mainBtn = new Button(text);
    String defaultStyle =
        "-fx-font-size: 26px; -fx-font-family: 'monospace'; -fx-font-weight: bold; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-background-color: linear-gradient(to right, #ffeb3b, #fbc02d); " +
            "-fx-background-radius: 100; -fx-background-insets: 0; " +
            "-fx-padding: 15 30 15 30; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0.5, 0, 2);";
    String hoverStyle =
        "-fx-font-size: 30px; -fx-font-weight: bold; -fx-font-family: 'monospace'; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-background-color: linear-gradient(to right, #fff176, #fdd835); " +
            "-fx-background-radius: 100; -fx-background-insets: 0; " +
            "-fx-padding: 18 34 20 34; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 12, 0.5, 0, 2);";
    String activeStyle =
        "-fx-font-size: 28px; -fx-font-family: 'monospace'; -fx-font-weight: bold; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-background-color: linear-gradient(to right, #fbc02d, #f9a825); " +
            "-fx-background-radius: 100; -fx-background-insets: 0; " +
            "-fx-padding: 16 32 16 32; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.7), 12, 0.5, 0, 2);";
    String focusedStyle =
        "-fx-font-size: 28px; -fx-font-family: 'monospace'; -fx-font-weight: bold; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-background-color: linear-gradient(to right, #ffe082, #ffca28); " +
            "-fx-background-radius: 100; -fx-background-insets: 0; " +
            "-fx-padding: 16 32 16 32; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 12, 0.5, 0, 2);";
    mainBtn.setStyle(defaultStyle);
    mainBtn.setPrefSize(400, 120);
    applyButtonEffects(mainBtn, defaultStyle, hoverStyle, activeStyle, focusedStyle);
    return mainBtn;
  }

  public static Button getSecondaryBtn(String text) {
    Button secondaryBtn = new Button(text);
    String defaultStyle =
        "-fx-font-size: 20px; -fx-font-family: 'monospace'; -fx-font-weight: normal; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-background-color: linear-gradient(to right, #ab47bc, #8e24aa); " +
            "-fx-background-radius: 50; -fx-background-insets: 0; " +
            "-fx-padding: 10 20 10 20; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 8, 0.5, 0, 1);";
    String hoverStyle =
        "-fx-font-size: 22px; -fx-font-family: 'monospace'; -fx-font-weight: normal; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-background-color: linear-gradient(to right, #ba68c8, #9c27b0); " +
            "-fx-background-radius: 50; -fx-background-insets: 0; " +
            "-fx-padding: 12 22 12 22; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 10, 0.5, 0, 1);";
    String activeStyle =
        "-fx-font-size: 21px; -fx-font-family: 'monospace'; -fx-font-weight: normal; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-background-color: linear-gradient(to right, #8e24aa, #7b1fa2); " +
            "-fx-background-radius: 50; -fx-background-insets: 0; " +
            "-fx-padding: 11 21 11 21; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.6), 8, 0.5, 0, 1);";
    String focusedStyle =
        "-fx-font-size: 21px; -fx-font-family: 'monospace'; -fx-font-weight: normal; " +
            "-fx-text-fill: #FFFFFF; " +
            "-fx-background-color: linear-gradient(to right, #ce93d8, #ab47bc); " +
            "-fx-background-radius: 50; -fx-background-insets: 0; " +
            "-fx-padding: 11 21 11 21; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.5), 8, 0.5, 0, 1);";
    secondaryBtn.setStyle(defaultStyle);
    secondaryBtn.setMinSize(300, 80);
    applyButtonEffects(secondaryBtn, defaultStyle, hoverStyle, activeStyle, focusedStyle);
    return secondaryBtn;
  }
}
