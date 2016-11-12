package icynote.ui;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;

import icynote.core.Note;
import icynote.loaders.NoteLoader;
import me.gujun.android.taggroup.TagGroup;
import util.Optional;

public class EditNote extends FragmentWithCoreAndLoader implements
        LoaderManager.LoaderCallbacks<Optional<Note>>{
    private static final String KEY_NOTE_ID = "note_id";
    private TagGroup mDefaultTagGroup;

    private String[] tags = {}; // initialize tags here

    public EditNote() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_NOTE_ID)) {
            //retrieveNote(savedInstanceState.getInt(KEY_NOTE_ID));
        }

        mDefaultTagGroup = (TagGroup) getActivity().findViewById(R.id.noteDisplayTagsText);
        if (tags != null && tags.length > 0) {
            mDefaultTagGroup.setTags(tags);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Bundle args = new Bundle();
        args.putInt(KEY_NOTE_ID, 1);
        getThisLoaderManager().initLoader(4, args, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), Theme.getTheme().toInt());

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        // inflate the layout using the cloned inflater, not default inflater
        View v = localInflater.inflate(R.layout.fragment_edit_note, container, false);

        EditText titleTextView = (EditText)v.findViewById(R.id.noteDisplayTitleText);
        titleTextView.setTextColor(Theme.getTheme().getTextColor());
        EditText mainTextView = (EditText)v.findViewById(R.id.noteDisplayBodyText);
        mainTextView.setTextColor(Theme.getTheme().getTextColor());

        return v;
    }

    @Override
    public Loader<Optional<Note>> onCreateLoader(int id, Bundle args) {
        return new NoteLoader(getContext(), getCore(), args.getInt(KEY_NOTE_ID));
    }

    @Override
    public void onLoadFinished(Loader<Optional<Note>> loader, Optional<Note> optionalNote) {
        Note note = optionalNote.get();
        EditText titleTextView = (EditText)getView().findViewById(R.id.noteDisplayTitleText);
        EditText mainTextView = (EditText)getView().findViewById(R.id.noteDisplayBodyText);
        // TODO: use an asynchrone task to set these things
        // (android doesn't like UI elements modified outside the UI thread
        titleTextView.setText(note.getTitle());
        mainTextView.setText(note.getContent());
    }

    @Override
    public void onLoaderReset(Loader<Optional<Note>> loader) {
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
