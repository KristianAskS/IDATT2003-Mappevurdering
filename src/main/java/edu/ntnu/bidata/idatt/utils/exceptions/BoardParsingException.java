package edu.ntnu.bidata.idatt.utils.exceptions;

import com.google.gson.JsonSyntaxException;

/**
 * A domain spesific exception shows error when parsing a board from a JSON file
 */
public class BoardParsingException extends RuntimeException {

  public BoardParsingException(String filePath, JsonSyntaxException error) {
    super("Error parsing board from: " + filePath, error);
  }
}
