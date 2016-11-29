package icynote.ui.contracts;

public interface PluginPresenter extends NoteOpenerBase, NotePresenterBase {
    interface Contract extends NoteOpenerBase.Contract, NotePresenterBase.Contract {
        void registerOnStartCallback(util.Callback executeOnActivityStart);
    }
}
