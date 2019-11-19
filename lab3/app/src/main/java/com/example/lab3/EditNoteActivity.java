package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditNoteActivity extends AppCompatActivity {

    private int id;
    EditText header;
    EditText content;
    EditText tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        header = (EditText)findViewById(R.id.note_header);
        tags = (EditText)findViewById(R.id.note_tags);
        content = (EditText)findViewById(R.id.note_content);

        Bundle arguments = getIntent().getExtras();

        if(arguments != null){
            id = arguments.getInt("id");
            header.setText(arguments.getString("header"));
            tags.setText(arguments.getString("tags"));
            content.setText(arguments.getString("content"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (item.getItemId())
        {
            case R.id.save:{
                ContentValues contentValues = new ContentValues();

                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy HH:mm:ss");

                contentValues.put(DBHelper.KEY_HEADER, header.getText().length() > 0 ?
                        header.getText().toString(): formatter.format(date));
                contentValues.put(DBHelper.KEY_TAGS, tags.getText().toString().toLowerCase());
                contentValues.put(DBHelper.KEY_CONTENT, content.getText().toString());
                db.update(DBHelper.TABLE_NOTES, contentValues, DBHelper.KEY_ID + "=" + id, null);
                break;
            }
            case R.id.delete:{
                db.delete(dbHelper.TABLE_NOTES, DBHelper.KEY_ID + "=" + id, null);
            }
        }
        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
        return true;
    }
}
