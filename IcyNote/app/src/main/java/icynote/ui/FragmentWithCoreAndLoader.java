package icynote.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;

import icynote.core.IcyNoteCore;

import static util.ArgumentChecker.requireNonNull;

public abstract class FragmentWithCoreAndLoader extends Fragment {
    private IcyNoteCore core;
    private LoaderManager loaderManager;

    public void setCore(IcyNoteCore core) {
        this.core = requireNonNull(core);
    }

    public void setLoaderManager(LoaderManager loaderManager) {
        this.loaderManager = requireNonNull(loaderManager);
    }

    protected IcyNoteCore getCore() {
        return core;
    }

    // Name conflict with Fragment.getLoaderManager
    protected LoaderManager getThisLoaderManager() {
        return loaderManager;
    }
}
