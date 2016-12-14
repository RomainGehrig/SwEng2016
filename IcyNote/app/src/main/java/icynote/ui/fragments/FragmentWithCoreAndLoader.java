package icynote.ui.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.text.SpannableString;

import icynote.noteproviders.NoteProvider;
import icynote.note.Note;

import static util.ArgumentChecker.requireNonNull;

/**
 * The fragment with core and loader.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public abstract class FragmentWithCoreAndLoader extends Fragment {
    private NoteProvider<Note<SpannableString>> core;
    private LoaderManager loaderManager;

    /**
     * Sets core.
     *
     * @param core the core
     */
    public void setCore(NoteProvider<Note<SpannableString>> core) {
        this.core = requireNonNull(core);
    }

    /**
     * Sets loader manager.
     *
     * @param loaderManager the loader manager
     */
    public void setLoaderManager(LoaderManager loaderManager) {
        this.loaderManager = requireNonNull(loaderManager);
    }

    /**
     * Gets core.
     *
     * @return the core
     */
    protected NoteProvider<Note<SpannableString>> getCore() {
        return core;
    }

    /**
     * Gets the loader manager.
     *
     * @return the loader manager
     */
// Name conflict with Fragment.getLoaderManager
    protected LoaderManager getThisLoaderManager() {
        return loaderManager;
    }
}
