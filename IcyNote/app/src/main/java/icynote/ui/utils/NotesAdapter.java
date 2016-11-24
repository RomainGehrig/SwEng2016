package icynote.ui.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.GregorianCalendar;

import icynote.note.Note;
import icynote.ui.R;


/**
 * @author Julien Harbulot
 * @author Diana
 * @version 2.0
 */

public class NotesAdapter extends ArrayAdapter<NotesAdapter.Bucket> {
    private Context context;
    private BucketClickedListener onClickListener;

    public static class Bucket {
        public Note<SpannableString> note;
        public boolean checked = false;
        public boolean enabled = true;
        public Bucket(Note<SpannableString> n) {
            note = n;
        }
    }

    public interface BucketClickedListener {
        void onClick(Bucket b);
    }

    public NotesAdapter(Context c, BucketClickedListener onItemClickListener) {
        super(c, 0);
        context = c;
        onClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
            //convertView.setOnCreateContextMenuListener(this);
        }

        final Bucket bucket = getItem(position);
        if (bucket == null) {
            throw new Resources.NotFoundException("NotesAdapter: no item at position " + position);
        }
        /*
        Log.d(this.getClass().getSimpleName(),
                "getItem(" + position + ")  -> " +
                "Bucket (enabled=" + bucket.enabled + ", " +
                "id=`" + bucket.note.getId() + "` " +
                "title=`" + bucket.note.getTitle() + "`)");
        */
        // Item Content: where the text (title/date/content) goes
        View itemContent = convertView.findViewById(R.id.item_content);
        itemContent.setTag(position);
        itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(NotesAdapter.class.getSimpleName(),
                        "item clicked :" +
                                "\tnoteId is " + bucket.note.getId() + " ," +
                                "\tposition is " + v.getTag() + "."
                );
                onClickListener.onClick(bucket);
            }
        });

        // Enabled
        if (!bucket.enabled) {
            bucket.checked = false;
        }
        itemContent.setEnabled(bucket.enabled);
        itemContent.setClickable(bucket.enabled);

        // Title
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(bucket.note.getTitle());
        tvTitle.setTag(position);

        // Checkbox
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox);
        cb.setChecked(bucket.checked);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bucket.checked = isChecked;
            }
        });

        // Date
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        GregorianCalendar tmpDate = bucket.note.getLastUpdate();
        String date = "Last updated: "
                + tmpDate.get(GregorianCalendar.DATE) + "/"
                + tmpDate.get(GregorianCalendar.MONTH) + "/"
                + tmpDate.get(GregorianCalendar.YEAR);
        tvDate.setText(date);

        // Content
        SpannableString content = bucket.note.getContent();
        int maxLength = 50;
        String strContent = ((content.length() > maxLength)
                ? content.subSequence(0, maxLength).toString() + "..."
                : content.toString())
                .replace('\n', ' ');
        TextView tvContent = (TextView) convertView.findViewById(R.id.tvContent);
        tvContent.setText(strContent);

        return convertView;
    }

    /*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Note<SpannableString> note = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        }

        // Title
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(note.getTitle());
        tvTitle.setTag(position);

        View itemContent = convertView.findViewById(R.id.item_content);
        itemContent.setTag(position);

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

        // Enabled
        if (notesOnHold.contains(note)) {
            cb.setChecked(false);
            checkedNotes.remove(note);
            itemContent.setEnabled(false);
            itemContent.setClickable(false);
        } else {
            itemContent.setEnabled(true);
            itemContent.setClickable(true);
        }
        return convertView;
    }
     */
    public void add(Note<SpannableString> note) {
        Bucket b = new Bucket(note);
        super.add(b);
    }

    public void setNotes(Iterable<Note<SpannableString>> notes) {
        super.clear();
        for (Note<SpannableString> n : notes) {
            super.add(new Bucket(n));
        }
    }

    public void setEnabled(boolean enabled, Note<SpannableString> note) {
        find(note).enabled = enabled;
        super.notifyDataSetChanged();
    }

    public void deleteNote(Note<SpannableString> note) {
        Bucket toDelete = find(note);
        super.remove(toDelete);
    }

    private Bucket find(Note<SpannableString> note) {
        for (int i = 0; i < super.getCount(); ++i) {
            Bucket b = super.getItem(i);
            if (b.note.getId() == note.getId()) {
                return b;
            }
        }
        return null;
    }
/*
    private Filter filter;
    private List<Note<SpannableString>> notes;
    private List<Note<SpannableString>> checkedNotes;
    private List<Note<SpannableString>> notesOnHold;
    Context context;

    public NotesAdapter(Context c) {
        super();
        this.context = c;
        notes = new ArrayList<>();
        checkedNotes = new ArrayList<>();
        notesOnHold = new ArrayList<>();
    }

    // FIXME tmp function to set the private array
    public void setNotes(Iterable<Note<SpannableString>> notesToSet) {
        notesToSet = requireNonNull(notesToSet);
        notes.clear();
        checkedNotes.clear();
        notesOnHold.clear();
        for(Note<SpannableString> n : notesToSet) {
            notes.add(n);
        }
    }

    public void deleteNote(Note<SpannableString> n) {
        notes.remove(n);
        checkedNotes.remove(n);
        notesOnHold.remove(n);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Note<SpannableString> getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return notes.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Note<SpannableString> note = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        }

        // Title
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(note.getTitle());
        tvTitle.setTag(position);

        View itemContent = convertView.findViewById(R.id.item_content);
        itemContent.setTag(position);

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

        // Enabled
        if (notesOnHold.contains(note)) {
            cb.setChecked(false);
            checkedNotes.remove(note);
            itemContent.setEnabled(false);
            itemContent.setClickable(false);
        } else {
            itemContent.setEnabled(true);
            itemContent.setClickable(true);
        }
        return convertView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu,
                                    View view,
                                    ContextMenu.ContextMenuInfo contextMenuInfo) {
    }

    public Filter getFilter() {
        if (filter == null)
            filter = new NoteFilter(notes);
        return filter;
    }

    public List<Note<SpannableString>> getCheckedNotes() {
        return new ArrayList<>(checkedNotes);
    }


    // returns the index of note with given id, or -1 if not found.
    private int getIndexOfNote(int id) {
        for(int i = 0; i < notes.size(); ++i) {
            if (notes.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    public void setEnabled(boolean enable, Note<SpannableString> note) {
        boolean onHold = notesOnHold.contains(note);
        if (enable && onHold) {
            notesOnHold.remove(note);
            notifyDataSetChanged();
        } else if (!enable && !onHold){
            notesOnHold.add(note);
            notifyDataSetChanged();
        }
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
            ArrayList<Note<SpannableString>> filtered =
                    (ArrayList<Note<SpannableString>>) results.values;
            notes.clear();
            for (int i = 0, l = filtered.size(); i < l; i++)
                notes.add(filtered.get(i));
            notifyDataSetInvalidated();
        }
    }
*/
}
