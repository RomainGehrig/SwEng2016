package icynote.ui.contracts;

import android.view.View;

import java.util.ArrayList;

public interface NoteOptionsPresenter extends NotePresenterBase {
    interface Contract extends NotePresenterBase.Contract {
        void optionPresenterFinished(NoteOptionsPresenter finished);
    }
    void receivePluginData(ArrayList<View> pluginActions);
}
