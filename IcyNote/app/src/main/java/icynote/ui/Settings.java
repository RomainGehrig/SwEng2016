package icynote.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import ch.epfl.sweng.project.R;


public class Settings extends Fragment {

    private Spinner spinnerStyles;

    public Settings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        // Spinner item selection Listener
        spinnerStyles.setOnItemSelectedListener(new CustomOnItemSelectedListener());
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

    }
}
