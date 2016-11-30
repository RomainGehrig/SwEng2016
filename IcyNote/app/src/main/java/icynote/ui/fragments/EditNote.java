package icynote.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import icynote.note.Note;
import icynote.ui.R;
import icynote.ui.contracts.NotePresenter;
import icynote.ui.view.NoteViewHolder;
import me.gujun.android.taggroup.TagGroup;

import static java.lang.Math.min;

public class EditNote extends Fragment implements NotePresenter {

    private Note<SpannableString> note;

    //-------------------------------------------------------------------------------------

    private Contract activity;

    private TagGroup mDefaultTagGroup;
    private String[] tags = {}; // initialize tags here
    private NoteViewHolder viewHolder;

    public EditNote() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate the layout using the cloned inflater, not default inflater
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);
        viewHolder = new NoteViewHolder(view);

        viewHolder.getOptionButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.updateSelection(viewHolder.getContent().getSelectionStart(),
                        viewHolder.getContent().getSelectionEnd());

                activity.openOptionalPresenter(EditNote.this);
            }
        });
        mDefaultTagGroup = (TagGroup) view.findViewById(R.id.noteDisplayTagsText);
        if (tags != null && tags.length > 0) {
            mDefaultTagGroup.setTags(tags);
        }

        /*
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
        */

        return view;
    }



    @Override
    public void receiveNote(Note<SpannableString> note) {
        this.note = note;
        setupNoteView();
    }

    @Override
    public void onSaveNoteFailure(String message) {
        // todo
    }

    @Override
    public void onPause() {
        super.onPause();
        note = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            activity = (EditNote.Contract) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() + " has to implement EditNote.Contract.");
        }
        setupNoteView();
    }

    private void setupNoteView() {
        if (note == null || viewHolder == null)
            return;

        viewHolder.enableAll();
        viewHolder.getTitle().setHint(R.string.noteDisplayTitleHint);
        viewHolder.getContent().setHint(R.string.noteDisplayTextHint);
        updateTexts();
        setTextWatchers();
    }

    private void updateTexts() {
        if (note == null || getView() == null)
            return;

        viewHolder.getTitle().setText(note.getTitle());
        viewHolder.getContent().setText(note.getContent());
    }

    private void setTextWatchers() {
        if (note == null || getView() == null)
            return;

        // add listener to the title
        viewHolder.getTitle().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                note.setTitle(new SpannableString(s));
                activity.saveNote(note, EditNote.this);
            }
        });

        // add listener to the content
        viewHolder.getContent().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {note.setContent(new SpannableString(s));
                viewHolder.getContent().removeTextChangedListener(this);
                int start = viewHolder.getContent().getSelectionStart();
                int end = viewHolder.getContent().getSelectionEnd();
                viewHolder.getContent().setText(note.getContent());
                int length = viewHolder.getContent().getText().length();
                if (start > 0 && end > 0) {
                    viewHolder.getContent().setSelection(min(start, length), min(end, length));
                }
                viewHolder.getContent().addTextChangedListener(this);
                activity.saveNote(note, EditNote.this);
            }
        });
    }

    /*
    private boolean containsNotLast(String[] l, String t) {
        List<String> e = Arrays.asList(l).subList(0, l.length - 1);
        for (String s : e) {
            if (s.equals(t)) {
                return true;
            }
        }
        return false;
    }
    */

    @Override
    public void onOpenOptPresenterFailure(String message) {
        Toast.makeText(getActivity(),
                message, Toast.LENGTH_SHORT).show();
    }
}
