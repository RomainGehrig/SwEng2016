package icynote.ui.contracts;

import android.text.SpannableString;

import java.util.List;

import icynote.note.Note;

public interface NotesPresenter {
    void receiveNotes(List<Note<SpannableString>> notes);
    void onOpenNoteFailure(String message);
    void onCreateNoteFailure(String message);
    void onDeleteNoteFailure(String message);
}
