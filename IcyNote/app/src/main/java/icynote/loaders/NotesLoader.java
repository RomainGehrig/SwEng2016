package icynote.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.SpannableString;
import android.util.Log;

import icynote.note.Note;
import icynote.noteproviders.NoteProvider;
import icynote.noteproviders.OrderBy;
import icynote.noteproviders.OrderType;

/**
 * Class for loading {@code Note}s in the background, preventing slowdown in
 * the main thread.
 *
 * @author Romain Gehrig
 * @version 1.0
 * @see NoteProvider
 */
public class NotesLoader extends AsyncTaskLoader<Iterable<Note<SpannableString>>> {
    public static final int LOADER_ID = 1;
    private final NoteProvider<Note<SpannableString>> core;
    private Iterable<Note<SpannableString>> notes;
    private OrderBy orderBy = OrderBy.CREATION;
    private OrderType orderType = OrderType.DSC;

    public NotesLoader(Context context, NoteProvider<Note<SpannableString>> core) {
        super(context);
        if (core == null)
            throw new IllegalArgumentException("addNoteDecorators is null");
        this.core = core;

        Log.i("NotesLoader", "NotesLoader created");
    }

    public void setOrderBy(OrderBy newOrderBy) {
        orderBy = newOrderBy;
    }

    public void setOrderType(OrderType newOrderType) {
        orderType = newOrderType;
    }

    @Override
    public Iterable<Note<SpannableString>> loadInBackground() {
        Log.i("NotesLoader", "loadInBackground " + android.os.Process.myTid());
        notes = core.getNotes(orderBy, orderType);
        return notes;
    }

    @Override
    public void deliverResult(Iterable<Note<SpannableString>> notes) {
        Log.i("NotesLoader", "deliverResult " + android.os.Process.myTid());
        // If the loader was not initialized or was reset recently
        if (isReset()) {
            if (notes != null) {
                this.notes = notes;
            }
            return;
        }

        if (isStarted()) {
            super.deliverResult(notes);
        }
    }

    @Override
    protected void onStartLoading() {
        Log.i("NotesLoader", "onStartLoading " + android.os.Process.myTid());

        forceLoad();
    }

    @Override
    protected void onReset() {
        Log.i("NotesLoader", "onReset");
        super.onReset();

        // Ensure the loader is stopped
        stopLoading();

        notes = null;
    }
}
