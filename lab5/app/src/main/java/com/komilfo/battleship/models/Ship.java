package com.komilfo.battleship.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Ship implements Serializable {
    private List<Cell> cells;

    Ship(){
        this.cells = new ArrayList<Cell>();
    }

    Ship(List<Cell> cells){
        for(Cell cell : cells)
            cell.ship = this;

        this.cells = cells;
    }

    List<Cell> getCells(){
        return cells;
    }

    public int getSize(){
        return cells.size();
    }

    public boolean isDestroyed(){
        for (Cell cell : cells)
            if(!cell.isHit)
                return false;
        return true;
    }

    public void addAll(List<Cell> cells)
    {
        for (Cell cell : cells)
        {
            cell.ship = this;
        }
        this.cells.addAll(cells);
    }

    public void add(Cell cell)
    {
        cell.ship = this;
        this.cells.add(cell);
    }

    public ShipMask getMask(){
        ShipMask mask = new ShipMask();
        mask.x1 = cells.get(0).x;
        mask.y1 = cells.get(0).y;
        mask.x2 = cells.get(cells.size()-1).x;
        mask.y2 = cells.get(cells.size()-1).y;
        return mask;
    }
}
