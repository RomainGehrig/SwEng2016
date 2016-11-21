package icynote.ui.fragments;

import android.support.v4.app.Fragment;

import icynote.ui.utils.ApplicationState;

public class FragmentWithState extends Fragment {
    private ApplicationState mState;

    public void setState(ApplicationState state) {
        mState = state;
    }

    public ApplicationState appState() {
        return mState;
    }
}
