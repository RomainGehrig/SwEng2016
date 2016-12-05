package icynote.ui.view;

import android.text.SpannableString;

import icynote.note.Note;
import icynote.ui.BlankActivity;
import icynote.ui.contracts.NoteOpenerBase;
import icynote.ui.contracts.NotesPresenter;
import icynote.ui.fragments.NotesList;

/**
 * Created by kl on 23.11.2016.
 */
public class MockNotesList extends BlankActivity implements NotesList.Contract {

    public boolean createNote = false;
    private boolean openNote = false;
    public boolean deleteNote = false;

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
