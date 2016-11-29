package icynote.ui.contracts;

import android.text.SpannableString;

import java.util.Collection;

import icynote.note.Note;

public interface TrashedNotesPresenter extends NoteOpenerBase {
    interface Contract extends NoteOpenerBase.Contract {
        void restoreTrashedNote(Note<SpannableString> note, TrashedNotesPresenter requester);
    }

    void receiveNotes(Collection<Note<SpannableString>> notes);
    void onTrashedNoteRestoredSuccess(Note<SpannableString> id);
    void onTrashedNoteRestoredFailure(Note<SpannableString> note, String message);
}
