package icynote.ui.view;

import android.text.SpannableString;

import icynote.note.Note;
import icynote.ui.BlankActivity;
import icynote.ui.contracts.NoteOptionsPresenter;
import icynote.ui.contracts.NotePresenterBase;
import icynote.ui.fragments.MetadataNote;

public class MockMetadataNote extends BlankActivity implements MetadataNote.Contract {

    private boolean saveNote = false;
    private boolean optionPresenterFinished = false;

    public MockMetadataNote() {}

    @Override
    public void optionPresenterFinished(NoteOptionsPresenter finished) {
        optionPresenterFinished = true;
    }

    @Override
    public void saveNote(Note<SpannableString> note, NotePresenterBase requester) {
        saveNote = true;
    }
}
