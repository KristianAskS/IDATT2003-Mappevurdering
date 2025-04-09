package edu.ntnu.bidata.idatt.view.components;

import edu.ntnu.bidata.idatt.controller.patterns.observer.BoardGameEvent;
import edu.ntnu.bidata.idatt.controller.patterns.observer.interfaces.BoardGameObserver;
import edu.ntnu.bidata.idatt.model.entity.Dice;
import java.util.Objects;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class DiceView implements BoardGameObserver {

  private static final String DICE_IMAGE_PATH = "/edu/ntnu/bidata/idatt/images/dice/";
  private final ImageView diceImageView;
  private final Dice dice;
  private final IntegerProperty rollResult;

  public DiceView() {
    diceImageView = new ImageView(new Image(
        Objects.requireNonNull(getClass().getResourceAsStream(DICE_IMAGE_PATH + "1.png"))
    ));
    diceImageView.setFitWidth(100);
    diceImageView.setFitHeight(100);

    dice = new Dice(1);
    rollResult = new SimpleIntegerProperty(0);
  }

  public Timeline createRollDiceAnimation(Runnable onFinished) {
    Timeline rollAnimationTimeline = new Timeline();
    final int numberOfFrames = 10;
    final int frameIntervalMillis = 100;

    for (int frameIndex = 0; frameIndex < numberOfFrames; frameIndex++) {
      KeyFrame keyFrame = new KeyFrame(
          Duration.millis(frameIntervalMillis * frameIndex),
          event -> {
            int result = dice.roll();
            dice.setRollResult(result);
            rollResult.set(result);
            String imagePath = DICE_IMAGE_PATH + result + ".png";
            diceImageView.setImage(new Image(
                Objects.requireNonNull(getClass().getResourceAsStream(imagePath))
            ));
          }
      );
      rollAnimationTimeline.getKeyFrames().add(keyFrame);
    }
    rollAnimationTimeline.setOnFinished(event -> {
      if (onFinished != null) {
        onFinished.run();
      }
    });
    return rollAnimationTimeline;
  }

  public IntegerProperty rollResultProperty() {
    return rollResult;
  }

  public Button getRollDiceBtn() {
    return Buttons.getSecondaryBtn("Press to roll");
  }

  public ImageView getDiceImageView() {
    return diceImageView;
  }

  @Override
  public void onEvent(BoardGameEvent eventType) {
    //TODO: add something here
  }
}
