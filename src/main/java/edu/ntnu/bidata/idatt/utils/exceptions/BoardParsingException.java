package edu.ntnu.bidata.idatt.utils.exceptions;

import com.google.gson.JsonSyntaxException;
import edu.ntnu.bidata.idatt.model.entity.Board;
/**
 * <p>Indicates that an error occurred while parsing a {@link Board}
 * from a JSON file.</p>
 * <p>Wraps the underlying {@link JsonSyntaxException} and includes the file path.</p>
 *
 * @author Tri Tac Le
 * @since 1.0
 */
public class BoardParsingException extends RuntimeException {

  /**
   * Constructs a new BoardParsingException.
   *
   * @param filePath the path of the JSON file that failed to parse
   * @param error    the underlying {@link JsonSyntaxException} cause
   */
  public BoardParsingException(String filePath, JsonSyntaxException error) {
    super("Error parsing board from: " + filePath, error);
  }
}
