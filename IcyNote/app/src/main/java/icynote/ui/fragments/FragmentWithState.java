package icynote.ui.fragments;

import android.support.v4.app.Fragment;

import icynote.plugins.PluginData;

public class FragmentWithState extends Fragment {
    private PluginData mState;

    public void setState(PluginData state) {
        mState = state;
    }

    public PluginData appState() {
        return mState;
    }
}
