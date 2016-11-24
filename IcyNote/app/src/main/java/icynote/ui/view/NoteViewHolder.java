package icynote.ui.view;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import icynote.ui.R;

public class NoteViewHolder {
    private final EditText title;
    private final EditText content;
    private final Button optionButton;

    public NoteViewHolder(final View view) {
        title = (EditText) view.findViewById(R.id.noteDisplayTitleText);
        content = (EditText) view.findViewById(R.id.noteDisplayBodyText);
        optionButton = (Button)view.findViewById(R.id.noteDisplaySettingsButton);
    }

    public void enableAll() {
        title.setEnabled(true);
        content.setEnabled(true);
    }

    public EditText getTitle() {
        return title;
    }

    public EditText getContent() {
        return content;
    }

    public View getOptionButton() {
        return optionButton;
    }
}
