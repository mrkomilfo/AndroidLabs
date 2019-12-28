package com.komilfo.battleship.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

import com.komilfo.battleship.R;
import com.komilfo.battleship.game.Game;
import com.komilfo.battleship.layouts.GameGridLayout;
import com.komilfo.battleship.models.Grid;

public class ArrangeActivity extends AppCompatActivity {
    private CheckBox mReadyButton;
    private TextView[] mCounters;
    private GameGridLayout mGameGridLayout;
    private Grid mGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrange);
        Game mGame = Game.getInstance();
        mReadyButton = findViewById(R.id.ready_button);
        mReadyButton.setOnCheckedChangeListener((v, isChecked) -> {
            mGame.setReady(isChecked);
            if(isChecked){
                mGame.setUserGrid(mGrid);
                mGame.start();
            }
        });

        mCounters = new TextView[4];
        mCounters[0] = findViewById(R.id.x4_counter);
        mCounters[1] = findViewById(R.id.x3_counter);
        mCounters[2] = findViewById(R.id.x2_counter);
        mCounters[3] = findViewById(R.id.x1_counter);

        mGrid = new Grid();
        mGameGridLayout = findViewById(R.id.my_grid);
        mGameGridLayout.setOnCellClickListener((x, y)->{
            mGrid.cellClick(x, y);
            updateLayout();
        });

        ((TextView)findViewById(R.id.game_id))
                .setText(String.format("id: %s", Game.getInstance().getId()));

        findViewById(R.id.clear).setOnClickListener(v -> {
            mGrid.clearShips();
            updateLayout();
        });

        ProgressDialog pd = new ProgressDialog(this);
        if (mGame.getStatus() != Game.Status.Connected)
        {
            pd.setTitle(String.format("id: %s", Game.getInstance().getId()));
            pd.setMessage("Waiting for opponent");
            pd.setCancelable(false);
            pd.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            pd.show();
        }

        mGame.setOnStatusChanged(status -> {
            if(status == Game.Status.HostTurn || status == Game.Status.ClientTurn){
                mGame.setOnStatusChanged(null);
                startActivity(new Intent(getApplicationContext(), GameActivity.class));
                finish();
            }
            else if (status == Game.Status.Connected)
            {
                pd.dismiss();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLayout();
    }

    private void updateLayout(){
        setCounters();
        mGameGridLayout.setGrid(mGrid.getMask());
        if(mGrid.shipsArePrepared()){
            mReadyButton.setEnabled(true);
        }
        else {
            mReadyButton.setEnabled(false);
            mReadyButton.setChecked(false);
            if(Game.getInstance().isReady())
                Game.getInstance().setReady(false);
        }
    }

    private void setCounters(){
        for(int i = 1; i < 5; i++)
            mCounters[i-1].setText(String.valueOf(mGrid.getAvailableShips(i)));
    }
}
