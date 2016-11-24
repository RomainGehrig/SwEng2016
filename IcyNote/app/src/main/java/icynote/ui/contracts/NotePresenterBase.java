package icynote.ui.contracts;

import android.text.SpannableString;

import icynote.note.Note;

public interface NotePresenterBase {
    interface Contract {
        void saveNote(Note< SpannableString > note, NotePresenterBase requester);
    }
    void receiveNote(Note<SpannableString> note);
    void onSaveNoteFailure(String message);
}
