package com.komilfo.battleship.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.komilfo.battleship.R;
import com.komilfo.battleship.game.Game;
import com.komilfo.battleship.layouts.GameGridLayout;
import com.komilfo.battleship.models.CellMask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GameActivity extends AppCompatActivity {
    private GameGridLayout mUserGrid;
    private GameGridLayout mOpponentGrid;
    private TextView mStatusView;
    private Game mGame;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mStatusView = findViewById(R.id.status_text);
        mUserGrid = findViewById(R.id.my_grid);
        mOpponentGrid = findViewById(R.id.opponent_grid);

        mUserGrid.setEnabled(false);
        mOpponentGrid.setOnCellClickListener((x, y) -> {
            CellMask mask = mGame.getOpponentCells().get(y).get(x);
            if(mask.isHit)
                return;
            if(mGame.isMyTurn()) {
                mOpponentGrid.setEnabled(false);
                mGame.hitOpponent(x, y);

                mask = mGame.getOpponentCells().get(y).get(x);
                ImageView cellView = mOpponentGrid.getCell(y, x);
                if (mask.hasDestroyedShip){
                    cellView.setBackgroundResource(R.drawable.ship_cell_destroyed);
                }
                else if (mask.hasShip) {
                    cellView.setBackgroundResource(R.drawable.ship_cell_damaged);
                }
                else {
                    cellView.setImageResource(R.drawable.sea_cell_blasted);
                }
            }
         });

        mGame = Game.getInstance();
        mGame.setLayoutChangedListener(this::refresh);

        mGame.setOnStatusChanged((status) -> {
            if(mGame.isMyTurn()) {
                mStatusView.setText("Your turn");
                mOpponentGrid.setEnabled(true);
            }
            else if(mGame.isOpponentTurn()){
                mStatusView.setText("Opponent's turn");
                mOpponentGrid.setEnabled(false);
            }
            else {
                mStatusView.setText("...");
                mOpponentGrid.setEnabled(false);
            }
        });

        mGame.setOnFinishedListener(won -> {
            TextView textView = findViewById(R.id.endgame_text);
            String result;
            if(won){
                result = "VICTORY!";
            }
            else{
                result = "DEFEAT!";
            }

            textView.setText(result);
            textView.setVisibility(View.VISIBLE);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user == null)
                return;
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");
            FirebaseDatabase.getInstance()
                    .getReference("users/" + user.getUid() + "/history")
                    .push().setValue(mGame.getId() + "| "
                    + formatter.format(date) + " "
                    + result);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh(mGame.getUserCells(), mGame.getOpponentCells());
        mGame.getOnStatusChanged().onStatusChanged(mGame.getStatus());
    }

    private void refresh(ArrayList<ArrayList<CellMask>> userMask, ArrayList<ArrayList<CellMask>> opponentMask){
        mUserGrid.setGrid(userMask);
        mOpponentGrid.setGrid(opponentMask, false);
    }
}
