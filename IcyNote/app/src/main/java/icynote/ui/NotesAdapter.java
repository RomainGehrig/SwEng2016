package icynote.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import icynote.core.Note;
import icynote.core.impl.NoteData;

import static util.ArgumentChecker.requireNonNull;

/**
 * Created by DAP on 25.10.2016.
 */


// TODO can we not use store the notes in an array and let the underlying ArrayAdapter handle it ?
public class NotesAdapter extends ArrayAdapter<Note> implements View.OnCreateContextMenuListener {

    public final static String ONE_NOTE = "ch.epfl.sweng.prdia_adapterlistview.NOTE_DATA";

    private Filter filter;
    private ArrayList<Note> notes;

    public NotesAdapter(Context context, ArrayList<Note> notes) {
        super(context, 0, notes);
        this.notes = notes;
    }

    // FIXME tmp function to set the private array
    public void setNotes(ArrayList<Note> notes) {
        notes = requireNonNull(notes);
        clear();
        addAll(notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Note note = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_note, parent, false);
        }

        // Title
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(note.getTitle());
        tvTitle.setTag(position);
        // edit a note when clicking on the title
        tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                Note currentNote = (Note)getItem(position);
                // TODO & FIXME: edit a note on click
                Log.i("NotesAdapter", "Click on note !");
 //               ((MainActivity)getContext()).openEditNote(currentNote.getId());
            }
        });


        // Date
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        GregorianCalendar tmpDate = note.getLastUpdate();
        String date = "Last Update: "
                + tmpDate.get(GregorianCalendar.DATE) + "/"
                + tmpDate.get(GregorianCalendar.MONTH) + "/"
                + tmpDate.get(GregorianCalendar.YEAR);
        tvDate.setText(date);
        String content = note.getContent();
        int pos1=-1, pos2=-1;
        if(content != "") {
            pos1 = content.indexOf("\n");
            if(pos1 >= 0) {
                pos2 = content.substring(pos1+1).indexOf("\n");
                if(pos2 >=0) {
                    content = content.substring(0, pos1+pos2+2);
                }
            }
            content = content + "...";
        }


        // Content
        TextView tvContent = (TextView) convertView.findViewById(R.id.tvContent);
        tvContent.setText(content);


        // Delete button
        Button btDelete = (Button) convertView.findViewById(R.id.btDelete);
        btDelete.setTag(position);
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                Note note1 = getItem(position);

                note1.setContent("");
                notifyDataSetChanged();
            }
        });

/*
        View separator = (View) convertView.findViewById(R.id.)*/



        convertView.setOnCreateContextMenuListener(this);

        LinearLayout layout_1 = (LinearLayout)convertView.findViewById(R.id.layout_1);
        //layout_1.setBackgroundColor(Color.rgb(204, 204, 140));

        return convertView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
    }

    //filter
    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new NoteFilter(notes);
        return filter;
    }

    private class NoteFilter extends Filter {

        private ArrayList<Note> sourceNotes;

        public NoteFilter(List<Note> notes) {
            sourceNotes = new ArrayList<Note>();
            synchronized (this) {
                sourceNotes.addAll(notes);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence chars) {
            String filterSeq = chars.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (filterSeq != null && filterSeq.length() > 0) {
                ArrayList<Note> filter = new ArrayList<Note>();

                for (Note note : sourceNotes) {
                    if ((note.getTitle() + note.getContent()).toLowerCase().contains(filterSeq))
                        filter.add(note);
                }
                result.count = filter.size();
                result.values = filter;
            } else {
                // add all objects
                synchronized (this) {
                    result.values = sourceNotes;
                    result.count = sourceNotes.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // NOTE: this function is *always* called from the UI thread.
            ArrayList<Note> filtered = (ArrayList<Note>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filtered.size(); i < l; i++)
                add((Note) filtered.get(i));
            notifyDataSetInvalidated();
        }
    }

}
