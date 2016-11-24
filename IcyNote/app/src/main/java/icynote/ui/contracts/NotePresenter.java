package icynote.ui.contracts;

import android.text.SpannableString;

import icynote.note.Note;

public interface NotePresenter {
    interface Contract {
        void saveNote(Note<SpannableString> note, NotePresenter requester);
    }
    void receiveNote(Note<SpannableString> note);
    void onSaveNoteFailure(String message);
}
