package icynote.ui.contracts;

import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public interface NoteOptionsPresenter extends NotePresenterBase {
    interface Contract extends NotePresenterBase.Contract {
        void optionPresenterFinished(NoteOptionsPresenter finished);
    }
    void receivePluginData(ArrayList<View> pluginActions);
}
