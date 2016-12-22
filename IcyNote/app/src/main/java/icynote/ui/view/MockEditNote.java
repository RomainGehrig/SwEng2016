package icynote.ui.view;

import android.text.SpannableString;

import icynote.note.Note;
import icynote.ui.BlankActivity;
import icynote.ui.contracts.NotePresenter;
import icynote.ui.contracts.NotePresenterBase;
import icynote.ui.fragments.EditNote;

/**
 * The type Mock edit note.
 *
 * @author Kim
 * @version 1.0
 */
public class MockEditNote extends BlankActivity implements EditNote.Contract {

    /**
     * true if the note is saved.
     */
    public boolean saveNote = false;
    /**
     * true if the metadata is opened.
     */
    public boolean openMetadata = false;
    private boolean openOptionalPresenter = false;

    /**
     * Instantiates a new Mock edit note.
     */
    public MockEditNote() {}

    @Override
    public void openOptionalPresenter(NotePresenter requester) {
        openOptionalPresenter = true;
    }

    @Override
    public void updateSelection(int start, int end) {

    }

    @Override
    public void saveNote(Note<SpannableString> note, NotePresenterBase requester) {
        saveNote = true;
    }
}
