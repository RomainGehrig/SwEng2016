package icynote.ui;

import android.view.View;
import android.widget.AdapterView;

public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        System.out.println("testtt");
        String selected = parent.getItemAtPosition(pos).toString();
        switch (selected) {
            case "Dark":
                Theme.changeToTheme((MyApp) MyApp.getApp(), Theme.ThemeType.DARK);
                //Style.setStyle(Style.ColorSetting.DARK);
                break;
            default:
                Theme.changeToTheme((MyApp) MyApp.getApp(), Theme.ThemeType.BRIGHT);
                //Style.setStyle(Style.ColorSetting.BRIGHT);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}
