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
 * @author Julien Harbulot
 * @author Diana
 * @version 2.0
 */

public class NotesAdapter extends ArrayAdapter<NotesAdapter.Bucket> {
    private static final String TAG = NotesAdapter.class.getSimpleName();

    private Context context;
    private BucketClickedListener onClickListener;
    private List<Bucket> lastListSetBySetNotesMethod = new ArrayList<>();

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

    public void add(Note<SpannableString> note) {
        Bucket b = new Bucket(note);
        super.add(b);
    }

    public void setNotes(Iterable<Note<SpannableString>> notes) {
        super.clear();
        lastListSetBySetNotesMethod.clear();
        for (Note<SpannableString> n : notes) {
            Bucket current = new Bucket(n);
            super.add(current);
            lastListSetBySetNotesMethod.add(current);
        }
    }

    public void setEnabled(boolean enabled, Note<SpannableString> note) {
        Bucket toEnable = find(note);
        if (toEnable == null) {
            Log.e(TAG, "unable to " + (enabled ? "enable" : "disable")
                    + " null bucket for id: " + note.getId());
        } else {
            toEnable.enabled = enabled;
            super.notifyDataSetChanged();
        }
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
                Log.d(this.getClass().getSimpleName(), "filtering not needed");
                result.count = lastListSetBySetNotesMethod.size();
                result.values = lastListSetBySetNotesMethod;
                return result;
            }

            Log.d(this.getClass().getSimpleName(), "filtering in progress");

            String filterSeq = chars.toString().toLowerCase();
            ArrayList<Bucket> filtered = new ArrayList<>();

            for (int i = 0; i < getCount(); ++i) {
                Bucket bucket = getItem(i);

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
            return bucket.note
                    .getTitle()
                    .toString()
                    .toLowerCase()
                    .contains(filterSeq);
        }
        boolean contentContainsFilter(Bucket bucket, String filterSeq) {
            return bucket.note.getContent()
                    .toString()
                    .toLowerCase()
                    .contains(filterSeq);
        }
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            // NOTE: this function is *always* called from the UI thread.
            Log.d(this.getClass().getSimpleName(), "publishingResults");
            clear();
            addAll((List<Bucket>)results.values);

            notifyDataSetChanged();
        }
    };
}
