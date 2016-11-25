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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import icynote.note.Note;
import icynote.plugins.FormatterPlugin;
import icynote.ui.R;
import icynote.ui.contracts.NoteOptionsPresenter;
import icynote.ui.contracts.NotePresenter;

public class MetadataNote extends Fragment implements NoteOptionsPresenter {
    private LinearLayout optionalActionsLayout = null;
    private Button backToNoteButton = null;
    private ReceivedData receivedData = null;
    private NoteOptionsPresenter.Contract contractor;

    public MetadataNote() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        if (receivedData == null) {
            receivedData = new ReceivedData();
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_metadata_note, container, false);

        optionalActionsLayout = (LinearLayout) v.findViewById(R.id.pluginButtons);
        backToNoteButton = (Button) v.findViewById(R.id.backButton);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // if the data is received, add it to the view immediately
        // otherwise it does nothing
        addNoteInView();
        addPluginActionInView();
    }

    @Override
    public void onResume() {
        super.onResume();
        contractor = (Contract) getActivity();
        backToNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contractor.optionPresenterFinished(MetadataNote.this);
            }
        });
    }

    @Override
    public void receiveNote(Note<SpannableString> note) {
        Log.d("NOTE", "RECEIVED");
        if (receivedData == null) {
            receivedData = new ReceivedData();
        }
        receivedData.note = note;
        addNoteInView();
    }

    @Override
    public void receivePluginData(ArrayList<View> pluginActions) {
        if (receivedData == null) {
            receivedData = new ReceivedData();
        }
        receivedData.optionalActions = pluginActions;
        addPluginActionInView();
    }

    @Override
    public void onSaveNoteFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }



    private void addPluginActionInView() {
        boolean viewIsCreated = optionalActionsLayout != null;
        boolean actionsReceived = receivedData.optionalActions != null;
        if (viewIsCreated && actionsReceived && !receivedData.areActionsAddedToView())  {
            receivedData.setActionsAddedToView();
            for (View button : receivedData.optionalActions) {
                optionalActionsLayout.addView(button);
            }
        }
    }

    private void addNoteInView() {
        boolean viewIsCreated = optionalActionsLayout != null;
        boolean noteReceived = receivedData.note != null;
        if (viewIsCreated && noteReceived && !receivedData.isNoteAddedToView())  {
            receivedData.setNoteAddedToView();
            setNoteInfo(receivedData.note);
        }
    }

    private void setNoteInfo(final Note<SpannableString> note) {

        // set title
        TextView titleTextView = (EditText) getView().findViewById(R.id.noteTitle);
        titleTextView.setText(note.getTitle());
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
                contractor.saveNote(note, MetadataNote.this);
            }
        });

        // set dates
        TextView dateCreatedTextView = (TextView) getView().findViewById(R.id.noteCreationDate);
        dateCreatedTextView.setText(dateToString(note.getCreation(), "Date Created:  "));

        TextView dateModifiedTextView = (TextView) getView().findViewById(R.id.noteModificationDate);
        dateModifiedTextView.setText(dateToString(note.getLastUpdate(), "Date Modified:  "));
    }

    private String dateToString(GregorianCalendar date, String description) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  'at'  HH:mm:ss");
        return description + dateFormat.format(date.getTime());
    }

    //----------------------------------------------------------------------------------------------

    private static class ReceivedData {
        Note<SpannableString> note;
        ArrayList<View> optionalActions;

        boolean isNoteAddedToView() {
            return noteAddedToView;
        }
        boolean areActionsAddedToView() {
            return actionsAddedToView;
        }
        void setNoteAddedToView() {
            noteAddedToView = true;
        }
        void setActionsAddedToView() {
            actionsAddedToView = true;
        }
        private boolean noteAddedToView = false;
        private boolean actionsAddedToView = false;
    }
}
