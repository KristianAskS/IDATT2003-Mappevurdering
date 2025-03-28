package edu.ntnu.bidata.idatt.utils;

public class InputValidation {

  private InputValidation() {
  }

  public static void validateString(String input) {
    if (input == null || input.isBlank()) {
      throw new IllegalArgumentException("Input cannot be null or empty");
    }
  }

  public static void validateInt(int input) {
    if (input < 0) {
      throw new IllegalArgumentException("Input cannot be negative");
    }
  }

  public static void validateSetDiceValue(int input) {
    if (input < 1 || input > 6) {
      throw new IllegalArgumentException("Dice number input must be between 1 and 6");
    }
  }
}
