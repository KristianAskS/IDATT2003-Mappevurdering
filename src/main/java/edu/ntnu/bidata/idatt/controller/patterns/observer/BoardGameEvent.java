package edu.ntnu.bidata.idatt.controller.patterns.observer;

import edu.ntnu.bidata.idatt.model.entity.Player;
import edu.ntnu.bidata.idatt.model.entity.Tile;

public record BoardGameEvent(BoardGameEventType eventType, Player player, Tile oldTile,
                             Tile newTile) {

}
