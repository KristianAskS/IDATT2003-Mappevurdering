package edu.ntnu.bidata.idatt.utils;

import edu.ntnu.bidata.idatt.utils.exceptions.BoardParsingException;

public class InputValidation {

  private InputValidation() {}

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
}
