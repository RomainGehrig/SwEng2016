package icynote.ui.view;

import android.text.SpannableString;

import icynote.note.Note;
import icynote.ui.BlankActivity;
import icynote.ui.contracts.NotePresenter;
import icynote.ui.contracts.NotePresenterBase;
import icynote.ui.fragments.EditNote;

/**
 * Created by kl on 23.11.2016.
 */
public class MockEditNote extends BlankActivity implements EditNote.Contract {

    public boolean saveNote = false;
    public boolean openMetadata = false;
    private boolean openOptionalPresenter = false;

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
