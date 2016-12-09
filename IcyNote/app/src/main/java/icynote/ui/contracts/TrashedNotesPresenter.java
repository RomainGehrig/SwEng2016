package icynote.ui.contracts;

import android.text.SpannableString;

import java.util.Collection;

import icynote.note.Note;

/**
 * The interface Trashed notes presenter.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public interface TrashedNotesPresenter extends NoteOpenerBase {
    /**
     * The interface Contract.
     */
    interface Contract extends NoteOpenerBase.Contract {
        /**
         * Restore trashed note.
         *
         * @param note      the note to be restored
         * @param requester the requester presenter
         */
        void restoreTrashedNote(Note<SpannableString> note, TrashedNotesPresenter requester);
    }

    /**
     * Receive notes.
     *
     * @param notes the notes
     */
    void receiveNotes(Collection<Note<SpannableString>> notes);

    /**
     * On trashed note restored success.
     *
     * @param id the id
     */
    void onTrashedNoteRestoredSuccess(Note<SpannableString> id);

    /**
     * On trashed note restored failure.
     *
     * @param note    the note that could'nt be restore
     * @param message the error message
     */
    void onTrashedNoteRestoredFailure(Note<SpannableString> note, String message);
}
