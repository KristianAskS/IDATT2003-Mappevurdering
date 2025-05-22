package edu.ntnu.bidata.idatt.utils.exceptions;

/**
 * <p>Indicates that an operation was attempted in an invalid or unexpected
 * game state.</p>
 *
 * @author Kristian Ask Selmer
 * @since 1.0
 */
public class InvalidGameStateException extends RuntimeException {

  /**
   * Constructs a new InvalidGameStateException with the message.
   *
   * @param message a description of the invalid game state condition
   */
  public InvalidGameStateException(String message) {
    super(message);
  }
}
