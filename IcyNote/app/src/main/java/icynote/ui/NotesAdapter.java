package icynote.ui;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import icynote.core.Note;

import static util.ArgumentChecker.requireNonNull;


/**
 * @author Diana
 */
// TODO can we not use store the notes in an array and let the underlying ArrayAdapter handle it ?
public class NotesAdapter extends ArrayAdapter<Note> implements View.OnCreateContextMenuListener {

    private Filter filter;
    private ArrayList<Note> notes;
    private ArrayList<Note> checkedNotes;
    private final CanDeleteNote noteDeleter;

    public NotesAdapter(Context context, ArrayList<Note> notes, CanDeleteNote noteDeleter) {
        super(context, 0, notes);
        this.notes = notes;
        this.checkedNotes = new ArrayList<>();
        this.noteDeleter = requireNonNull(noteDeleter);
    }

    // FIXME tmp function to set the private array
    public void setNotes(ArrayList<Note> notes) {
        notes = requireNonNull(notes);
        clear();
        addAll(notes);
    }

    public void deleteNote(Note n) {
        notes.remove(n);
        checkedNotes.remove(n);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Note note = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_note, parent, false);
        }

        // Title
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(note.getTitle());
        tvTitle.setTag(position);

        View itemContent = convertView.findViewById(R.id.item_content);
        itemContent.setTag(position);
        itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                Note currentNote = (Note)getItem(position);
                ((MainActivity)getContext()).openEditNote(currentNote.getId());
            }
        });

        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkedNotes.add(note);
                } else {
                    checkedNotes.remove(note);
                }
            }
        });
        cb.setChecked(checkedNotes.contains(note));

        // Date
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        GregorianCalendar tmpDate = note.getLastUpdate();
        String date = "Last update: "
                + tmpDate.get(GregorianCalendar.DATE) + "/"
                + tmpDate.get(GregorianCalendar.MONTH) + "/"
                + tmpDate.get(GregorianCalendar.YEAR);
        tvDate.setText(date);
        String content = note.getContent();
        int pos1=-1;
        int pos2=-1;
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

        convertView.setOnCreateContextMenuListener(this);
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

    public List<Note> getCheckedNotes() {
        return new ArrayList<>(checkedNotes);
    }

    private class NoteFilter extends Filter {

        private ArrayList<Note> sourceNotes;

        public NoteFilter(List<Note> notes) {
            sourceNotes = new ArrayList<>();
            synchronized (this) {
                sourceNotes.addAll(notes);
            }
        }

        @Override
        protected FilterResults performFiltering(CharSequence chars) {
            String filterSeq = chars.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (filterSeq != null && !filterSeq.isEmpty()) {
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
