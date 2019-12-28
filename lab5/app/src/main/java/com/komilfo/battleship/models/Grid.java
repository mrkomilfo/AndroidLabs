package com.komilfo.battleship.models;


import com.komilfo.battleship.interfaces.OnGridChangeListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Grid implements Serializable {
    public static final int SIZE = 10;

    private Cell[][] cells;
    private List<Ship> ships;
    private int[] ship_count = new int[]{0, 0, 0, 0, 0};
    private static final int[] SHIP_MAX_COUNT = new int[]{10, 4, 3, 2, 1};
    private int destroyed_ships = 0;

    public Grid(){
        ships = new ArrayList<>();
        cells = new Cell[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                cells[i][j] = new Cell(j, i);
            }
    }

    public boolean hit(int x, int y){
        Cell cell = cells[y][x];

        if(cell.isHit)
            return false;

        cell.isHit = true;
        if(cell.ship == null)
            return false;

        if(cell.ship.isDestroyed()){
            destroyed_ships++;
            List<Cell> ship_cells = cell.ship.getCells();

            int x1 = ship_cells.get(0).x;
            int y1 = ship_cells.get(0).y;
            int x2 = ship_cells.get(ship_cells.size()-1).x;
            int y2 = ship_cells.get(ship_cells.size()-1).y;

            int x_left = x1 == 0 ? 0 : x1 - 1;
            int x_right = x2 == SIZE - 1 ? SIZE - 1 : x2 + 1;
            int y_top = y1 == 0 ? 0 : y1 - 1;
            int y_bottom = y2 == SIZE - 1 ? SIZE - 1 : y2 + 1;

            for(int i = y_top; i <= y_bottom; i++)
                for(int j = x_left; j <= x_right; j++) {
                    cells[i][j].isHit = true;
                    if (cells[i][j].ship != null)
                    {
                        cells[i][j].destroyed_ship = true;
                    }
                }
        }
        return true;
    }

    public void cellClick(int x, int y) {
        Cell cell = cells[y][x];
        if (cell.ship == null) {
            if (x-1 >= 0){
                if (y-1 >= 0 && cells[y-1][x-1].ship != null){
                    return;
                }
                else if (y+1 < SIZE && cells[y+1][x-1].ship != null){
                    return;
                }
            }
            if (x+1 < SIZE){
                if (y-1 >= 0 && cells[y-1][x+1].ship != null){
                    return;
                }
                else if (y+1 <= SIZE-1 && cells[y+1][x+1].ship != null){
                    return;
                }
            }
            Ship ship1 = null;
            Ship ship2 = null;
            if (x-1 >= 0 && cells[y][x-1].ship != null || x+1 < SIZE && cells[y][x+1].ship != null)
            {
                if (x-1 >= 0 && cells[y][x-1].ship != null){
                    ship1 = cells[y][x-1].ship;
                }
                if (x+1 < SIZE && cells[y][x+1].ship != null)
                {
                    ship2 = cells[y][x+1].ship;
                }
            }
            else if (y-1 >= 0 && cells[y-1][x].ship != null || y+1 < SIZE && cells[y+1][x].ship != null){
                if (y-1 >= 0 && cells[y-1][x].ship != null){
                    ship1 = cells[y-1][x].ship;
                }
                if (y+1 < SIZE && cells[y+1][x].ship != null)
                {
                    ship2 = cells[y+1][x].ship;
                }
            }
            if ((ship1 == null ? 0 : ship1.getSize()) + (ship2 == null ? 0 : ship2.getSize()) >= 4){
                return;
            }
            Ship newShip = new Ship();
            if (ship1 != null) {
                newShip.addAll(ship1.getCells());
                ships.remove(ship1);
            }
            newShip.add(cell);
            if (ship2 != null) {
                newShip.addAll(ship2.getCells());
                ships.remove(ship2);
            }
            ships.add(newShip);
        }
        else{
            Ship targetShip = cell.ship;
            if (x-1 >= 0 && cells[y][x-1].ship != null && x+1 < SIZE && cells[y][x+1].ship != null){
                List<Cell> firstShipCells = targetShip.getCells().subList(0, targetShip.getCells().indexOf(cells[y][x]));
                List<Cell> secondShipCells = targetShip.getCells().subList(targetShip.getCells().indexOf(cells[y][x+1]), targetShip.getSize());
                ships.add(new Ship(firstShipCells));
                ships.add(new Ship(secondShipCells));
                ships.remove(targetShip);
            }
            else if (y-1 >= 0 && cells[y-1][x].ship != null && y+1 < SIZE && cells[y+1][x].ship != null){
                List<Cell> firstShipCells = targetShip.getCells().subList(0, targetShip.getCells().indexOf(cells[y][x]));
                List<Cell> secondShipCells = targetShip.getCells().subList(targetShip.getCells().indexOf(cells[y+1][x]), targetShip.getSize());
                ships.add(new Ship(firstShipCells));
                ships.add(new Ship(secondShipCells));
                ships.remove(targetShip);
            }
            else if (targetShip.getSize() == 1){
                ships.remove(targetShip);
            }
            else{
                targetShip.getCells().remove(cell);
            }
            cell.ship = null;
        }
        updateCounters();
    }

    public void putShip(ShipMask mask){
        putShip(mask.x1, mask.y1, mask.x2, mask.y2);
    }

    private void putShip(int x1, int y1, int x2, int y2){
        List<Cell> ship_cells = new ArrayList<Cell>();
        if (x1 == x2)
        {
            for (int y = y1; y <= y2; y++){
                ship_cells.add(cells[y][x1]);
            }
        }
        else if (y1 == y2){
            ship_cells.addAll(Arrays.asList(cells[y1]).subList(x1, x2 + 1));
        }
        ships.add(new Ship(ship_cells));
    }

    public void clearShips(){
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                cells[i][j].ship = null;
            }
        ship_count = new int[]{0, 0, 0, 0, 0};
        ships.clear();
    }

    public int getRemainingShips(){
        return 10 - destroyed_ships;
    }

    private void updateCounters(){
        ship_count = new int[]{0, 0, 0, 0, 0};
        for(Ship ship: ships){
            ship_count[ship.getSize()]++;
        }
    }

    public boolean shipsArePrepared() {
        for (int i = 1; i < 5; i++) {
            if (ship_count[i] != 5-i){
                return false;
            }
        }
        return true;
    }

    public int getAvailableShips(int size){
        return SHIP_MAX_COUNT[size] - ship_count[size];
    }


    public ArrayList<ArrayList<CellMask>>  getMask(){
        return getMask(true);
    }

    public ArrayList<ArrayList<CellMask>>  getMask(boolean includeShips){
        ArrayList<ArrayList<CellMask>> gridMask = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; i++) {
            gridMask.add(new ArrayList<>(SIZE));
            for (int j = 0; j < SIZE; j++) {
                CellMask mask = new CellMask();
                mask.hasShip = includeShips && cells[i][j].ship != null;
                mask.hasDestroyedShip = cells[i][j].destroyed_ship;
                mask.isHit = cells[i][j].isHit;
                gridMask.get(i).add(mask);
            }
        }
        return gridMask;
    }

    public List<ShipMask> getShipMasks(){
        List<ShipMask> masks = new ArrayList<>();
        for(Ship ship : ships)
            masks.add(ship.getMask());
        return masks;
    }

    private OnGridChangeListener onGridChangeListener;

    public OnGridChangeListener getOnGridChangeListener() {
        return onGridChangeListener;
    }

    public void setOnGridChangeListener(OnGridChangeListener onGridChangeListener) {
        this.onGridChangeListener = onGridChangeListener;
    }
}
