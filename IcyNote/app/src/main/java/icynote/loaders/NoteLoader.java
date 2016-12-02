package icynote.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.text.SpannableString;

import icynote.note.Note;
import icynote.noteproviders.NoteProvider;
import util.Optional;

import static util.ArgumentChecker.requireNonNull;

/**
 * Class for loading a single {@code Note}s in the background, preventing slowdown in
 * the main thread.
 *
 * @author Romain Gehrig
 * @version 1.0
 * @see NoteProvider
 */
public class NoteLoader extends AsyncTaskLoader<Optional<Note<SpannableString>>> {
    public static final int LOADER_ID = 2;
    private final NoteProvider<Note<SpannableString>> core;
    private Optional<Integer> noteId;

    private static final String ERROR_ADD_NOTE_DECORATOR_NULL = "addNoteDecorators is null";

    public NoteLoader(Context context, NoteProvider<Note<SpannableString>> core, Optional<Integer> noteId) {
        super(context);
        if (core == null)
            throw new IllegalArgumentException(ERROR_ADD_NOTE_DECORATOR_NULL);
        this.core = requireNonNull(core);
        this.noteId = requireNonNull(noteId);
    }

    @Override
    public Optional<Note<SpannableString>> loadInBackground() {
        if (noteId.isPresent())
            return core.getNote(noteId.get());
        else
            return core.createNote();
    }

    @Override
    public void deliverResult(Optional<Note<SpannableString>> note) {
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
        stopLoading();
    }
}
