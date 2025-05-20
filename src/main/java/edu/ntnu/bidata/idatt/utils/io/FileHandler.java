package edu.ntnu.bidata.idatt.utils.io;

import java.io.IOException;
import java.util.List;

/*
 * Interface for handling reading and writing from/to files
 * @version 1.3
 * @since 1.0
 * @author Trile
 */
public interface FileHandler<T> {

  /**
   * Writes a list of players objects to a file
   *
   * @param filePath the path to the file
   * @throws IOException if writing to the file fails
   */
  void writeToFile(List<T> element, String filePath) throws IOException;

  /**
   * Reads a list of players objects from a file
   *
   * @param filePath the path to the file
   * @return a list of players objects
   * @throws IOException if reading from the file fails
   */
  List<T> readFromFile(String filePath) throws IOException;
}
