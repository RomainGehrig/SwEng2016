package icynote.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.text.SpannableString;

import icynote.noteproviders.NoteProvider;
import icynote.note.Note;

import static util.ArgumentChecker.requireNonNull;

public abstract class FragmentWithCoreAndLoader extends Fragment {
    private NoteProvider<Note<SpannableString>> core;
    private LoaderManager loaderManager;

    public void setCore(NoteProvider<Note<SpannableString>> core) {
        this.core = requireNonNull(core);
    }

    public void setLoaderManager(LoaderManager loaderManager) {
        this.loaderManager = requireNonNull(loaderManager);
    }

    protected NoteProvider<Note<SpannableString>> getCore() {
        return core;
    }

    // Name conflict with Fragment.getLoaderManager
    protected LoaderManager getThisLoaderManager() {
        return loaderManager;
    }
}
