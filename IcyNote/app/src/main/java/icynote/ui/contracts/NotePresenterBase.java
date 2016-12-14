package icynote.ui.contracts;

import android.text.SpannableString;

import icynote.note.Note;

/**
 * The interface Note presenter base.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public interface NotePresenterBase {
    /**
     * The interface Contract.
     */
    interface Contract {
        /**
         * Save note.
         *
         * @param note      the note
         * @param requester the requester
         */
        void saveNote(Note< SpannableString > note, NotePresenterBase requester);
    }

    /**
     * Receive note.
     *
     * @param note the note to be received
     */
    void receiveNote(Note<SpannableString> note);

    /**
     * On save note failure.
     *
     * @param message the error message
     */
    void onSaveNoteFailure(String message);
}
