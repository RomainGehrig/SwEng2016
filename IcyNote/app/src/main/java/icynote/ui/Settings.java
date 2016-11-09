package icynote.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;


public class Settings extends Fragment {

    private Spinner spinnerStyles;
    protected int firstSelection = 1;
    private static OnSpinnerSelection mCallback;

    public Settings() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnSpinnerSelection) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnSpinnerSelection");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), Theme.getTheme().toInt());

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        // inflate the layout using the cloned inflater, not default inflater
        View view = localInflater.inflate(R.layout.fragment_settings, container, false);

        setSpinnerStyles(view);

        return view;
    }

    // Set up the style spinner
    private void setSpinnerStyles(View view) {
        spinnerStyles = (Spinner) view.findViewById(R.id.styles_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), Theme.getTheme().getSpinnerView(),
                getResources().getStringArray(R.array.styles_list));
        adapter.setDropDownViewResource(Theme.getTheme().getSpinnerDropDown());
        spinnerStyles.setAdapter(adapter);

        // Match the current item of the spinner with the current theme
        spinnerStyles.setSelection(Theme.getTheme().toPosition());
        spinnerStyles.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public void tellActivityToChangeTheme(Theme.ThemeType newTheme)
    {
        mCallback.onThemeSelected(newTheme);
    }

    public void firstSelectionProcessed()
    {
        firstSelection = 0;
    }

    // Container Activity must implement this interface
    public interface OnSpinnerSelection {
        public void onThemeSelected(Theme.ThemeType newTheme);
    }

}
