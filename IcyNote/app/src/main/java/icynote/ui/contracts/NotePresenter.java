package icynote.ui.contracts;

import android.text.SpannableString;

import icynote.note.Note;

public interface NotePresenter {
    void receiveNote(Note<SpannableString> note);

}
