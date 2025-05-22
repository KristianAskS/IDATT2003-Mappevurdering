package edu.ntnu.bidata.idatt.view.components;

import java.util.Objects;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

/**
 * <p>Provides a reusable JavaFX {@link BackgroundImage}.</p>
 *
 * <p>The image is loaded once from the path and have these functionalities:
 * <ul>
 *   <li>Not repeat horizontally or vertically ({@link BackgroundRepeat#NO_REPEAT}),</li>
 *   <li>Be centered in both dimensions ({@link BackgroundPosition#CENTER}),</li>
 *   <li>Scale to fill the container while maintaining aspect ratio.</li>
 * </ul>
 * </p>
 *
 * @author Tri Tac Le
 * @since 1.0
 */
public class BackgroundImageView {

  /**
   * Loads and returns the background image as a
   * {@link BackgroundImage}.
   *
   * @return a {@link BackgroundImage} ready to be applied
   * as a scene or a background
   * @throws NullPointerException if the image resource cannot be found
   */
  public static javafx.scene.layout.BackgroundImage getBackgroundImage() {
    Image bgImage = new Image(
        Objects.requireNonNull(
            BackgroundImageView.class.getResource(
                "/edu/ntnu/bidata/idatt/images/backgroundImg.jpg"
            )
        ).toExternalForm()
    );
    return new javafx.scene.layout.BackgroundImage(
        bgImage,
        BackgroundRepeat.NO_REPEAT,
        BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.CENTER,
        new BackgroundSize(
            1.0, 1.0,
            true, true,
            false, false
        )
    );
  }
}
