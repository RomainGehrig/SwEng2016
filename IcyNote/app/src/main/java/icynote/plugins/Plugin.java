package icynote.plugins;

import android.content.Intent;

import icynote.ui.utils.ApplicationState;

public interface Plugin {
    boolean canHandle(int requestCode);
    void handle(int requestCode, int resultCode, Intent data, ApplicationState state);
}
