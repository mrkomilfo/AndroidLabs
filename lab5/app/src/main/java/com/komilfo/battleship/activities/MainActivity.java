package com.komilfo.battleship.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.komilfo.battleship.App;
import com.komilfo.battleship.R;
import com.komilfo.battleship.game.ClientGame;
import com.komilfo.battleship.game.Game;
import com.komilfo.battleship.game.HostGame;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import static androidx.appcompat.app.AlertDialog.*;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private ImageButton mUserImageButton;
    private Button mUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = App.getInstance().getAuth();
        Button mCreateButton = findViewById(R.id.create_button);
        Button mJoinButton = findViewById(R.id.join_button);
        Button mStatsButton = findViewById(R.id.stats_button);
        Button mExitButton = findViewById(R.id.exit_button);
        mUserButton = findViewById(R.id.user_name);
        mUserImageButton = findViewById(R.id.user_image);

        Button.OnClickListener userListener = (v) ->
                startActivity(new Intent(this, UserActivity.class));

        mUserImageButton.setOnClickListener(userListener);
        mUserButton.setOnClickListener(userListener);

        mCreateButton.setOnClickListener(v -> create());
        mJoinButton.setOnClickListener(v -> join());
        mStatsButton.setOnClickListener(v -> {
            if(mAuth.getCurrentUser() == null)
                Toast.makeText(this, "Authorization required", Toast.LENGTH_SHORT)
                        .show();
            else{
                startActivity(new Intent(this, StatsActivity.class));
            }
        });
        mExitButton.setOnClickListener(v -> exit());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUser(mAuth.getCurrentUser());
    }

    private void setUser(FirebaseUser user) {
        if (user == mUser)
            return;
        mUser = user;
        String email = user == null? getResources().getString(R.string.guest) : user.getEmail();

        mUserButton.setText(email);
        Uri uri = user == null? Uri.parse("") : user.getPhotoUrl();
        if (uri != null)
            Picasso.get().load(uri)
                    .placeholder(R.drawable.ic_person)
                    .into(mUserImageButton);
    }

    private void join() {
        Builder alertDialog = new Builder(MainActivity.this);
        alertDialog.setTitle("Join");

        final EditText input = new EditText(MainActivity.this);
        input.setHint("id");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton("OK",
                (dialog, which) -> joinById(input.getText().toString()));

        alertDialog.setNegativeButton("Cancel",
                (dialog, which) -> dialog.cancel());

        alertDialog.show();
    }

    private void joinById(String id){
        ClientGame game = new ClientGame();
        game.setOnErrorListener(this::onError);
        game.setOnStatusChanged(status -> {
            if(status == Game.Status.Connected){
                Intent intent = new Intent(getApplicationContext(), ArrangeActivity.class);
                startActivity(intent);
                game.setOnStatusChanged(null);
            } else if(status == Game.Status.OnError){
                onError("Connection error");
            }
        });

        game.joinGame(id);
        Game.setInstance(game);
    }

    private void create(){
        HostGame game = new HostGame();
        game.setOnErrorListener(this::onError);
        game.setOnStatusChanged(status -> {
            if(status == Game.Status.Created){
                Intent intent = new Intent(getApplicationContext(), ArrangeActivity.class);
                startActivity(intent);
                game.setOnStatusChanged(null);
            }
        });

        game.createGame();
        Game.setInstance(game);
    }

    private void exit(){
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    private void onError(String msg){

        Toast.makeText(this, msg, Toast.LENGTH_SHORT)
                .show();
    }
}