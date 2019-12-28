package com.komilfo.battleship.interfaces;

import com.komilfo.battleship.game.Game;

public interface OnStatusChangedListener {
    void onStatusChanged(Game.Status status);
}
