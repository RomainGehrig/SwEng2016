package icynote.ui.view;

import android.text.SpannableString;

import icynote.note.Note;
import icynote.ui.BlankActivity;
import icynote.ui.contracts.NoteOptionsPresenter;
import icynote.ui.contracts.NotePresenterBase;
import icynote.ui.fragments.MetadataNote;

/**
 * The Mock metadata note.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class MockMetadataNote extends BlankActivity implements MetadataNote.Contract {

    /**
     * true if the note is savec.
     */
    public boolean saveNote = false;
    private boolean optionPresenterFinished = false;
    /**
     * true if the edit note is opened.
     */
    public boolean openEditNote = false;

    /**
     * Instantiates a new Mock metadata note.
     */
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
