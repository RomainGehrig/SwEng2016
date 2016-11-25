package icynote.ui.contracts;


public interface NoteOpenerBase {
    interface Contract {
        void openNote(int id, NoteOpenerBase requester);
    }
    void onOpenNoteFailure(String message);
}