package icynote.ui;

import android.app.Application;
import android.os.Bundle;

/**
 * Created by mikebardet on 27.10.16.
 */

public class MyApp extends MainActivity{
    private static MyApp app;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        app = this;
    }

    public static MyApp getApp() {
        return app;
    }
}
