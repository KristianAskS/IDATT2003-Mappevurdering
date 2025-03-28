package edu.ntnu.bidata.idatt.patterns.observer;

import edu.ntnu.bidata.idatt.entity.Player;
import edu.ntnu.bidata.idatt.model.Tile;

public record BoardGameEvent(BoardGameEventType eventType, Player player, Tile oldTile,
                             Tile newTile) {
}
