package icynote.ui.view;

import android.text.SpannableString;

import icynote.note.Note;
import icynote.ui.BlankActivity;
import icynote.ui.contracts.NoteOpenerBase;
import icynote.ui.contracts.NotesPresenter;
import icynote.ui.fragments.NotesList;

/**
 * The Mock notes list.
 *
 * @author Kim
 * @version 1.0
 */
public class MockNotesList extends BlankActivity implements NotesList.Contract {

    /**
     * true if the note is created.
     */
    public boolean createNote = false;
    private boolean openNote = false;
    /**
     * true if the note is deleted.
     */
    public boolean deleteNote = false;

    /**
     * Instantiates a new Mock notes list.
     */
    public MockNotesList() {

    }

    @Override
    public void createNote(NotesPresenter requester) {
        createNote = true;
    }

    @Override
    public void deleteNote(Note<SpannableString> note, NotesPresenter requester) {
        deleteNote = true;
    }

    @Override
    public void openNote(int id, NoteOpenerBase requester) {
        openNote = true;
    }

    @Override
    public void reOpenLastOpenedNote(NoteOpenerBase requester) {

    }
}
