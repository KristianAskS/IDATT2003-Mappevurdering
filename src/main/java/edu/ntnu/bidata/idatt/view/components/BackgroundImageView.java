package edu.ntnu.bidata.idatt.view.components;

import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

public class BackgroundImageView {

  public static javafx.scene.layout.BackgroundImage getBackgroundImage() {
    Image bgImage = new Image(
        Objects.requireNonNull(BackgroundImageView.class.getResource(
            "/edu/ntnu/bidata/idatt/images/backgroundImg.jpg")).toExternalForm()
    );
    return new javafx.scene.layout.BackgroundImage(
        bgImage,
        BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.CENTER,
        new BackgroundSize(1.0, 1.0, true, true, false, false)
    );
  }
}
