package edu.ntnu.bidata.idatt.utils.exceptions;

public class GameUIException extends RuntimeException {

  public GameUIException(String message, Throwable cause) {
    super(message, cause);
  }

  public GameUIException(Throwable cause) {
    super("Unexpected UI error", cause);
  }
}
