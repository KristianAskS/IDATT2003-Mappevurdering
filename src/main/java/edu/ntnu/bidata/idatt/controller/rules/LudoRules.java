package edu.ntnu.bidata.idatt.controller.rules;

import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;

/**
 * Ludo rules for the game.
 * Implements the GameRules interface.
 */
public final class LudoRules implements GameRules {

  private static final int TRACK_LEN = 52;
  private static final Color BLUE = Color.web("#2196F3");
  private static final Color GREEN = Color.web("#4CAF50");
  private static final Color YELLOW = Color.web("#FFEB3B");
  private static final Color RED = Color.web("#F44336");
  private static final EnumMap<Base, Integer> START = new EnumMap<>(Base.class);

  static {
    START.put(Base.BLUE, 3);
    START.put(Base.GREEN, 42);
    START.put(Base.YELLOW, 29);
    START.put(Base.RED, 16);
  }

  private final Map<String, Base> playerBase = new HashMap<>();
  private final EnumMap<Base, Integer> baseUsed = new EnumMap<>(Base.class);
  private final Map<String, Boolean> lapDone = new HashMap<>();
  private boolean extraTurn = false;

  {
    for (Base b : Base.values()) {
      baseUsed.put(b, 0);
    }
  }

  /**
   * Wraps the tile id around the track length.
   *
   * @param id the tile id to wrap
   * @return the wrapped tile id
   */
  private static int wrap(int id) {
    id %= TRACK_LEN;
    return id <= 0 ? id + TRACK_LEN : id;
  }

  /**
   * Calculates the distance between two tiles in a clockwise direction.
   *
   * @param from the starting tile id
   * @param to   the ending tile id
   * @return the distance between the two tiles
   */
  private static int distanceCW(int from, int to) {
    return from <= to ? to - from : TRACK_LEN - from + to;
  }

  /**
   * Calculates the distance between two tiles in a counter-clockwise direction.
   *
   * @param from the starting tile id
   * @param to   the ending tile id
   * @return the distance between the two tiles
   */
  private static int distanceCCW(int from, int to) {
    return from >= to ? from - to : TRACK_LEN - to + from;
  }

  /**
   * Checks if the given tile id crosses the start tile in a clockwise direction.
   *
   * @param cur   the current tile id
   * @param dist  the distance between the current tile and the start tile
   * @param start the start tile id
   * @return true if the tile crosses the start tile, false otherwise
   */
  private static boolean crossesStartCW(int cur, int dist, int start) {
    int end = cur + dist;
    return (cur < start && end >= start)
        || (end > TRACK_LEN && (end % TRACK_LEN) >= start);
  }

  /**
   * Calculates the distance between two colors.
   *
   * @param a the first color
   * @param b the second color
   * @return the distance between the two colors
   */
  private static double dist(Color a, Color b) {
    double dr = a.getRed() - b.getRed();
    double dg = a.getGreen() - b.getGreen();
    double db = a.getBlue() - b.getBlue();
    return dr * dr + dg * dg + db * db;
  }

  /**
   * Determines if the player can enter the track with the given roll.
   *
   * @param p      the player to check
   * @param rolled the rolled dice value
   * @return true if the player can enter the track, false otherwise
   */
  @Override
  public boolean canEnterTrack(Player p, int rolled) {
    return p.getCurrentTileId() > 0 || rolled == 6;
  }

  /**
   * Returns the destination tile id for the player after rolling the dice.
   *
   * @param p       the player to check
   * @param rolled  the rolled dice value
   * @param ignored the maximum tile id
   * @return the destination tile id
   */
  @Override
  public int destinationTile(Player p, int rolled, int ignored) {
    extraTurn = (rolled == 6);
    int cur = p.getCurrentTileId();

    if (cur == 0) {
      if (rolled == 6) {
        return start(p);
      }
      return -1;
    }
    if (rolled == 0) {
      return cur;
    }

    boolean cw = rolled > 0;
    int dist = Math.abs(rolled) % TRACK_LEN;

    if (cw && !lapDone.getOrDefault(p.getName(), false)
        && crossesStartCW(cur, dist, start(p))) {
      lapDone.put(p.getName(), true);
    }

    int entry = entrySquare(p);
    boolean passesEntry = cw
        ? distanceCW(cur, entry) <= dist
        : distanceCCW(cur, entry) <= dist;

    int dest;
    int stepsTaken;

    if (passesEntry && lapDone.getOrDefault(p.getName(), false)) {
      dest = entry;
      stepsTaken = cw
          ? distanceCW(cur, entry)
          : distanceCCW(cur, entry);
    } else {
      int raw = cw ? cur + dist : cur - dist;
      dest = wrap(raw);
      stepsTaken = dist;
    }

    p.setAmountOfSteps(p.getAmountOfSteps() + stepsTaken);
    return dest;
  }

  @Override
  public void onLand(Player p, Tile t) {
  }

  /**
   * Returns true if the player has an extra turn.
   *
   * @return true if the player has an extra turn, false otherwise
   */
  public boolean isExtraTurn() {
    return extraTurn;
  }

  /**
   * Returns the entry square for the player.
   *
   * @param p the player to check
   * @return the entry square for the player
   */
  public int entrySquare(Player p) {
    return wrap(start(p) - 2);
  }


  /**
   * Returns the start tile id for the player.
   *
   * @param p the player to check
   * @return the start tile id for the player
   */
  private int start(Player p) {
    return START.get(playerBase.computeIfAbsent(p.getName(), n -> pickBase(p)));
  }

  /**
   * Picks the nearest base color that matches the player's color.
   *
   * @param p the player to check
   * @return the nearest base color for the player
   */
  private Base pickBase(Player p) {
    Base b = nearestBase(p.getColor());
    for (int i = 0; i < Base.values().length; i++) {
      if (baseUsed.get(b) == 0) {
        break;
      }
      b = Base.values()[(b.ordinal() + 1) % Base.values().length];
    }
    baseUsed.put(b, baseUsed.get(b) + 1);
    return b;
  }

  /**
   * Finds the nearest base color to the given color.
   *
   * @param c the color to find the nearest base color for
   * @return the nearest base color to the given color
   */
  private Base nearestBase(Color c) {
    double b = dist(c, BLUE), g = dist(c, GREEN),
        y = dist(c, YELLOW), r = dist(c, RED);
    double min = Math.min(Math.min(b, g), Math.min(y, r));
    return min == b ? Base.BLUE
        : min == g ? Base.GREEN
        : min == y ? Base.YELLOW
        : Base.RED;
  }

  public enum Base { BLUE, GREEN, YELLOW, RED }
}