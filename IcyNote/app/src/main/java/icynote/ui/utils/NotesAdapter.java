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
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import icynote.note.Note;
import icynote.ui.R;


/**
 * The notes adapter used to list the notes and the trashed notes.
 *
 * @author Julien Harbulot
 * @author Diana Petrescu
 * @version 2.0
 */
public class NotesAdapter extends ArrayAdapter<NotesAdapter.Bucket> {
    private static final String TAG = NotesAdapter.class.getSimpleName();

    private final Context context;
    private final BucketClickedListener onClickListener;

    /** used by the filter to retrieve the full list of notes */
    private final NotesBackup fullListBackup = new NotesBackup();

    /**
     * The type Bucket.
     */
    public class Bucket {
        private Note<SpannableString> note;
        private boolean checked = false;
        private boolean enabled = true;

        /**
         * Instantiates a new Bucket for a note.
         *
         * @param n the note
         */
        public Bucket(Note<SpannableString> n) {
            setNote(n);
        }

        /**
         * Gets note from the bucket.
         *
         * @return the note
         */
        public Note<SpannableString> getNote() {
            return note;
        }

        /**
         * Sets note from the bucket.
         *
         * @param note the note
         */
        public void setNote(Note<SpannableString> note) {
            this.note = note;
            NotesAdapter.this.notifyDataSetChanged();
        }

        /**
         * Checks if th note is checked.
         *
         * @return true if the notes is checked.
         */
        public boolean isChecked() {
            return checked;
        }

        /**
         * Sets if the not is checked.
         *
         * @param checked
         */
        public void setChecked(boolean checked) {
            this.checked = checked;
            NotesAdapter.this.notifyDataSetChanged();
        }

        /**
         * Is enabled boolean.
         *
         * @return the boolean
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Sets enabled.
         *
         * @param enabled the enabled
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
            NotesAdapter.this.notifyDataSetChanged();
        }
    }

    /**
     * The interface for the Bucket clicked listener.
     */
    public interface BucketClickedListener {
        void onClick(Bucket b);
    }

    /**
     * Instantiates a new Notes adapter.
     *
     * @param c                   the context
     * @param onItemClickListener the on item click listener
     */
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
            throw new Resources.NotFoundException(context.getString(R.string.error_notes_adapter_no_item_found) + position);
        }

        // Item Content: where the text (title/date/content) goes
        View itemContent = convertView.findViewById(R.id.item_content);
        itemContent.setTag(position);
        itemContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(NotesAdapter.class.getSimpleName(),
                        "item clicked :" +
                                "\tnoteId is " + bucket.getNote().getId() + " ," +
                                "\tposition is " + v.getTag() + "."
                );
                onClickListener.onClick(bucket);
            }
        });

        // Enabled
        if (!bucket.isEnabled()) {
            bucket.setChecked(false);
        }
        itemContent.setEnabled(bucket.isEnabled());
        itemContent.setClickable(bucket.isEnabled());

        // Title
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(bucket.getNote().getTitle());
        tvTitle.setTag(position);

        // Checkbox
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox);
        cb.setChecked(bucket.isChecked());
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                bucket.setChecked(isChecked);
            }
        });

        // Date
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        GregorianCalendar tmpDate = bucket.getNote().getLastUpdate();
        String date = context.getString(R.string.notes_adapter_date)
                + tmpDate.get(GregorianCalendar.DATE) + "/"
                + tmpDate.get(GregorianCalendar.MONTH) + "/"
                + tmpDate.get(GregorianCalendar.YEAR);
        tvDate.setText(date);

        // Content
        SpannableString content = bucket.getNote().getContent();
        int maxLength = 50;
        String strContent = ((content.length() > maxLength)
                ? content.subSequence(0, maxLength).toString() + "..."
                : content.toString())
                .replace('\n', ' ');
        TextView tvContent = (TextView) convertView.findViewById(R.id.tvContent);
        tvContent.setText(strContent);

        return convertView;
    }

    /**
     * Sets notes.
     *
     * @param notes the notes
     */
    public void setNotes(Iterable<Note<SpannableString>> notes) {
        clear();
        fullListBackup.clear();
        for (Note<SpannableString> n : notes) {
            Bucket newBucket = new Bucket(n);
            add(newBucket);
            fullListBackup.add(newBucket);
        }
    }

    /**
     * Sets enabled.
     *
     * @param enabled the enabled
     * @param note    the note
     */
    public void setEnabled(boolean enabled, Note<SpannableString> note) {
        Bucket toEnable = find(note);
        if (toEnable == null) {
            Log.e(TAG, "unable to find for " + (enabled ? "enabling" : "disabling.")
                    + " null bucket for id: " + note.getId());
        } else {
            toEnable.setEnabled(enabled);
            super.notifyDataSetChanged();
        }
    }

    /**
     * Delete note.
     *
     * @param note the note
     */
    public void deleteNote(Note<SpannableString> note) {
        Bucket b = find(note);
        if (b != null) {
            remove(b);
            fullListBackup.remove(b);
            notifyDataSetChanged();
        }
    }

    private Bucket find(Note<SpannableString> note) {
        for (int i = 0; i < super.getCount(); ++i) {
            Bucket b = super.getItem(i);
            if (b.getNote().getId() == note.getId()) {
                return b;
            }
        }
        return null;
    }

    //filter
    @NonNull
    @Override
    public Filter getFilter() {
        return filter;
    }

    private final Filter filter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence chars) {
            FilterResults result = new FilterResults();

            if (chars == null ||chars.length() == 0)  {
                /*
                Log.d(NotesAdapter.class.getSimpleName(),
                        "filtering not needed"+
                                "actual adapter size " + getCount() +
                                "backup size " + fullListBackup.getData().size());
                                */
                result.count = fullListBackup.size();
                result.values = fullListBackup.getData();
                return result;
            }
            /*
            Log.d(NotesAdapter.class.getSimpleName(),
                    "filtering in progress " +
                    "actual adapter size " + getCount() +
                    "backup size " + fullListBackup.getData().size());
            */

            String filterSeq = chars.toString().toLowerCase();
            ArrayList<Bucket> filtered = new ArrayList<>();

            for (Bucket bucket : fullListBackup.getData()) {
                if (titleContainsFilter(bucket, filterSeq)
                        || contentContainsFilter(bucket, filterSeq))
                {
                    filtered.add(bucket);
                }
            }
            result.count = filtered.size();
            result.values = filtered;

            return result;
        }

        boolean titleContainsFilter(Bucket bucket, String filterSeq) {
            return bucket.getNote()
                    .getTitle()
                    .toString()
                    .toLowerCase()
                    .contains(filterSeq);
        }
        boolean contentContainsFilter(Bucket bucket, String filterSeq) {
            return bucket.getNote().getContent()
                    .toString()
                    .toLowerCase()
                    .contains(filterSeq);
        }
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // NOTE: this function is *always* called from the UI thread.
            NotesAdapter.super.clear(); //super needed to not clear the backup
            addAll((List<Bucket>)results.values);
            //Log.d(NotesAdapter.class.getSimpleName(), "publishingResults "
            //+ " actual adapter size " + getCount() +
            //" backup size " + fullListBackup.getData().size());
            notifyDataSetChanged();
        }
    };

    private static class NotesBackup {
        private final ArrayList<Bucket> backupData = new ArrayList<>();

        /**
         * Add.
         *
         * @param b the b
         */
        public void add(Bucket b) {
            //Log.i(NotesAdapter.class.getSimpleName(), "add note " + b.getNote().getId() + " to backup");
            backupData.add(b);
        }

        /**
         * Remove.
         *
         * @param b the b
         */
        public void remove(Bucket b) {
            //Log.i(NotesAdapter.class.getSimpleName(),
            //        "remove note "
            //        + b.getNote().getId()
            //        + "from backup");
            backupData.remove(b);
        }

        /**
         * Clear.
         */
        public void clear() {
            //Log.i(NotesAdapter.class.getSimpleName(), "clear backup");
            backupData.clear();
        }

        /**
         * Size int.
         *
         * @return the int
         */
        public int size() {
            //Log.i(NotesAdapter.class.getSimpleName(), "get size of backup " + backupData.size());
            return backupData.size();
        }

        /**
         * Gets data.
         *
         * @return the data
         */
        public ArrayList<Bucket> getData() {
            //Log.i(NotesAdapter.class.getSimpleName(), "duplicate backup");
            return new ArrayList<>(backupData);
        }
    }
}