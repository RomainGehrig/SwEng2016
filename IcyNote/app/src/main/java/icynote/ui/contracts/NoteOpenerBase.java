package icynote.ui.contracts;


/**
 * The interface Note opener base.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public interface NoteOpenerBase {
    /**
     * The interface Contract.
     */
    interface Contract {
        /**
         * Open note.
         *
         * @param id        the id
         * @param requester the requester
         */
        void openNote(int id, NoteOpenerBase requester);

        /**
         * Re open last opened note.
         *
         * @param requester the requester
         */
        void reOpenLastOpenedNote(NoteOpenerBase requester);
    }

    /**
     * On open note failure.
     *
     * @param message the error message
     */
    void onOpenNoteFailure(String message);
}
