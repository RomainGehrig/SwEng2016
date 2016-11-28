package icynote.ui.contracts;

import android.text.SpannableString;

import icynote.note.Note;

public interface TrashedNotesPresenter extends NoteOpenerBase {
    interface Contract extends NoteOpenerBase.Contract {
        void deleteTrashedNote(Note<SpannableString> note, TrashedNotesPresenter requester);
    }

    void receiveNotes(Iterable<Note<SpannableString>> notes);
    void onTrashedNoteDeletionFailure(Note<SpannableString> note, String message);
    void onTrashedNoteDeletionSuccess(Note<SpannableString> id);
}
