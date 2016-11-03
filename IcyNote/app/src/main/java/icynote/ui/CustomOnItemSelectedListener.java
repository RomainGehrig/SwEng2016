package icynote.ui;

import android.view.View;
import android.widget.AdapterView;

public class CustomOnItemSelectedListener extends Settings implements AdapterView.OnItemSelectedListener {

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (firstSelection == 1) {
            firstSelectionProcessed();
        }
        else {
            System.out.println("testtt");
            String selectedTheme = parent.getItemAtPosition(pos).toString();
            if (!selectedTheme.equals(Theme.getTheme().toString())) {
                switch (selectedTheme) {
                    case "Dark":
                        Theme.setTheme(Theme.ThemeType.DARK);
                        break;
                    default:
                        Theme.setTheme(Theme.ThemeType.BRIGHT);
                        break;
                }
                tellActivityToChangeTheme(Theme.getTheme());
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }
}
