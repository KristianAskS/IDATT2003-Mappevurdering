package edu.ntnu.bidata.idatt.model.entity;

import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.time.LocalDate;
import java.time.Period;
import javafx.scene.paint.Color;

/**
 * <p>Player in the game.</p>
 *
 * <p>Each player has:
 * <ul>
 *   <li>a name,</li>
 *   <li>a {@link TokenView},</li>
 *   <li>a current tile position on the board,</li>
 *   <li>an optional date of birth for,</li>
 *   <li>a display color,</li>
 *   <li>and a step counter for movement tracking.</li>
 * </ul>
 * </p>
 *
 * @author Kristian Ask Selmer, Tri Tac Le
 * @since 1.0
 */
public class Player {
  private final String name;
  private TokenView token;
  private int currentTileId;
  private LocalDate dateOfBirth;
  private Color color;
  private int amountOfSteps;

  /**
   * Constructs a player with name and token, without a birthdate.
   *
   * @param name  the player's name
   * @param token the {@link TokenView} representing the player's token
   */
  public Player(String name, TokenView token) {
    this(name, token, null);
  }

  /**
   * Constructs a player with name, token, and date of birth.
   * <p>Initializes position to 0 and step count to 0.</p>
   *
   * @param name        the player's name
   * @param token       the {@link TokenView} representing the player's token
   * @param dateOfBirth the player's date of birth
   */
  public Player(String name, TokenView token, LocalDate dateOfBirth) {
    this.name = name;
    this.token = token;
    this.currentTileId = 0;
    this.dateOfBirth = dateOfBirth;
    this.color = token.getTokenColor();
    this.amountOfSteps = 0;
  }

  /**
   * Returns the player's name.
   *
   * @return the name of the player
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the player's token view.
   *
   * @return the {@link TokenView} for this player
   */
  public TokenView getToken() {
    return token;
  }

  /**
   * Updates the player's token view and the display color.
   *
   * @param token the new {@link TokenView}; must be non-null
   */
  public void setTokenView(TokenView token) {
    this.token = token;
    this.color = token.getTokenColor();
  }

  /**
   * Returns the display color of the player.
   *
   * @return the {@link Color}
   */
  public Color getColor() {
    return color;
  }

  /**
   * Sets the display color
   *
   * @param color the {@link Color}
   */
  public void setColor(Color color) {
    this.color = color;
  }

  /**
   * Returns the id of the current tile occupied by the player.
   *
   * @return the current tile ID
   */
  public int getCurrentTileId() {
    return currentTileId;
  }

  /**
   * Updates the player's current tile position.
   *
   * @param id the new tile id to set
   */
  public void setCurrentTileId(int id) {
    this.currentTileId = id;
  }

  /**
   * Returns the player's date of birth.
   *
   * @return the {@link LocalDate} of birth
   */
  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  /**
   * Updates the player's date of birth.
   *
   * @param dateOfBirth the new {@link LocalDate} of birth
   */
  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  /**
   * Calculates the player's age in full years based on the date of birth.
   *
   * @return the age in years, or -1 if dateOfBirth is null
   */
  public int getAge() {
    return (dateOfBirth == null)
        ? -1
        : Period.between(dateOfBirth, LocalDate.now()).getYears();
  }

  /**
   * Returns a string representation combining name and token.
   *
   * @return a string in the format "name token"
   */
  @Override
  public String toString() {
    return name + " " + token;
  }

  /**
   * Returns the total number of steps the player has moved.
   *
   * @return the step count
   */
  public int getAmountOfSteps() {
    return amountOfSteps;
  }

  /**
   * Updates the total number of steps the player has moved.
   *
   * @param amountOfSteps the new step count
   */
  public void setAmountOfSteps(int amountOfSteps) {
    this.amountOfSteps = amountOfSteps;
  }
}
