package icynote.ui.view;

import android.view.View;
import android.widget.EditText;

import icynote.ui.R;

public class NoteViewHolder {
    private final EditText title;
    private final EditText content;

    public NoteViewHolder(final View view) {
        title = (EditText) view.findViewById(R.id.noteDisplayTitleText);
        content = (EditText) view.findViewById(R.id.noteDisplayBodyText);
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

}
