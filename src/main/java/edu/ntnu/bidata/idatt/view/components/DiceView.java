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

public class DiceView implements BoardGameObserver {

  public static final int NUMB_OF_DICE = 2;
  private static final Logger logger = Logger.getLogger(DiceView.class.getName());
  private static final String DICE_IMAGE_PATH = "/edu/ntnu/bidata/idatt/images/dice/";
  private final ImageView diceImageView1;
  private final ImageView diceImageView2;
  private final Dice dice;
  private final IntegerProperty rollResult;

  public DiceView() {
    diceImageView1 = new ImageView(new Image(
        Objects.requireNonNull(getClass().getResourceAsStream(DICE_IMAGE_PATH + "1.png"))
    ));
    diceImageView1.setFitWidth(100);
    diceImageView1.setFitHeight(100);

    diceImageView2 = new ImageView(new Image(
        Objects.requireNonNull(getClass().getResourceAsStream(DICE_IMAGE_PATH + "1.png"))
    ));
    diceImageView2.setFitWidth(100);
    diceImageView2.setFitHeight(100);

    dice = new Dice(NUMB_OF_DICE);
    rollResult = new SimpleIntegerProperty(0);
  }

  public HBox getDiceImageHBox() {
    return new HBox(10, diceImageView1, diceImageView2);
  }

  public Timeline createRollDiceAnimation(Runnable onFinished) {
    Timeline rollAnimationTimeline = new Timeline();
    final int numberOfFrames = 10;
    final int frameIntervalMillis = 100;

    for (int frameIndex = 0; frameIndex < numberOfFrames; frameIndex++) {
      KeyFrame keyFrame = new KeyFrame(
          Duration.millis(frameIntervalMillis * frameIndex),
          event -> {
            int[] rollTempNumb = dice.rollDice();
            diceImageView1.setImage(new Image(
                Objects.requireNonNull(
                    getClass().getResourceAsStream(DICE_IMAGE_PATH + rollTempNumb[0] + ".png"))
            ));
            diceImageView2.setImage(new Image(
                Objects.requireNonNull(
                    getClass().getResourceAsStream(DICE_IMAGE_PATH + rollTempNumb[1] + ".png"))
            ));
          }
      );
      rollAnimationTimeline.getKeyFrames().add(keyFrame);
    }
    rollAnimationTimeline.setOnFinished(event -> {
      int[] diceRoll = dice.rollDice();
      int rollResultFinal = diceRoll[0] + diceRoll[1];
      dice.setRollResult(rollResultFinal);
      rollResult.set(rollResultFinal);
      diceImageView1.setImage(new Image(
          Objects.requireNonNull(getClass().getResourceAsStream(
              DICE_IMAGE_PATH + diceRoll[0] + ".png"
          ))
      ));
      diceImageView2.setImage(new Image(
          Objects.requireNonNull(getClass().getResourceAsStream(
              DICE_IMAGE_PATH + diceRoll[1] + ".png"
          ))
      ));
      if (onFinished != null) {
        logger.log(Level.INFO, "Dice rolled");
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

  @Override
  public void onEvent(BoardGameEvent eventType) {
    //TODO: add something here
  }
}
