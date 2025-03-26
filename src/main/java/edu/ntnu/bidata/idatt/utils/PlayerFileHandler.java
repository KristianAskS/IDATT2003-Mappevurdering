package edu.ntnu.iir.bidata.utils;

import edu.ntnu.iir.bidata.entity.Player;
import java.io.IOException;
import java.util.List;

/*
 * Interface for handling player files
 * @version 1.0
 * @since 1.0
 * @see CsvPlayerFileHandler
 * @author Trile
 */
public interface PlayerFileHandler {
  /**
   * Writes a list of players objects to a file
   *
   * @param players  a list of players objects
   * @param filePath the path to the file
   * @throws IOException if writing to the file fails
   */
  void writePlayers(List<Player> players, String filePath) throws IOException;

  /**
   * Reads a list of players objects from a file
   *
   * @param filePath the path to the file
   * @return a list of players objects
   * @throws IOException if reading from the file fails
   */
  List<Player> readPlayers(String filePath) throws IOException;
}
