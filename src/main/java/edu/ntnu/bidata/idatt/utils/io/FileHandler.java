package edu.ntnu.bidata.idatt.utils.io;

import java.io.IOException;
import java.util.List;

/**
 * <p>Defines a generic handler for serializing and deserializing lists of objects
 * to and from files.</p>
 *
 * @param <T> the type of objects to be read from or written to the file
 * @author Tri Tac Le
 * @version 1.3
 * @since 1.0
 */
public interface FileHandler<T> {

  /**
   * Writes the given list of objects to the specified file.
   *
   * @param element  the list of objects to write
   * @param filePath the path to the file
   * @throws IOException if an I/O error occurs during writing
   */
  void writeToFile(List<T> element, String filePath) throws IOException;

  /**
   * Reads a list of objects from the specified file.
   *
   * @param filePath the path to the source file
   * @return a list of objects read from the file
   * @throws IOException if an I/O error occurs during reading
   */
  List<T> readFromFile(String filePath) throws IOException;
}
