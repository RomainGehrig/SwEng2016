package icynote.ui.contracts;

public interface NotePresenter extends NotePresenterBase {
    interface Contract extends NotePresenterBase.Contract{
        void openOptionalPresenter(NotePresenter requester);
        void updateSelection(int start, int end);
    }
    void onOpenOptPresenterFailure(String message);
}
