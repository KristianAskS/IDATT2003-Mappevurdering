package edu.ntnu.bidata.idatt.utils.exceptions;

/**
 * <p>Indicates an error occurring within the game’s ui layer.</p>
 *
 * @author Kristian Ask Selmer
 * @since 1.0
 */
public class GameUIException extends RuntimeException {

  /**
   * Constructs a new GameUIException with the message and cause.
   *
   * @param message a description of the UI error
   * @param cause   the exception that triggered this UI failure
   */
  public GameUIException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs a new GameUIException with a message and the cause.
   *
   * @param cause the exception that triggered this UI failure
   */
  @SuppressWarnings("Unused")
  public GameUIException(Throwable cause) {
    super("Unexpected UI error", cause);
  }
}
