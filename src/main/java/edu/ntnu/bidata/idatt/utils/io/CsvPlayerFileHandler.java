package edu.ntnu.bidata.idatt.utils.io;

import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Token;
import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;

/**
 * <p>Handles reading and writing {@link Player} data to and from CSV files.</p>
 *
 * @author Tri Tac Le
 * @version 2.2
 * @since 1.0
 */
public class CsvPlayerFileHandler implements FileHandler<Player> {
  private static final String IMG_DIR = "data/games/tokenimages";
  private final Logger logger = Logger.getLogger(CsvPlayerFileHandler.class.getName());

  /**
   * Writes the provided list of players to the CSV file at {@code filePath},
   * overwriting any existing content.
   *
   * @param players  the list of {@link Player} objects to serialize
   * @param filePath the path to the CSV file
   * @throws IOException              if an I/O error occurs during writing
   * @throws IllegalArgumentException if {@code filePath} is null or blank
   */
  @Override
  public void writeToFile(List<Player> players, String filePath) throws IOException {
    if (filePath == null || filePath.isBlank()) {
      throw new IllegalArgumentException("File path cannot be null or blank");
    }
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))) {
      for (Player player : players) {
        serializer(bufferedWriter, player);
        logger.log(Level.FINE, "Player '{0}' written to file", player.getName());
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Error writing to file: {0}", e.getMessage());
      throw e;
    }
  }

  /**
   * Reads players from the CSV file at {@code filePath}, parsing each line into
   * a {@link Player}.
   *
   * @param filePath the path to the CSV file
   * @return a list of {@link Player} instances parsed from the file
   * @throws IOException              if an I/O error occurs during reading
   * @throws IllegalArgumentException if {@code filePath} is null or blank
   */
  @Override
  public List<Player> readFromFile(String filePath) throws IOException {
    if (filePath == null || filePath.isBlank()) {
      throw new IllegalArgumentException("File path cannot be null or blank");
    }
    List<Player> players = new ArrayList<>();
    try (BufferedReader in = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = in.readLine()) != null) {
        String[] data = line.split(",", -1);
        if (data.length < 4) {
          continue;
        }

        String name = data[0].trim();
        Color color = Color.web(data[1].trim());
        String shape = data[2].trim().toLowerCase();

        LocalDate dob = null;
        if (!data[3].isBlank()) {
          try {
            dob = LocalDate.parse(data[3].trim());
          } catch (Exception e) {
            logger.log(Level.WARNING,
                "Invalid date for player {0}: {1}", new Object[] {name, data[3]});
          }
        }

        String imageRel = data.length > 4 ? data[4].trim() : "";
        String imageAbs = imageRel.isBlank() ? null : toFileUri(imageRel);

        TokenView token = new TokenView(Token.token(color, shape, imageAbs));
        players.add(new Player(name, token, dob));
      }
    }
    return players;
  }

  /**
   * Adds the provided list of players to the CSV file at {@code filePath} without
   * overriding it.
   *
   * @param players  the list of {@link Player} objects to append
   * @param filePath the path to the CSV file
   * @throws IOException              if an I/O error occurs during writing
   * @throws IllegalArgumentException if {@code filePath} is null or blank
   */
  public void appendToFile(List<Player> players, String filePath) throws IOException {
    if (filePath == null || filePath.isBlank()) {
      throw new IllegalArgumentException("File path cannot be null or blank");
    }
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {
      for (Player player : players) {
        serializer(bw, player);
        logger.log(Level.FINE, "Appended player '{0}'", player.getName());
      }
    }
  }

  /**
   * Serializes a single {@link Player} to CSV format and writes it to the {@link BufferedWriter}.
   *
   * @param bufferedWriter the writer to which the CSV line is written
   * @param player         the {@link Player} to serialize
   * @throws IOException if an I/O error occurs during writing
   */
  private void serializer(BufferedWriter bufferedWriter, Player player) throws IOException {
    TokenView token = player.getToken();
    String dob = player.getDateOfBirth() == null ? "" : player.getDateOfBirth().toString();

    String img = "";
    String path = token.getImagePath();
    if (path != null && !path.isBlank()) {
      if (path.startsWith("file:")) {
        Path p = Paths.get(URI.create(path));
        Path base = Paths.get("").toAbsolutePath().normalize();
        img = base.relativize(p).toString().replace('\\', '/');
      } else {
        img = path;
      }
    }

    String line = String.join(",",
        player.getName(),
        toRgbString(token.getTokenColor()),
        token.getTokenShape(),
        dob,
        img
    );
    bufferedWriter.write(line);
    bufferedWriter.newLine();
  }

  /**
   * Converts a {@link Color} to a 8‑digit RGBA hex string.
   *
   * @param color the {@link Color} to convert
   * @return a string in the format #RRGGBBAA
   */
  private String toRgbString(Color color) {
    int red = (int) (color.getRed() * 255);
    int green = (int) (color.getGreen() * 255);
    int blue = (int) (color.getBlue() * 255);
    int alpha = (int) (color.getOpacity() * 255);
    return String.format("#%02X%02X%02X%02X", red, green, blue, alpha);
  }

  /**
   * Converts a file path to a file URI string.
   *
   * @param csvPath the path string from the CSV data
   * @return a normalized file URI, or null if input is blank
   */
  private String toFileUri(String csvPath) {
    if (csvPath == null || csvPath.isBlank()) {
      return null;
    }
    if (csvPath.matches("^[a-zA-Z][a-zA-Z0-9+.-]*:.*")) {
      return csvPath;
    }
    Path path = Path.of(csvPath).toAbsolutePath().normalize();
    return path.toUri().toString();
  }
}
