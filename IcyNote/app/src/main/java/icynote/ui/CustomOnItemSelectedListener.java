package icynote.ui;

import android.view.View;
import android.widget.AdapterView;

public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        String selected = parent.getItemAtPosition(pos).toString();
        switch (selected) {
            case "Dark":
                Style.setStyle(Style.ColorSetting.DARK);
                break;
            default:
                Style.setStyle(Style.ColorSetting.BRIGHT);
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}
