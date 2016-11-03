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
            if (!(pos == Theme.getTheme().toPosition())) {
                Theme.setTheme(pos);
                tellActivityToChangeTheme(Theme.getTheme());
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}
