package icynote.ui.utils;

import android.content.SharedPreferences;
import android.util.Log;

import icynote.noteproviders.OrderBy;
import icynote.noteproviders.OrderType;


/**
 * The class for the preferences holder.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class PreferencesHolder {
    private final SharedPreferences pref;

    /**
     * Instantiates a new Preferences holder.
     *
     * @param sp shared preferences
     */
    public PreferencesHolder(SharedPreferences sp) {
        pref = sp;
    }

    /**
     * Checks wheter the plugin is enables.
     *
     * @param pluginName the plugin name
     * @return true if the plugin is enables
     */
    public boolean isPluginEnabled(String pluginName) {
        if (!pref.contains(pluginName)) {
            Log.e(this.getClass().getSimpleName(), "Preference for plugin was not set");
        }
        return pref.getBoolean(pluginName, false);
    }

    /**
     * Gets sort index.
     *
     * @return OrderBy
     */
    public OrderBy getSortIndex() {
        String v = pref.getString("select_sort_by", "");
        switch (v) {
            case "TITLE":
                return OrderBy.TITLE;
            case "CREATION":
                return OrderBy.CREATION;
            default:
                return OrderBy.LAST_UPDATE;
        }
    }

    /**
     * Gets order type.
     *
     * @return the order type
     */
    public OrderType getOrderType() {
        String v = pref.getString("select_sort_type", "");
        switch (v) {
            case "DSC":
                return OrderType.DSC;
            default:
                return OrderType.ASC;
        }
    }
}
