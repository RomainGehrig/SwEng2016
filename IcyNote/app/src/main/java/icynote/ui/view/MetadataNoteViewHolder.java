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

/**
 * The Metadata note view holder.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class MetadataNoteViewHolder {
    private final View layout;
    private final LinearLayout optionalActionsLayout;
    private final EditText titleTextView;
    private final TextView dateCreatedTextView;
    private final TextView dateModifiedTextView;
    private TextWatcher currentTextWatcher;
    private final Button backToNoteButton;

    /**
     * Inflate metadata note view holder.
     *
     * @param inflater  the inflater
     * @param container the container
     * @return the metadata note view holder
     */
    public static MetadataNoteViewHolder inflate(LayoutInflater inflater, ViewGroup container) {
        View layout = inflater.inflate(R.layout.fragment_metadata_note, container, false);
        return new MetadataNoteViewHolder(layout);
    }

    private MetadataNoteViewHolder(View inflatedLayout) {
        layout = inflatedLayout;
        titleTextView = (EditText) layout.findViewById(R.id.noteTitle);
        dateCreatedTextView = (TextView) layout.findViewById(R.id.noteCreationDate);
        dateModifiedTextView = (TextView) layout.findViewById(R.id.noteModificationDate);

        optionalActionsLayout = (LinearLayout) layout.findViewById(R.id.pluginButtons);
        backToNoteButton = (Button) layout.findViewById(R.id.backButton);
    }

    /**
     * Gets the layout.
     *
     * @return the view layout
     */
    public View getLayout() {
        return layout;
    }

    /**
     * Gets the optional actions layout.
     *
     * @return the optional actions layout
     */
    public LinearLayout getOptionalActionsLayout() {
        return optionalActionsLayout;
    }

    /**
     * Gets the title text view.
     *
     * @return the title text view
     */
    public EditText getTitleTextView() {
        return titleTextView;
    }

    /**
     * Gets the date created text view.
     *
     * @return the date created text view
     */
    public TextView getDateCreatedTextView() {
        return dateCreatedTextView;
    }

    /**
     * Gets the date modified text view.
     *
     * @return the date modified text view
     */
    public TextView getDateModifiedTextView() {
        return dateModifiedTextView;
    }

    /**
     * Sets the current text watcher.
     *
     * @param currentTextWatcher the current text watcher
     */
    public void setCurrentTextWatcher(TextWatcher currentTextWatcher) {
        this.currentTextWatcher = currentTextWatcher;
    }

    /**
     * Gets the current text watcher.
     *
     * @return the current text watcher
     */
    public TextWatcher getCurrentTextWatcher() {
        return currentTextWatcher;
    }

    /**
     * Gets back to note button.
     *
     * @return the back to note button
     */
    public Button getBackToNoteButton() {
        return backToNoteButton;
    }
}
