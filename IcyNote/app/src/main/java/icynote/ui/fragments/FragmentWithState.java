package icynote.ui.fragments;

import android.support.v4.app.Fragment;

import icynote.plugins.PluginData;

/**
 * The fragment with state
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class FragmentWithState extends Fragment {
    private PluginData mState;

    /**
     * Sets the state.
     *
     * @param state the plugin data
     */
    public void setState(PluginData state) {
        mState = state;
    }

    /**
     * Gets the state.
     *
     * @return the plugin data
     */
    public PluginData appState() {
        return mState;
    }
}
