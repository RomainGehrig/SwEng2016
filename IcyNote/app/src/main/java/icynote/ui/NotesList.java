package icynote.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import icynote.core.IcyNoteCore;
import icynote.core.Note;
import icynote.loaders.NotesLoader;

import static util.ArgumentChecker.requireNonNull;

public class NotesList extends FragmentWithCoreAndLoader implements LoaderManager.LoaderCallbacks<Iterable<Note>> {
    private static final String TAG = "NotesList";
    private Loader<Iterable<Note>> loader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            Log.i(TAG, "savedInstanceState was not null ! (could save content)");
        }
        Log.i(TAG, "NotesList created");
    }

    @Override
    public void onResume() {
        super.onResume();
        getThisLoaderManager().initLoader(1, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), Theme.getTheme().toInt());

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        // inflate the layout using the cloned inflater, not default inflater
        return localInflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
        /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Iterable<Note>> onCreateLoader(int id, Bundle args) {
        // TODO: indicate we are now waiting for the note
        Log.i(TAG, "Loader initialized");
        loader = new NotesLoader(getContext(), getCore());
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Iterable<Note>> loader, Iterable<Note> data) {
        // TODO display notes
        Log.i(TAG, "Received notes");
        for(Note n: data) {
            Log.i(TAG, "Note: " + n.getTitle() + ", " + n.getContent() + ", " + n.getId());
        }
        Log.i(TAG, "END");
    }

    @Override
    public void onLoaderReset(Loader<Iterable<Note>> loader) {
        Log.i(TAG, "Loader reset");
        // TODO: anything to do ?
    }
}
