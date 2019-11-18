package com.example.lab3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends BaseAdapter implements Filterable {

    private ArrayList<Note> originalNotes = null;
    private ArrayList<Note> filteredNotes = null;
    private LayoutInflater layoutInflater;

    public  NoteAdapter(Context context, ArrayList<Note> notes){
        this.originalNotes = notes;
        this.filteredNotes = notes;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return filteredNotes.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredNotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = layoutInflater.inflate(R.layout.list_item, parent, false);
        }
        TextView header = view.findViewById(R.id.noteTitleTextView);
        header.setText(getNote(position).getHeader());
        TextView content = view.findViewById(R.id.noteContentTextView);
        content.setText(getNote(position).getContent());
        TextView date = view.findViewById(R.id.noteDateTextView);
        date.setText(getNote(position).getDate().toString());
        return view;
    }

    private Note getNote(int position){
        return  (Note)getItem(position);
    }

    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence)
            {
                FilterResults results = new FilterResults();

                //If there's nothing to filter on, return the original data for your list
                if(charSequence == null || charSequence.length() == 0)
                {
                    results.values = originalNotes;
                    results.count = originalNotes.size();
                }
                else
                {
                    final List<Note> list = originalNotes;
                    int count = list.size();
                    final List<Note> filterResultsData = new ArrayList<Note>(count);

                    String[] words = charSequence.toString().toLowerCase().split(" ");
                    for(Note note : list)
                    {
                        for (String word: words)
                        {
                            if (note.getTags() != null && note.getTags().contains(word))
                            {
                                filterResultsData.add(note);
                                break;
                            }
                        }
                    }

                    results.values = filterResultsData;
                    results.count = filterResultsData.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults)
            {
                filteredNotes = (ArrayList<Note>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}
