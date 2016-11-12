package icynote.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import icynote.core.IcyNoteCore;
import icynote.core.Note;
import util.Optional;

import static util.ArgumentChecker.requireNonNull;

/**
 * Class for loading a single {@code Note}s in the background, preventing slowdown in
 * the main thread.
 *
 * @author Romain Gehrig
 * @version 1.0
 * @see icynote.core.IcyNoteCore
 */
public class NoteLoader extends AsyncTaskLoader<Optional<Note>> {
    private final IcyNoteCore core;
    private int noteId;

    public NoteLoader(Context context, IcyNoteCore core, int noteId) {
        super(context);
        if (core == null)
            throw new IllegalArgumentException("core is null");
        this.core = requireNonNull(core);
        this.noteId = noteId;
    }

    @Override
    public Optional<Note> loadInBackground() {
        return core.getNote(noteId);
    }

    @Override
    public void deliverResult(Optional<Note> note) {
        // If the loader was not initialized or was reset recently
        if (isReset()) {
            return;
        }

        if (isStarted()) {
            super.deliverResult(note);
        }
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
        // Ensure the loader is stopped
        onStopLoading();
    }
}
