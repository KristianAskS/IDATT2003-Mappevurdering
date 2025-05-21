package edu.ntnu.bidata.idatt.model.entity;

import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.time.LocalDate;
import java.time.Period;
import javafx.scene.paint.Color;

public class Player {

  private final String name;
  private TokenView token;
  private int currentTileId;
  private LocalDate dateOfBirth;
  private Color color;

  public Player(String name, TokenView token) {
    this(name, token, null);
  }

  public Player(String name, TokenView token, LocalDate dateOfBirth) {
    this.name = name;
    this.token = token;
    this.currentTileId = 0;
    this.dateOfBirth = dateOfBirth;
    this.color = token.getTokenColor();
  }

  public String getName() {
    return name;
  }

  public TokenView getToken() {
    return token;
  }

  public void setTokenView(TokenView token) {
    this.token = token;
    this.color = token.getTokenColor();
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  public int getCurrentTileId() {
    return currentTileId;
  }

  public void setCurrentTileId(int id) {
    this.currentTileId = id;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate d) {
    this.dateOfBirth = d;
  }

  public int getAge() {
    return dateOfBirth == null ? -1
        : Period.between(dateOfBirth, LocalDate.now()).getYears();
  }

  @Override
  public String toString() {
    return name + " " + token;
  }

  public Color getColour() {
    return color;
  }
}