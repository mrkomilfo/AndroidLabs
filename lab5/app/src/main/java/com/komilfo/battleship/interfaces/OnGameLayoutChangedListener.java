package com.komilfo.battleship.interfaces;

import com.komilfo.battleship.models.CellMask;
import java.util.ArrayList;

public interface OnGameLayoutChangedListener {
    void onGameLayoutChanged(ArrayList<ArrayList<CellMask>> userMask
            , ArrayList<ArrayList<CellMask>> opponentMask);
}
