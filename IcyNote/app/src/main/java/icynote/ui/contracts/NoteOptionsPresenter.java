package icynote.ui.contracts;

import android.view.View;

import java.util.ArrayList;

/**
 * The interface for Note options presenter.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public interface NoteOptionsPresenter extends NotePresenterBase {
    /**
     * The interface Contract.
     */
    interface Contract extends NotePresenterBase.Contract {
        /**
         * Option presenter finished.
         *
         * @param finished the NoteOptionsPresenter finished
         */
        void optionPresenterFinished(NoteOptionsPresenter finished);
    }

    /**
     * Receive plugin data.
     *
     * @param pluginActions the plugin actions
     */
    void receivePluginData(ArrayList<View> pluginActions);
}
