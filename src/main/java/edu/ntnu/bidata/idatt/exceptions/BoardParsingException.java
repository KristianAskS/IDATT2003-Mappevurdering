package edu.ntnu.bidata.idatt.exceptions;

/**
 * A domain spesific exception shows error when parsing a board from a JSON file
 */
public class BoardParsingException extends RuntimeException {
  public BoardParsingException(String message) {
    super(message);
  }

  public BoardParsingException(String message, Throwable cause) {
    super(message, cause);
  }
}
