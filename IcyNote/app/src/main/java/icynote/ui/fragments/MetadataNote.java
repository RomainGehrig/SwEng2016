package icynote.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;

import icynote.note.Note;
import icynote.ui.R;
import icynote.ui.contracts.NoteOptionsPresenter;
import icynote.ui.view.MetadataNoteViewHolder;

/**
 * The fragment for the Metadata of a note.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class MetadataNote extends Fragment implements NoteOptionsPresenter {
    private MetadataNoteViewHolder viewHolder;

    private ReceivedData receivedData = null;
    private NoteOptionsPresenter.Contract contractor;

    /**
     * Instantiates a new Metadata note. Required empty public constructor
     */
    public MetadataNote() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewHolder = MetadataNoteViewHolder.inflate(inflater, container);
        viewHolder.getBackToNoteButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contractor.optionPresenterFinished(MetadataNote.this);
            }
        });
        return viewHolder.getLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        contractor = (Contract) getActivity();

        // if the data is received, add it to the view immediately
        // otherwise it does nothing
        // android bug: must be called in `onResume` method and not `onViewCreated`.
        // see: http://stackoverflow.com/questions/13303469/
        // edit_text-set_text-not-working-with-fragment

        Log.i(this.getClass().getSimpleName(), "onViewCreated");
        addNoteInView();
        addPluginActionInView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewHolder = null;
    }

    @Override
    public void onSaveNoteFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void receiveNote(Note<SpannableString> note) {
        Log.d(this.getClass().getSimpleName(), "note received id = " + note.getId());
        if (receivedData == null) {
            receivedData = new ReceivedData();
        }
        receivedData.note = note;
        addNoteInView();
    }

    @Override
    public void receivePluginData(ArrayList<View> pluginActions) {
        Log.d(this.getClass().getSimpleName(), "plugin actions received");
        if (receivedData == null) {
            receivedData = new ReceivedData();
        }
        receivedData.optionalActions = pluginActions;
        addPluginActionInView();
    }

    private void addPluginActionInView() {
        boolean viewIsCreated = getView() != null;
        boolean actionsReceived = receivedData != null && receivedData.optionalActions != null;
        if (viewIsCreated && actionsReceived)  {
            viewHolder.getOptionalActionsLayout().removeAllViews();
            for (View button : receivedData.optionalActions) {
                viewHolder.getOptionalActionsLayout().addView(button);
            }
        }
    }

    private void addNoteInView() {
        boolean viewIsCreated = getView() != null;
        boolean noteReceived = receivedData != null && receivedData.note != null;
        Log.d(this.getClass().getSimpleName(),
                "viewIsCreated=" + viewIsCreated +", noteReceived="+ noteReceived);
        if (viewIsCreated && noteReceived)  {
            setNoteInfo(receivedData.note);
        }
    }

    private void setNoteInfo(final Note<SpannableString> note) {
        Log.i(this.getClass().getSimpleName(),
                "updating note info for note id=" + note.getId() +
        ", title=" + note.getTitle());

        removeTextWatchers();

        // update title while there is no text watcher
        viewHolder.getTitleTextView().setText(note.getTitle());
        Log.i(this.getClass().getSimpleName(),
                "testView is now : " + viewHolder.getTitleTextView().getText().toString());

        // create updated text watcher
        viewHolder.setCurrentTextWatcher(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i(MetadataNote.class.getSimpleName(),
                        "set new note title : " + s.toString());
                note.setTitle(new SpannableString(s));
                contractor.saveNote(note, MetadataNote.this);
            }
        });

        Log.i(this.getClass().getSimpleName(), "adding text watchers");
        viewHolder.getTitleTextView().addTextChangedListener(viewHolder.getCurrentTextWatcher());

        // set dates
        viewHolder.getDateCreatedTextView()
                .setText(dateToString(note.getCreation(), getString(R.string.metadata_note_date_created)));

        viewHolder.getDateModifiedTextView()
                .setText(dateToString(note.getLastUpdate(), getString(R.string.metadata_note_date_modified)));
    }

    private String dateToString(GregorianCalendar date, String description) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(getString(R.string.metadata_note_date_format),
                Locale.ROOT);
        return description + dateFormat.format(date.getTime());
    }

    private void removeTextWatchers() {
        if (viewHolder.getCurrentTextWatcher() != null) {
            Log.i(this.getClass().getSimpleName(),
                    "removing text watchers");
            viewHolder
                    .getTitleTextView()
                    .removeTextChangedListener(viewHolder.getCurrentTextWatcher());
        }
    }

    private static class ReceivedData {
        /**
         * The Note.
         */
        Note<SpannableString> note;
        /**
         * The Optional actions.
         */
        ArrayList<View> optionalActions;
    }
}
