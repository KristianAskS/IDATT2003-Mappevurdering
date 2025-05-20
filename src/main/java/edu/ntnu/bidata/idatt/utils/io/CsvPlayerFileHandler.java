package edu.ntnu.bidata.idatt.utils.io;

import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Token;
import edu.ntnu.bidata.idatt.view.components.TokenView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;

/**
 * Class for handling CSV-files containing players
 *
 * @author Trile
 * @version 1.0
 * @since 1.0
 */
public class CsvPlayerFileHandler implements FileHandler<Player> {

  private static final String IMG_DIR = "data/games/tokenimages";
  Logger logger = Logger.getLogger(CsvPlayerFileHandler.class.getName());

  /**
   * @param players  List over players
   * @param filePath the path to the CSV-file
   * @throws IOException if writing to the file fails
   */
  @Override
  public void writeToFile(List<Player> players, String filePath) throws IOException {
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath))) {
      for (Player player : players) {
        TokenView token = player.getToken();

        String dob = player.getDateOfBirth() == null ? "" : player.getDateOfBirth().toString();

        String writeLine = String.join(",",
            player.getName(),
            toRgbString(token.getTokenColor()),
            token.getTokenShape(),
            dob,
            token.getImagePath() == null ? "" : token.getImagePath()
        );

        bufferedWriter.write(writeLine);
        bufferedWriter.newLine();
        logger.log(Level.FINE, "Player: " + player.getName() + " has been written to the file");
      }
    } catch (IOException e) {
      logger.log(Level.SEVERE, "Error writing to the file: " + e.getMessage());
    }
  }

  /**
   * Reads a list of players objects from a CSV-file
   *
   * @param filePath the path to the CSV-file
   * @return a list of players objects
   * @throws IOException if reading from the file fails
   */
  @Override
  public List<Player> readFromFile(String filePath) throws IOException {
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
                "Invalid date for player {0}: {1}", new Object[]{name, data[3]});
          }
        }

        String imageRel = data.length > 4 ? data[4].trim() : "";
        String imageAbs = imageRel.isBlank() ? null : toFileUri(imageRel);

        // String img = playerData.length > 3 ? playerData[3].trim() : null;

        TokenView token = new TokenView(Token.token(color, shape, imageAbs));
        players.add(new Player(name, token, dob));
      }
    }
    return players;
  }

  private String toRgbString(Color color) {
    int red = (int) (color.getRed() * 255);
    int green = (int) (color.getGreen() * 255);
    int blue = (int) (color.getBlue() * 255);
    int alpha = (int) (color.getOpacity() * 255);
    return String.format("#%02X%02X%02X%02X", red, green, blue, alpha);
  }

  private String toFileUri(String csvPath) {
    if (csvPath.matches("^[a-zA-Z][a-zA-Z0-9+.-]*:.*")) {
      return csvPath;
    }
    Path p = Paths.get(csvPath);
    if (!p.isAbsolute()) {
      p = Paths.get("").toAbsolutePath().resolve(p).normalize();
    }
    return p.toUri().toString();
  }
}
