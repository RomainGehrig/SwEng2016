package icynote.ui.contracts;

import android.text.SpannableString;

import icynote.note.Note;

/**
 * The interface Notes presenter
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public interface NotesPresenter extends NoteOpenerBase {
    /**
     * The interface Contract.
     */
    interface Contract extends NoteOpenerBase.Contract {
        /**
         * Create note.
         *
         * @param requester the requester
         */
        void createNote(NotesPresenter requester);

        /**
         * Delete note.
         *
         * @param note      the note to be deleted
         * @param requester the requester
         */
        void deleteNote(Note<SpannableString> note, NotesPresenter requester);
    }

    /**
     * Receive notes.
     *
     * @param notes the notes to be received
     */
    void receiveNotes(Iterable<Note<SpannableString>> notes);

    /**
     * On create note failure.
     *
     * @param message the error message
     */
    void onCreateNoteFailure(String message);

    /**
     * On note deletion failure.
     *
     * @param note    the note that couldn't be deleted
     * @param message the error message
     */
    void onNoteDeletionFailure(Note<SpannableString> note, String message);

    /**
     * On note deletion success.
     *
     * @param id the id of the deleted note
     */
    void onNoteDeletionSuccess(Note<SpannableString> id);
}
