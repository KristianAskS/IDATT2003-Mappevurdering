package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEvent;
import edu.ntnu.bidata.idatt.controller.patterns.observer.interfaces.BoardGameObserver;
import edu.ntnu.bidata.idatt.model.entity.Dice;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

/**
 * <p>Visual component for displaying and animating dice rolls in the game UI.</p>
 *
 * <p>Implements {@link BoardGameObserver} to receive game‐related events if needed.
 * Maintains two {@link ImageView} for the dice faces, a {@link Dice},
 * and an {@link IntegerProperty} to show the final roll sum.</p>
 *
 * @author Tri Tac Le
 * @since 1.0
 */
public class DiceView implements BoardGameObserver {
  public static final int NUMB_OF_DICE = 2;
  private static final Logger logger = Logger.getLogger(DiceView.class.getName());
  private static final String DICE_IMAGE_PATH = "/edu/ntnu/bidata/idatt/images/dice/";
  private final ImageView diceImageView1;
  private final ImageView diceImageView2;
  private final Dice dice;
  private final IntegerProperty rollResult;

  /**
   * Constructs a new {@code DiceView}
   */
  public DiceView() {
    diceImageView1 = createDieImageView();
    diceImageView2 = createDieImageView();
    dice = new Dice(NUMB_OF_DICE);
    rollResult = new SimpleIntegerProperty(0);
  }

  /**
   * Generates an {@link ImageView} object that holds the dice image
   * from the path
   *
   * @return the imageview with the images
   */
  private ImageView createDieImageView() {
    ImageView imageView = new ImageView(new Image(
        Objects.requireNonNull(
            getClass().getResourceAsStream(DICE_IMAGE_PATH + "1.png"))
    ));
    imageView.setFitWidth(100);
    imageView.setFitHeight(100);
    return imageView;
  }

  /**
   * Returns an {@link HBox} containing the two dice image views
   *
   * @return an HBox with the dice images
   */
  public HBox getDiceImageHBox() {
    return new HBox(10, diceImageView1, diceImageView2);
  }

  /**
   * Creates a {@link Timeline} animation that simulates rolling the dice.
   *
   * @param onFinished a {@link Runnable} callback to execute after the final roll completes
   * @return a {@link Timeline} ready to be played
   */
  public Timeline createRollDiceAnimation(Runnable onFinished) {
    Timeline timeline = new Timeline();
    final int frames = 10;
    final int intervalMs = 100;

    for (int i = 0; i < frames; i++) {
      timeline.getKeyFrames().add(new KeyFrame(
          Duration.millis(intervalMs * i),
          e -> updateRandomFaces()
      ));
    }

    timeline.setOnFinished(e -> {
      int[] result = dice.rollDice();
      int sum = result[0] + result[1];
      dice.setRollResult(sum);
      rollResult.set(sum);
      setFace(diceImageView1, result[0]);
      setFace(diceImageView2, result[1]);
      if (onFinished != null) {
        logger.log(Level.INFO, "Dice rolled: {0}", sum);
        onFinished.run();
      }
    });
    return timeline;
  }

  private void updateRandomFaces() {
    int[] r = dice.rollDice();
    setFace(diceImageView1, r[0]);
    setFace(diceImageView2, r[1]);
  }

  private void setFace(ImageView iv, int face) {
    iv.setImage(new Image(
        Objects.requireNonNull(
            getClass().getResourceAsStream(
                DICE_IMAGE_PATH + face + ".png"))
    ));
  }

  /**
   * Exposes the final roll sum as a {@link IntegerProperty}.
   *
   * @return the property containing the last roll sum
   */
  public IntegerProperty rollResultProperty() {
    return rollResult;
  }

  /**
   * Returns a styled button for triggering a die roll.
   *
   * @return a {@link Button} labeled "Press to roll"
   */
  public Button getRollDiceBtn() {
    return Buttons.getSecondaryBtn("Press to roll");
  }

  /**
   * Receives board game events.
   *
   * @param eventType the event that occurred
   */
  @Override
  public void onEvent(BoardGameEvent eventType) {
  }
}
