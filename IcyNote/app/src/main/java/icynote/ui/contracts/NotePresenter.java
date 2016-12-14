package icynote.ui.contracts;

/**
 * The interface Note presenter.
 */
public interface NotePresenter extends NotePresenterBase {
    /**
     * The interface Contract.
     */
    interface Contract extends NotePresenterBase.Contract{
        /**
         * Open optional presenter.
         *
         * @param requester the requester
         */
        void openOptionalPresenter(NotePresenter requester);

        /**
         * Update selection.
         *
         * @param start the start
         * @param end   the end
         */
        void updateSelection(int start, int end);
    }

    /**
     * On open opt presenter failure.
     *
     * @param message the message
     */
    void onOpenOptPresenterFailure(String message);
}
