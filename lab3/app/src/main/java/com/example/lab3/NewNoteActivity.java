package com.example.lab3;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewNoteActivity extends AppCompatActivity {

    TextView header;
    TextView content;
    TextView tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        header = (TextView)findViewById(R.id.note_header);
        content = (TextView)findViewById(R.id.note_content);
        tags = (TextView)findViewById(R.id.note_tags);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId())
        {
            case R.id.save:{
                DBHelper dbHelper = new DBHelper(this);
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();

                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");

                contentValues.put(DBHelper.KEY_HEADER, header.getText().length() > 0 ?
                        header.getText().toString(): formatter.format(date));
                contentValues.put(DBHelper.KEY_TAGS, tags.getText().toString().toLowerCase());
                contentValues.put(DBHelper.KEY_CONTENT, content.getText().toString());
                contentValues.put(DBHelper.KEY_DATE, date.getTime());
                db.insert(DBHelper.TABLE_NOTES, null, contentValues);
            }
            case R.id.delete:{

            }
            default:{
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
            }
        }
        return true;
    }

}
