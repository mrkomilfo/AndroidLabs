package com.komilfo.battleship.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.komilfo.battleship.R;
import com.komilfo.battleship.interfaces.OnCellClickListener;
import com.komilfo.battleship.models.*;

import java.util.ArrayList;

import static com.komilfo.battleship.models.Grid.SIZE;

public class GameGridLayout extends GridLayout {
    public GameGridLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    public void setup(){
        setColumnCount(SIZE);
        setRowCount(SIZE);
        removeAllViews();


        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                ImageView view = new ImageView(getContext(), null, R.style.cell);
                LayoutParams lp = new GridLayout.LayoutParams();
                lp.height = 0;
                lp.width = 0;
                lp.columnSpec = GridLayout.spec(i, GridLayout.FILL, 1f);
                lp.rowSpec = GridLayout.spec(j, GridLayout.FILL, 1f);
                view.setLayoutParams(lp);

                final int x = i;
                final int y = j;
                view.setOnClickListener(v -> {
                    if(onCellClickListener != null)
                        onCellClickListener.onCellClick(x, y);
                });
                addView(view);
            }
    }

    public void setGrid(ArrayList<ArrayList<CellMask>>  mask){
        setGrid(mask, true);
    }

    public void setGrid(ArrayList<ArrayList<CellMask>>  mask, boolean showShips){
        if(mask == null)
            return;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                CellMask cellMask = mask.get(i).get(j);

                ImageView view = getCell(i, j);
                if(showShips) {
                    if (cellMask.hasDestroyedShip){
                        view.setBackgroundResource(R.drawable.ship_cell_destroyed);
                    }
                    else if (cellMask.hasShip) {
                        view.setBackgroundResource(cellMask.isHit ? R.drawable.ship_cell_damaged : R.drawable.ship_cell);
                    }
                    else {
                        view.setBackgroundResource(cellMask.isHit ? R.drawable.sea_cell_blasted : R.drawable.sea_cell);
                    }
                }
                else {
                    if (cellMask.hasDestroyedShip) {
                        view.setBackgroundResource(R.drawable.ship_cell_destroyed);
                    }
                    else if (cellMask.hasShip && cellMask.isHit) {
                        view.setBackgroundResource(R.drawable.ship_cell_damaged);
                    }
                    else {
                        view.setBackgroundResource(cellMask.isHit ? R.drawable.sea_cell_blasted : R.drawable.sea_cell);
                    }
                }
            }

    }

    public ImageView getCell(int x, int y){
        return (ImageView) getChildAt(y * SIZE + x);
    }

    private OnCellClickListener onCellClickListener;

    public void setOnCellClickListener(OnCellClickListener onCellClickListener) {
        this.onCellClickListener = onCellClickListener;
    }
}
