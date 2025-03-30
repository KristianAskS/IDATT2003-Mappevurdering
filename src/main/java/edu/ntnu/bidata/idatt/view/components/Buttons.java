package edu.ntnu.bidata.idatt.view.components;

import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class Buttons {

  private static final double HOVER_SCALE = 1.05;
  private static final double ANIMATION_DURATION = 200; //milliseconds

  private static void applyHoverEffect(Button btn, String defaultStyle, String hoverStyle) {
    btn.setOnMouseEntered(e -> {
      btn.setStyle(hoverStyle);
      ScaleTransition scaleUp = new ScaleTransition(Duration.millis(ANIMATION_DURATION), btn);
      scaleUp.setToX(HOVER_SCALE);
      scaleUp.setToY(HOVER_SCALE);
      scaleUp.play();
    });
    btn.setOnMouseExited(e -> {
      btn.setStyle(defaultStyle);
      ScaleTransition scaleDown = new ScaleTransition(Duration.millis(ANIMATION_DURATION), btn);
      scaleDown.setToX(1.0);
      scaleDown.setToY(1.0);
      scaleDown.play();
    });
  }

  public static Button getBackBtn(String text) {
    Button backBtn = new Button(text);
    String defaultStyle =
        "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: 'monospace';" +
            "-fx-text-fill: #ffffff;" +
            "-fx-background-color: #7DBED7;" +
            "-fx-border-color: black;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(three-pass-box, black, 10, 0, 0, 0);" +
            "-fx-padding: 10 20 10 20;";
    String hoverStyle =
        "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: 'monospace';" +
            "-fx-text-fill: #ffffff;" +
            "-fx-background-color: #B3E5FC;" +
            "-fx-border-color: black;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(three-pass-box, black, 10, 0, 0, 0);" +
            "-fx-padding: 10 20 10 20;";
    backBtn.setStyle(defaultStyle);
    applyHoverEffect(backBtn, defaultStyle, hoverStyle);
    return backBtn;
  }

  public static Button getExitBtn(String text) {
    Button exitBtn = new Button(text);
    String defaultStyle =
        "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: 'monospace';" +
            "-fx-text-fill: #ffffff;" +
            "-fx-background-color: #E57373;" +
            "-fx-border-color: black;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 0;" +
            "-fx-background-radius: 0;" +
            "-fx-effect: dropshadow(three-pass-box, black, 10, 0, 0, 0);" +
            "-fx-padding: 10 20 10 20;";
    String hoverStyle =
        "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: 'monospace';" +
            "-fx-text-fill: #ffffff;" +
            "-fx-background-color: #EF5350;" +
            "-fx-border-color: black;" +
            "-fx-border-width: 1;" +
            "-fx-border-radius: 0;" +
            "-fx-background-radius: 0;" +
            "-fx-effect: dropshadow(three-pass-box, black, 10, 0, 0, 0);" +
            "-fx-padding: 10 20 10 20;";
    exitBtn.setStyle(defaultStyle);
    applyHoverEffect(exitBtn, defaultStyle, hoverStyle);
    return exitBtn;
  }

  public static Button getMainBtn(String text) {
    Button mainBtn = new Button(text);
    String defaultStyle =
        "-fx-font-size: 26px;" +
            "-fx-font-family: 'monospace';" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-background-color: linear-gradient(#61a2b1, #2A5058);" +
            "-fx-background-radius: 100;" +
            "-fx-background-insets: 0;" +
            "-fx-padding: 15 30 15 30;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0.5, 0, 2)";
    String hoverStyle =
        "-fx-font-size: 30px;" +
            "-fx-font-weight: bold;" +
            "-fx-font-family: 'monospace';" +
            "-fx-text-fill: #ffffff;" +
            "-fx-background-color: linear-gradient(#8BC6EC, #9599E2);" +
            "-fx-background-radius: 100;" +
            "-fx-background-insets: 0;" +
            "-fx-padding: 18 34 20 34;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0.5, 0, 2)";
    mainBtn.setStyle(defaultStyle);
    mainBtn.setPrefSize(400, 120);
    applyHoverEffect(mainBtn, defaultStyle, hoverStyle);
    return mainBtn;
  }

  public static Button getSecondaryBtn(String text) {
    Button secondaryBtn = new Button(text);
    String defaultStyle =
        "-fx-font-size: 20px;" +
            "-fx-font-family: 'monospace';" +
            "-fx-font-weight: normal;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-background-color: linear-gradient(#7B8D8E, #516A6A);" +
            "-fx-background-radius: 50;" +
            "-fx-background-insets: 0;" +
            "-fx-padding: 10 20 10 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0.5, 0, 1)";
    String hoverStyle =
        "-fx-font-size: 22px;" +
            "-fx-font-family: 'monospace';" +
            "-fx-font-weight: normal;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-background-color: linear-gradient(#8CA7A8, #637D7E);" +
            "-fx-background-radius: 50;" +
            "-fx-background-insets: 0;" +
            "-fx-padding: 12 22 12 22;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 6, 0.5, 0, 2)";
    secondaryBtn.setStyle(defaultStyle);
    secondaryBtn.setMinSize(300, 80);
    applyHoverEffect(secondaryBtn, defaultStyle, hoverStyle);
    return secondaryBtn;
  }
}
