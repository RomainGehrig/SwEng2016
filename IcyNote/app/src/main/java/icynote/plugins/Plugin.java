package icynote.plugins;

import android.content.Intent;

public interface Plugin {
    boolean canHandle(int requestCode);
    void handle(int requestCode, int resultCode, Intent data, PluginData state);
    String getName();
    void setEnabled(boolean enabled);
    boolean isEnabled();
}
