package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private ListView list;
    private GridView grid;
    private ArrayList<Note> notes;
    private DBHelper dbHelper;
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadDB();
        adapter = new NoteAdapter(this, notes);

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            list = (ListView) findViewById(R.id.notesListView);
            list.setAdapter(adapter);
            list.setOnItemClickListener(
                    new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                            Note note = notes.get(i);
                            Intent intent = new Intent(getApplication(), EditNoteActivity.class);
                            intent.putExtra("id", note.getId());
                            intent.putExtra("header", note.getHeader());
                            intent.putExtra("tags", note.tagsInString());
                            intent.putExtra("content", note.getContent());
                            startActivity(intent);

                        }
                    });
        }
        else{
            grid = (GridView) findViewById(R.id.notesGridView);
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(
                    new AdapterView.OnItemClickListener(){
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                            Note note = notes.get(i);
                            Intent intent = new Intent(getApplication(), EditNoteActivity.class);
                            intent.putExtra("id", note.getId());
                            intent.putExtra("header", note.getHeader());
                            intent.putExtra("content", note.getContent());
                            intent.putExtra("tags", note.tagsInString());
                            startActivity(intent);

                        }
                    });
        }

        EditText search = (EditText)findViewById(R.id.searchEditText);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplication(), NewNoteActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId())
        {
            case R.id.header_sort:{
                Collections.sort(notes);
                adapter.notifyDataSetChanged();
                break;
            }
            case R.id.date_sort:{
                Collections.sort(notes, new Comparator<Note>() {
                    @Override
                    public int compare(Note n1, Note n2) {
                        return (int)(n1.getDate().getTime() - n2.getDate().getTime());
                    }
                });
                adapter.notifyDataSetChanged();
                break;
            }
        }
        return true;
    }

    public void loadDB()
    {
        dbHelper = new DBHelper(this);
        notes = new ArrayList<Note>();
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_NOTES, null, null, null, null, null, null);
        if (cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int headerIndex = cursor.getColumnIndex(DBHelper.KEY_HEADER);
            int tagsIndex = cursor.getColumnIndex(DBHelper.KEY_TAGS);
            int contentIndex = cursor.getColumnIndex(DBHelper.KEY_CONTENT);
            int timeIndex = cursor.getColumnIndex(DBHelper.KEY_DATE);
            do{
                notes.add(new Note(cursor.getInt(idIndex), cursor.getString(headerIndex),
                        cursor.getString(contentIndex), cursor.getString(tagsIndex),
                        new Date(cursor.getLong(timeIndex))));

            } while(cursor.moveToNext());
        }
        cursor.close();
    }
}
