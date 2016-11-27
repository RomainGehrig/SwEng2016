package icynote.ui.view;

import android.text.SpannableString;

import icynote.note.Note;
import icynote.ui.BlankActivity;
import icynote.ui.contracts.NoteOptionsPresenter;
import icynote.ui.contracts.NotePresenterBase;
import icynote.ui.fragments.MetadataNote;

public class MockMetadataNote extends BlankActivity implements MetadataNote.Contract {

    public boolean saveNote = false;
    public boolean optionPresenterFinished = false;
    public boolean openEditNote = false;

    public MockMetadataNote() {}

    @Override
    public void optionPresenterFinished(NoteOptionsPresenter finished) {
        optionPresenterFinished = true;
    }

    @Override
    public void saveNote(Note<SpannableString> note, NotePresenterBase requester) {
        saveNote = true;
    }


    /*public void reOpenEditNote() {
        openEditNote = true;
    }*/
}
