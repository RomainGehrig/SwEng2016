package icynote.ui.contracts;

/**
 * The interface Plugin presenter.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public interface PluginPresenter extends NoteOpenerBase, NotePresenterBase {
    /**
     * The interface Contract.
     */
    interface Contract extends NoteOpenerBase.Contract, NotePresenterBase.Contract {
        /**
         * On start callback.
         *
         * @param executeOnActivityStart the
         */
        void registerOnStartCallback(util.Callback executeOnActivityStart);
    }
}
