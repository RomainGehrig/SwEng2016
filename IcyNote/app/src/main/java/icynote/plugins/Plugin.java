package icynote.plugins;

import android.content.Intent;
import android.view.View;

/**
 * The Plugin interface.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public interface Plugin {
    /**
     * Can handle boolean.
     *
     * @param requestCode the request code
     * @return true if the plugin can handle the request code
     */
    boolean canHandle(int requestCode);

    /**
     * Handle
     *
     * @param requestCode the request code
     * @param resultCode  the result code
     * @param data        the intent data
     * @param state       the plugin data
     */
    void handle(int requestCode, int resultCode, Intent data, PluginData state);

    /**
     * Gets name.
     *
     * @return the name
     */
    String getName();

    /**
     * Gets meta buttons.
     *
     * @param state the state
     * @return the meta buttons
     */
    Iterable<View> getMetaButtons(PluginData state);


    /**
     * Sets enabled.
     *
     * @param enabled
     */
    void setEnabled(boolean enabled);

    /**
     * Return a boolean to know if enabled.
     *
     * @return true if it is enabled
     */
    boolean isEnabled();
}
