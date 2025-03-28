package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.view.SceneManager;
import javafx.scene.control.Button;

public class Buttons {

  public static Button getBackBtn(String text) {
    Button backBtn = new Button(text);
    backBtn.setStyle(
        "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-background-color: #7DBED7;" +
            "-fx-border-color: black;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(three-pass-box, black, 10, 0, 0, 0);" +
            "-fx-padding: 10 20 10 20;"
    );

    String hoverStyle =
        "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-background-color: #B3E5FC;" +
            "-fx-border-color: black;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(three-pass-box, black, 10, 0, 0, 0);" +
            "-fx-padding: 10 20 10 20;";

    backBtn.setOnMouseEntered(e -> backBtn.setStyle(hoverStyle));
    backBtn.setOnMouseExited(e -> {
      backBtn.setStyle(
          "-fx-font-size: 16px;" +
              "-fx-font-weight: bold;" +
              "-fx-text-fill: #ffffff;" +
              "-fx-background-color: #7DBED7;" +
              "-fx-border-color: black;" +
              "-fx-border-width: 2;" +
              "-fx-border-radius: 10;" +
              "-fx-background-radius: 10;" +
              "-fx-effect: dropshadow(three-pass-box, black, 10, 0, 0, 0);" +
              "-fx-padding: 10 20 10 20;"
      );
    });
    return backBtn;
  }

  public static Button getExitBtn(String text) {
    Button exitBtn = new Button(text);

    exitBtn.setStyle(
        "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-background-color: #7DBED7;" +
            "-fx-border-color: black;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(three-pass-box, black, 10, 0, 0, 0);" +
            "-fx-padding: 10 20 10 20;"
    );

    String hoverStyle =
        "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-background-color: #B3E5FC;" +
            "-fx-border-color: black;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-effect: dropshadow(three-pass-box, black, 10, 0, 0, 0);" +
            "-fx-padding: 10 20 10 20;";

    exitBtn.setOnMouseEntered(e -> exitBtn.setStyle(hoverStyle));
    exitBtn.setOnMouseExited(e -> {
      exitBtn.setStyle(
          "-fx-font-size: 16px;" +
              "-fx-font-weight: bold;" +
              "-fx-text-fill: #ffffff;" +
              "-fx-background-color: #7DBED7;" +
              "-fx-border-color: black;" +
              "-fx-border-width: 2;" +
              "-fx-border-radius: 10;" +
              "-fx-background-radius: 10;" +
              "-fx-effect: dropshadow(three-pass-box, black, 10, 0, 0, 0);" +
              "-fx-padding: 10 20 10 20;"
      );
    });

    return exitBtn;
  }

  public static Button getPlayBtn(String text) {
    Button playBtn = new Button(text);

    playBtn.setStyle(
        "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-background-color: linear-gradient(#61a2b1, #2A5058);" +
            "-fx-background-radius: 30;" +
            "-fx-background-insets: 0;" +
            "-fx-padding: 10 20 10 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0.5, 0, 2);"
    );

    String hoverStyle =
        "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #ffffff;" +
            "-fx-background-color: linear-gradient(#8BC6EC, #9599E2);" +
            "-fx-background-radius: 30;" +
            "-fx-background-insets: 0;" +
            "-fx-padding: 10 20 10 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.4), 10, 0.5, 0, 2);";

    playBtn.setOnMouseEntered(e -> playBtn.setStyle(hoverStyle));
    playBtn.setOnMouseExited(e -> {
      playBtn.setStyle(
          "-fx-font-size: 16px;" +
              "-fx-font-weight: bold;" +
              "-fx-text-fill: #ffffff;" +
              "-fx-background-color: linear-gradient(#61a2b1, #2A5058);" +
              "-fx-background-radius: 30;" +
              "-fx-background-insets: 0;" +
              "-fx-padding: 10 20 10 20;" +
              "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0.5, 0, 2);"
      );
    });

    return playBtn;
  }
}
