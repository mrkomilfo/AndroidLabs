package com.komilfo.battleship.models;

import java.io.Serializable;

public class Cell implements Serializable {
    public Ship ship = null;
    public boolean isHit = false;
    public boolean destroyed_ship = false;
    Cell(int x, int y){
        this.x = x;
        this.y = y;
    }
    int x;
    int y;
}

