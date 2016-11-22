package icynote.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.EditText;

import java.util.Arrays;
import java.util.List;

import me.gujun.android.taggroup.TagGroup;

import icynote.core.Note;
import icynote.loaders.NoteLoader;
import me.gujun.android.taggroup.TagGroup;
import util.Optional;

public class EditNote extends FragmentWithCoreAndLoader implements
        LoaderManager.LoaderCallbacks<Optional<Note>>{
    private static final String KEY_NOTE_ID = "note_id";
    private TagGroup mDefaultTagGroup;

    private String[] tags = {"1", "2", "3"}; // initialize tags here
    private Note note;

    public EditNote() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_NOTE_ID)) {
            //retrieveNote(savedInstanceState.getInt(KEY_NOTE_ID));
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
        View view = localInflater.inflate(R.layout.fragment_edit_note, container, false);

        mDefaultTagGroup = (TagGroup) view.findViewById(R.id.noteDisplayTagsText);
        if (tags != null && tags.length > 0) {
            mDefaultTagGroup.setTags(tags);
        }

        mDefaultTagGroup.setOnTagChangeListener(new TagGroup.OnTagChangeListener() {
            @Override
            public void onAppend( TagGroup tagGroup, String tag) {
                String[] allTags = tagGroup.getTags();

                if(containsNotLast(allTags, tag)){
                    //tagGroup.setBackgroundColor(Color.BLACK);
                } else {
                    //tagGroup.setBackgroundColor(Color.WHITE);
                }
            }

            @Override
            public void onDelete(TagGroup tagGroup, String tag) {
                // ---
            }
        } );

        // FIXME does nothing
        mDefaultTagGroup.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ( event.getAction()==KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_A) {
                    Log.d("key", "press");
                    ((TagGroup)v.findViewById(R.id.noteDisplayTagsText)).submitTag();
                    return true;
                }
                return false;
            }
        } );

        EditText titleTextView = (EditText)view.findViewById(R.id.noteDisplayTitleText);
        titleTextView.setTextColor(Theme.getTheme().getTextColor());
        EditText mainTextView = (EditText)view.findViewById(R.id.noteDisplayBodyText);
        mainTextView.setTextColor(Theme.getTheme().getTextColor());

        // add listener to the title
        titleTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                note.setTitle(s.toString());
            }
        });

        // add listener to the content
        mainTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                note.setContent(s.toString());
            }
        });

        return view;
    }

    private boolean containsNotLast(String[] l, String t){
        List<String> e = Arrays.asList(l).subList(0, l.length-1);
        for(String s: e){
            if(s.equals(t)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Loader<Optional<Note>> onCreateLoader(int id, Bundle args) {
        return new NoteLoader(getContext(), getCore(), args.getInt(KEY_NOTE_ID));
    }

    @Override
    public void onLoadFinished(Loader<Optional<Note>> loader, Optional<Note> optionalNote) {
        // TODO what to do if note is not present ?
        note = optionalNote.get();
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
