package icynote.ui;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.Set;

public class CustomOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        /*Toast.makeText(parent.getContext(),
                "On Item Select : \n" + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_LONG).show();*/
        String selected = parent.getItemAtPosition(pos).toString();
        switch (selected) {
            case "Dark":
                Settings.setStyle(Settings.ColorSetting.DARK);
                break;
            default:
                Settings.setStyle(Settings.ColorSetting.BRIGHT);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}
