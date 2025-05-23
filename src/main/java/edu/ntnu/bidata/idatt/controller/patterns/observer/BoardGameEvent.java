package edu.ntnu.bidata.idatt.controller.patterns.observer;

import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Tile;
import java.util.List;

/**
 * A record that represents a board game event
 */
public record BoardGameEvent(
    BoardGameEventType eventType,
    Player player,
    Tile oldTile,
    Tile newTile,
    List<Player> finalRanking
) {
}


