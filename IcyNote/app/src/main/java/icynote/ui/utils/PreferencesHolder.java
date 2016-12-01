package icynote.ui.utils;

import android.content.SharedPreferences;
import android.util.Log;

import icynote.noteproviders.OrderBy;
import icynote.noteproviders.OrderType;


public class PreferencesHolder {
    SharedPreferences pref;

    public PreferencesHolder(SharedPreferences sp) {
        pref = sp;
    }
    public boolean isPluginEnabled(String pluginName) {
        if (!pref.contains(pluginName)) {
            Log.e(this.getClass().getSimpleName(), "Preference for plugin was not set");
        }
        return pref.getBoolean(pluginName, false);
    }
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
