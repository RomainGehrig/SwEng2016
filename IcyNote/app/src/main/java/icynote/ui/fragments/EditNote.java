package icynote.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Arrays;
import java.util.List;

import icynote.loaders.NoteLoader;
import icynote.note.Note;
import icynote.ui.R;
import me.gujun.android.taggroup.TagGroup;
import util.Optional;

public class EditNote extends FragmentWithState implements
        LoaderCallbacks<Optional<Note<SpannableString>>> {
    public static final String KEY_NOTE_ID = "note_id";

    private TagGroup mDefaultTagGroup;
    private String[] tags = {"1", "2", "3"}; // initialize tags here
    private Note<SpannableString> note;
    private Integer noteId;

    public EditNote() {
        // Required empty public constructor
    }

    public void setNoteId(Integer id) {
        noteId = id;
    }

    @Override
    public void onResume() {
        super.onResume();
        restartLoader();
    }

    private void restartLoader() {
        Bundle args = new Bundle();
        if (noteId != null) {
            args.putInt(EditNote.KEY_NOTE_ID, noteId);
        }
        appState()
                .getLoaderManager()
                .restartLoader(NoteLoader.LOADER_ID, args, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate the layout using the cloned inflater, not default inflater
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);

        mDefaultTagGroup = (TagGroup) view.findViewById(R.id.noteDisplayTagsText);
        if (tags != null && tags.length > 0) {
            mDefaultTagGroup.setTags(tags);
        }

        mDefaultTagGroup.setOnTagChangeListener(new TagGroup.OnTagChangeListener() {
            @Override
            public void onAppend(TagGroup tagGroup, String tag) {
                String[] allTags = tagGroup.getTags();

                if (containsNotLast(allTags, tag)) {
                    //tagGroup.setBackgroundColor(Color.BLACK);
                } else {
                    //tagGroup.setBackgroundColor(Color.WHITE);
                }
            }

            @Override
            public void onDelete(TagGroup tagGroup, String tag) {
                // ---
            }
        });

        // FIXME does nothing
        mDefaultTagGroup.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_A) {
                    Log.d("key", "press");
                    ((TagGroup) v.findViewById(R.id.noteDisplayTagsText)).submitTag();
                    return true;
                }
                return false;
            }
        });

        EditText titleTextView = (EditText) view.findViewById(R.id.noteDisplayTitleText);
        EditText mainTextView = (EditText) view.findViewById(R.id.noteDisplayBodyText);

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
                note.setTitle(new SpannableString(s));
                appState().getNoteProvider().persist(note);
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
                note.setContent(new SpannableString(s));
                appState().getNoteProvider().persist(note);
            }
        });

        return view;
    }

    private boolean containsNotLast(String[] l, String t) {
        List<String> e = Arrays.asList(l).subList(0, l.length - 1);
        for (String s : e) {
            if (s.equals(t)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Loader<Optional<Note<SpannableString>>> onCreateLoader(int id, Bundle args) {
        Optional<Integer> noteId = Optional.empty();
        if (args != null && args.containsKey(KEY_NOTE_ID)) {
            noteId = Optional.of(args.getInt(KEY_NOTE_ID));
        }
        return new NoteLoader(getContext(), appState().getNoteProvider(), noteId);
    }

    @Override
    public void onLoadFinished(Loader<Optional<Note<SpannableString>>> loader,
                               Optional<Note<SpannableString>> optionalNote) {
        // TODO what to do if note is not present ?
        note = optionalNote.get();
        appState().setLastOpenedNoteId(note.getId());

        EditText titleTextView = (EditText) getView().findViewById(R.id.noteDisplayTitleText);
        EditText mainTextView = (EditText) getView().findViewById(R.id.noteDisplayBodyText);
        // TODO use an asynchronous task to set these things ?
        // (android doesn't like UI elements modified outside the UI thread
        titleTextView.setText(note.getTitle());
        mainTextView.setText(note.getContent());
    }

    @Override
    public void onLoaderReset(Loader<Optional<Note<SpannableString>>> loader) {
    }
}