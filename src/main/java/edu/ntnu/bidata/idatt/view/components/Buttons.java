package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.view.SceneManager;
import javafx.scene.control.Button;

/**
 * Class to store button components
 *
 * @author Trile
 */
public class Buttons {
  public static Button getBackBtn(String text) {
    Button backBtn = new Button(text);
    backBtn.setStyle(
        "-fx-background-color: #7DBED7;"
            + "-fx-border-color: black;"
            + "-fx-border-width: 2;"
            + "-fx-border-radius: 10;"
            + "-fx-background-radius: 10;"
            + "-fx-effect: dropshadow(three-pass-box, black, 10, 0, 0, 0);"
    );
    backBtn.setOnMouseEntered(e -> {
      backBtn.setStyle(
          "-fx-background-color: #B3E5FC;" +
              "-fx-border-color: black;" +
              "-fx-border-width: 2;" +
              "-fx-border-radius: 10;" +
              "-fx-background-radius: 10;" +
              "-fx-effect: dropshadow(three-pass-box, black, 10, 0, 0, 0);"
      );
    });

    backBtn.setOnMouseExited(e -> {
      backBtn.setStyle(
          "-fx-background-color: #7DBED7;" +
              "-fx-border-color: black;" +
              "-fx-border-width: 2;" +
              "-fx-border-radius: 10;" +
              "-fx-background-radius: 10;" +
              "-fx-effect: dropshadow(three-pass-box, black, 10, 0, 0, 0);"
      );
    });


    backBtn.setOnAction(e -> {
      SceneManager.showLandingScene();
    });
    return backBtn;
  }
}
