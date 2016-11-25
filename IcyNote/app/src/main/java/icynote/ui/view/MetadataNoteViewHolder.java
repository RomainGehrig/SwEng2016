package icynote.ui.view;

import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import icynote.ui.R;

public class MetadataNoteViewHolder {
    private View layout;
    private LinearLayout optionalActionsLayout;
    private EditText titleTextView;
    private TextView dateCreatedTextView;
    private TextView dateModifiedTextView;
    private TextWatcher currentTextWatcher;
    private Button backToNoteButton;

    public static MetadataNoteViewHolder inflate(LayoutInflater inflater, ViewGroup container) {
        View layout = inflater.inflate(R.layout.fragment_metadata_note, container, false);
        return new MetadataNoteViewHolder(layout);
    }

    public MetadataNoteViewHolder(View inflatedLayout) {
        layout = inflatedLayout;
        titleTextView = (EditText) layout.findViewById(R.id.noteTitle);
        dateCreatedTextView = (TextView) layout.findViewById(R.id.noteCreationDate);
        dateModifiedTextView = (TextView) layout.findViewById(R.id.noteModificationDate);

        optionalActionsLayout = (LinearLayout) layout.findViewById(R.id.pluginButtons);
        backToNoteButton = (Button) layout.findViewById(R.id.backButton);
    }

    public View getLayout() {
        return layout;
    }

    public LinearLayout getOptionalActionsLayout() {
        return optionalActionsLayout;
    }

    public EditText getTitleTextView() {
        return titleTextView;
    }

    public TextView getDateCreatedTextView() {
        return dateCreatedTextView;
    }

    public TextView getDateModifiedTextView() {
        return dateModifiedTextView;
    }

    public void setCurrentTextWatcher(TextWatcher currentTextWatcher) {
        this.currentTextWatcher = currentTextWatcher;
    }

    public TextWatcher getCurrentTextWatcher() {
        return currentTextWatcher;
    }

    public Button getBackToNoteButton() {
        return backToNoteButton;
    }
}
