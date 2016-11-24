package icynote.ui.contracts;

import android.text.SpannableString;

import icynote.note.Note;

public interface NotesPresenter extends NoteOpenerBase {
    interface Contract extends NoteOpenerBase.Contract {
        void createNote(NotesPresenter requester);
        void deleteNote(Note<SpannableString> note, NotesPresenter requester);
    }

    void receiveNotes(Iterable<Note<SpannableString>> notes);
    void onCreateNoteFailure(String message);
    void onNoteDeletionFailure(Note<SpannableString> note, String message);
    void onNoteDeletionSuccess(Note<SpannableString> id);
}
