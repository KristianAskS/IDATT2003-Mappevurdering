package edu.ntnu.bidata.idatt.view.components;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

/**
 * <p>Factory and utility methods for creating styled {@link Button} instances</p>
 *
 * @author Tri Tac Le
 * @version 1.2
 * @since 1.0
 */
public class Buttons {
  private static final double HOVER_SCALE = 1.1;
  private static final double ANIMATION_DURATION = 200;

  /**
   * <p>Attaches mouse event handlers to the given button to animate scale and rotation
   * on hover, exit, press, and release.</p>
   *
   * @param btn the {@link Button} where the effects will be applied
   */
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
    });

    btn.setOnMouseReleased(e -> {
      ScaleTransition scaleUp = new ScaleTransition(Duration.millis(ANIMATION_DURATION), btn);
      scaleUp.setToX(btn.isHover() ? HOVER_SCALE : 1.0);
      scaleUp.setToY(btn.isHover() ? HOVER_SCALE : 1.0);
      scaleUp.play();
    });
  }

  /**
   * Creates a default "Back" button with appropriate CSS classes and animations.
   *
   * @param text the text to display on the button
   * @return a styled {@link Button} instance
   */
  public static Button getBackBtn(String text) {
    Button backBtn = new Button(text);
    backBtn.getStyleClass().addAll("button", "back-btn");
    applyButtonEffects(backBtn);
    return backBtn;
  }

  /**
   * Creates a default "Exit" button with appropriate CSS classes and animations.
   *
   * @param text the text to display on the button
   * @return a styled {@link Button} instance
   */
  public static Button getExitBtn(String text) {
    Button exitBtn = new Button(text);
    exitBtn.getStyleClass().addAll("button", "exit-btn");
    applyButtonEffects(exitBtn);
    return exitBtn;
  }

  /**
   * Creates a large primary action button with CSS styling and animations.
   *
   * @param text the text to display on the button
   * @return a styled {@link Button} instance
   */
  public static Button getPrimaryBtn(String text) {
    Button primaryBtn = new Button(text);
    primaryBtn.getStyleClass().addAll("button", "primary-btn");
    primaryBtn.setPrefSize(400, 120);
    applyButtonEffects(primaryBtn);
    return primaryBtn;
  }

  /**
   * Creates a small primary action button with CSS styling and animations.
   *
   * @param text the text to display on the button
   * @return a styled {@link Button} instance
   */
  public static Button getSmallPrimaryBtn(String text) {
    Button smallPrimaryBtn = new Button(text);
    smallPrimaryBtn.getStyleClass().addAll("button", "small-primary-btn");
    applyButtonEffects(smallPrimaryBtn);
    return smallPrimaryBtn;
  }

  /**
   * Creates a secondary action button with CSS styling and animations.
   *
   * @param text the text to display on the button
   * @return a styled {@link Button} instance
   */
  public static Button getSecondaryBtn(String text) {
    Button secondaryBtn = new Button(text);
    secondaryBtn.getStyleClass().addAll("button", "secondary-btn");
    secondaryBtn.setMinSize(300, 80);
    applyButtonEffects(secondaryBtn);
    return secondaryBtn;
  }

  /**
   * Creates an edit button with CSS styling.
   *
   * @param text the text to display on the button
   * @return a styled {@link Button} instance
   */
  public static Button getEditBtn(String text) {
    Button editBtn = new Button(text);
    editBtn.getStyleClass().addAll("button", "edit-btn");
    return editBtn;
  }
}
