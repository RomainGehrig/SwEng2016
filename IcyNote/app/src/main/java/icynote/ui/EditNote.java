package icynote.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import icynote.core.IcyNoteCore;
import icynote.core.Note;
import me.gujun.android.taggroup.TagGroup;

import static icynote.ui.NotesList.notes;


public class EditNote extends Fragment {
    private TagGroup mDefaultTagGroup;

    /**************** DIRTY HACK FOR TESTS ****************/
    static public Note note;

    private String[] tags = {}; // initialize tags here

    public EditNote() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDefaultTagGroup = (TagGroup) getActivity().findViewById(R.id.noteDisplayTagsText);
        if (tags != null && tags.length > 0) {
            mDefaultTagGroup.setTags(tags);
        }

        int id = getArguments().getInt("id");
        if(id > notes.size()){
            throw new IllegalArgumentException("The note with id " + id + " does not exist.");
        }

        note = notes.get(getArguments().getInt("id"));

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
        titleTextView.setText(note.getTitle());
        EditText mainTextView = (EditText)v.findViewById(R.id.noteDisplayBodyText);
        mainTextView.setTextColor(Theme.getTheme().getTextColor());
        mainTextView.setText(note.getContent());


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

        return v;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
