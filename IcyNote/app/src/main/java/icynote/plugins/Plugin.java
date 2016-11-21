package icynote.plugins;

import android.app.Activity;
import android.content.Intent;

public interface Plugin {
    boolean canHandle(int requestCode);
    void handle(int requestCode, int resultCode, Intent data, Activity activity);
}
