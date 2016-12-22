package icynote.ui.view;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import icynote.ui.R;

/**
 * The note view holder.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class NoteViewHolder {
    private final EditText title;
    private final EditText content;
    private final Button optionButton;

    /**
     * Instantiates a new Note view holder.
     *
     * @param view the view
     */
    public NoteViewHolder(final View view) {
        title = (EditText) view.findViewById(R.id.noteDisplayTitleText);
        content = (EditText) view.findViewById(R.id.noteDisplayBodyText);
        optionButton = (Button)view.findViewById(R.id.noteDisplaySettingsButton);
    }

    /**
     * Enable all the elements.
     */
    public void enableAll() {
        title.setEnabled(true);
        content.setEnabled(true);
    }

    /**
     * Gets the title.
     *
     * @return the title
     */
    public EditText getTitle() {
        return title;
    }

    /**
     * Gets the content.
     *
     * @return the content
     */
    public EditText getContent() {
        return content;
    }

    /**
     * Gets the option button.
     *
     * @return the option button
     */
    public View getOptionButton() {
        return optionButton;
    }
}
