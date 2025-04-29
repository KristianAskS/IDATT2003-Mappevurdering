package edu.ntnu.bidata.idatt.model.entity;

import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.time.LocalDate;
import java.time.Period;

/**
 * The type Player.
 */
public class Player {

  private final String name;
  private TokenView token;
  private int currentTileId;
  private LocalDate dateOfBirth;

  // Backwards compatibility: TODO: might update.
  public Player(String name, TokenView token) {
    this.name = name;
    this.token = token;
    this.currentTileId = 0;
  }

  public Player(String name, TokenView token, LocalDate dateOfBirth) {
    this.name = name;
    this.token = token;
    this.currentTileId = 0;
    this.dateOfBirth = dateOfBirth;
  }


  public String getName() {
    return name;
  }

  public TokenView getToken() {
    return token;
  }

  public void setTokenView(TokenView token) {
    this.token = token;
  }

  public int getCurrentTileId() {
    return currentTileId;
  }

  public void setCurrentTileId(int currentTileId) {
    this.currentTileId = currentTileId;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public int getAge() {
    return dateOfBirth == null ? -1 : Period.between(dateOfBirth, LocalDate.now()).getYears();
  }

  @Override
  public String toString() {
    return name + " " + token.toString();
  }
}