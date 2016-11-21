package icynote.ui.utils;

import android.content.Context;
import android.text.SpannableString;
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

import icynote.note.Note;
import icynote.ui.MainActivity;
import icynote.ui.R;

import static util.ArgumentChecker.requireNonNull;


/**
 * @author Diana
 */
// TODO can we not use store the notes in an array and let the underlying ArrayAdapter handle it ?
public class NotesAdapter extends ArrayAdapter<Note<SpannableString>> implements View.OnCreateContextMenuListener {

    private Filter filter;
    private ArrayList<Note<SpannableString>> notes;
    private ArrayList<Note<SpannableString>> checkedNotes;
    private final CanDeleteNote noteDeleter;

    public NotesAdapter(Context context, ArrayList<Note<SpannableString>> notes, CanDeleteNote noteDeleter) {
        super(context, 0, notes);
        this.notes = notes;
        this.checkedNotes = new ArrayList<>();
        this.noteDeleter = requireNonNull(noteDeleter);
    }

    // FIXME tmp function to set the private array
    public void setNotes(ArrayList<Note<SpannableString>> notes) {
        notes = requireNonNull(notes);
        clear();
        addAll(notes);
    }

    public void deleteNote(Note<SpannableString> n) {
        notes.remove(n);
        checkedNotes.remove(n);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Note<SpannableString> note = getItem(position);
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
                Note<SpannableString> currentNote = (Note<SpannableString>) getItem(position);
                ((MainActivity) getContext()).openEditNote(currentNote.getId());
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
        SpannableString content = note.getContent();
        int pos1 = -1;
        int pos2 = -1;

        String strContent = "";
        int maxLength = 50;
        if (content.length() > maxLength) {
            strContent = content.subSequence(0, maxLength).toString() + "...";
        } else {
            strContent = content.toString();
        }

        strContent = strContent.replace('\n', ' ');

        // Content
        TextView tvContent = (TextView) convertView.findViewById(R.id.tvContent);
        tvContent.setText(strContent);

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

    public List<Note<SpannableString>> getCheckedNotes() {
        return new ArrayList<>(checkedNotes);
    }

    private class NoteFilter extends Filter {

        private ArrayList<Note<SpannableString>> sourceNotes;

        public NoteFilter(List<Note<SpannableString>> notes) {
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
                ArrayList<Note<SpannableString>> filter = new ArrayList<Note<SpannableString>>();

                for (Note<SpannableString> note : sourceNotes) {
                    if (note.getTitle().toString().toLowerCase().contains(filterSeq)
                            || note.getContent().toString().toLowerCase().contains(filterSeq)) {
                        filter.add(note);
                    }
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
            ArrayList<Note<SpannableString>> filtered = (ArrayList<Note<SpannableString>>) results.values;
            notifyDataSetChanged();
            clear();
            for (int i = 0, l = filtered.size(); i < l; i++)
                add((Note<SpannableString>) filtered.get(i));
            notifyDataSetInvalidated();
        }
    }

}
