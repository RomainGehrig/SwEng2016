package icynote.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class Settings extends Fragment {

    private Spinner spinnerStyles;
    private static OnSpinnerSelection mCallback;

    public Settings() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        System.out.println("*** On Attach test *****");
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnSpinnerSelection) context;
            System.out.println("*** mCallback : " + mCallback);
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
        //Style.ColorSetting curr = Style.getStyle();
        //container.setBackgroundColor(curr.getBackgroundColor());
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        setSpinnerStyles(view);

        return view;
    }


    // Set up the style spinner
    private void setSpinnerStyles(View view) {
        spinnerStyles = (Spinner) view.findViewById(R.id.styles_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_view,
                getResources().getStringArray(R.array.styles_list));
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinnerStyles.setAdapter(adapter);

        // Match the current item of the spinner with the current theme
        String currentTheme = Theme.getTheme().toString();
        int themeSpinnerIndex = adapter.getPosition(currentTheme);
        spinnerStyles.setSelection(themeSpinnerIndex);

        // Spinner item selection Listener
        spinnerStyles.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public void tellActivityToChangeTheme(Theme.ThemeType newTheme)
    {
        System.out.println("*** mCallback in tell : " + mCallback);
        mCallback.onThemeSelected(newTheme);
        //currentTheme = theme;
        //activity.finish();
        //activity.startActivity(new Intent(activity, activity.getClass()));
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }*/



    // Container Activity must implement this interface
    public interface OnSpinnerSelection {
        public void onThemeSelected(Theme.ThemeType newTheme);
    }

}
