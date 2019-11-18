package com.example.lab3;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView list;
    private GridView grid;
    private ArrayList<Note> notes;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadNotes();
        final NoteAdapter adapter = new NoteAdapter(this, notes);

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



    /*@Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView)searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }*/


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loadNotes()
    {
        notes = new ArrayList<Note>(){{
            add(new Note(1,"Header 1", "Note 1"));
            add(new Note(2,"Header 2", "Note 2"));
            add(new Note(3,"Header 3", "Note 3"));
            add(new Note(4,"Header 4", "Note 4"));
            add(new Note(5,"Header 5", "Note 5"));
            add(new Note(6,"Header 6", "Note 6"));
        }};
        notes.get(0).setTags(new ArrayList<String>(){{add("a"); add("g");}});
        notes.get(1).setTags(new ArrayList<String>(){{add("a"); add("b");}});
        notes.get(2).setTags(new ArrayList<String>(){{add("b"); add("c");}});
    }
}
