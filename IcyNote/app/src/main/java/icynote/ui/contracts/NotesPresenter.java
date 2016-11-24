package icynote.ui.contracts;

import android.text.SpannableString;

import icynote.note.Note;

public interface NotesPresenter {
    interface Contract {
        void openNote(int id, NotesPresenter requester);
        void createNote(NotesPresenter requester);
        void deleteNote(Note<SpannableString> note, NotesPresenter requester);
    }

    void receiveNotes(Iterable<Note<SpannableString>> notes);
    void onOpenNoteFailure(String message);
    void onCreateNoteFailure(String message);
    void onNoteDeletionFailure(Note<SpannableString> note, String message);
    void onNoteDeletionSuccess(Note<SpannableString> id);
}
