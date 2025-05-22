package edu.ntnu.bidata.idatt.controller.rules;

import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.paint.Color;

public final class LudoRules implements GameRules {

  private static final int TRACK_LEN = 52;
  private static final int HOME_LEN = 5;
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

  private static int wrap(int id) {
    id %= TRACK_LEN;
    return id <= 0 ? id + TRACK_LEN : id;
  }

  private static int distanceCW(int from, int to) {
    return from <= to ? to - from : TRACK_LEN - from + to;
  }

  private static int distanceCCW(int from, int to) {
    return from >= to ? from - to : TRACK_LEN - to + from;
  }

  private static boolean crossesStartCW(int cur, int dist, int start) {
    int end = cur + dist;
    return (cur < start && end >= start)
        || (end > TRACK_LEN && (end % TRACK_LEN) >= start);
  }

  private static double dist(Color a, Color b) {
    double dr = a.getRed() - b.getRed();
    double dg = a.getGreen() - b.getGreen();
    double db = a.getBlue() - b.getBlue();
    return dr * dr + dg * dg + db * db;
  }

  @Override
  public boolean canEnterTrack(Player p, int rolled) {
    return p.getCurrentTileId() > 0 || rolled == 6;
  }

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

  public boolean isExtraTurn() {
    return extraTurn;
  }

  public int entrySquare(Player p) {
    return wrap(start(p) - 2);
  }

  public int finishEntry(Player p) {
    return entrySquare(p);
  }

  private int start(Player p) {
    return START.get(playerBase.computeIfAbsent(p.getName(), n -> pickBase(p)));
  }

  private Base pickBase(Player p) {
    Base b = nearestBase(p.getColour());
    for (int i = 0; i < Base.values().length; i++) {
      if (baseUsed.get(b) == 0) {
        break;
      }
      b = Base.values()[(b.ordinal() + 1) % Base.values().length];
    }
    baseUsed.put(b, baseUsed.get(b) + 1);
    return b;
  }

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