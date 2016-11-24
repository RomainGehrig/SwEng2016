package icynote.ui.contracts;

public interface NotePresenter extends NotePresenterBase {
    interface Contract extends NotePresenterBase.Contract{
        void openOptionalPresenter(NotePresenter requester);
    }
    void onOpenOptPresenterFailure(String message);
}
