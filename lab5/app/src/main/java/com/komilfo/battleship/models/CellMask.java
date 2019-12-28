package com.komilfo.battleship.models;

import java.io.Serializable;

public class CellMask implements Serializable {
    public boolean hasShip = false;
    public boolean hasDestroyedShip = false;
    public boolean isHit = false;
}
