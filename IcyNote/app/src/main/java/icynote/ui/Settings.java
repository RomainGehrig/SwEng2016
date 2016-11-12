package icynote.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import icynote.login.LoginManager;
import icynote.login.LoginManagerFactory;


public class Settings extends Fragment {

    static private final int RC_LINK_GOOGLE = 1505;
    static private final int RC_UNLINK_GOOGLE = 1505;

    private Spinner spinnerStyles;
    protected int firstSelection = 1;
    private static OnSpinnerSelection mCallback;
    private Button linkButton;
    private Button unlinkButton;

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


        linkButton = (Button) view.findViewById(R.id.linkGoogleButton);
        linkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                linkGoogleAccount();
            }
        });

        unlinkButton = (Button) view.findViewById(R.id.unlinkGoogleButton);
        unlinkButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unlinkGoogleAccount();
            }
        });
        updateLinkButtons();

        return view;
    }

    // Set up the style spinner
    private void setSpinnerStyles(View view) {
        spinnerStyles = (Spinner) view.findViewById(R.id.styles_list);
        ArrayAdapter<String> adapter;
        switch (Theme.getTheme())
        {
            case BRIGHT:
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_view_bright,
                        getResources().getStringArray(R.array.styles_list));
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_bright);
                break;
            case DARK:
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_view_dark,
                        getResources().getStringArray(R.array.styles_list));
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_dark);
                break;
            default:
                adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_view_bright,
                        getResources().getStringArray(R.array.styles_list));
                adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item_bright);
                break;
        }
        spinnerStyles.setAdapter(adapter);

        // Match the current item of the spinner with the current theme
        spinnerStyles.setSelection(Theme.getTheme().toPosition());
        spinnerStyles.setOnItemSelectedListener(new CustomOnItemSelectedListener());
    }

    public void tellActivityToChangeTheme(Theme.ThemeType newTheme)
    {
        mCallback.onThemeSelected(newTheme);
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

    public void firstSelectionProcessed()
    {
        firstSelection = 0;
    }

    // Container Activity must implement this interface
    public interface OnSpinnerSelection {
        public void onThemeSelected(Theme.ThemeType newTheme);
    }

    public void linkGoogleAccount() {
        Intent intent = new Intent(getContext(), GoogleLinkCredentials.class);
        startActivityForResult(intent, RC_LINK_GOOGLE);
    }

    public void unlinkGoogleAccount() {
        Intent intent = new Intent(getContext(), GoogleUnLinkCredentials.class);
        startActivityForResult(intent, RC_UNLINK_GOOGLE);
    }

    public void updateLinkButtons() {
        LoginManager loginManager = LoginManagerFactory.getInstance();

        if (loginManager.userCanLoginWithEmail()
                && loginManager.userCanLoginWithGoogle()) {
            unlinkButton.setVisibility(View.VISIBLE);
        } else {
            unlinkButton.setVisibility(View.INVISIBLE);
        }

        if (loginManager.userCanLoginWithEmail()
                && !loginManager.userCanLoginWithGoogle()) {
            linkButton.setVisibility(View.VISIBLE);
        } else {
            linkButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from linkWithGoogleAccount()
        if (requestCode == RC_LINK_GOOGLE) {
            if (resultCode == Activity.RESULT_OK) {
                String message = data.getStringExtra("googleLink.message");
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
            updateLinkButtons();
        }

        // Result returned from unlinkWithGoogleAccount()
        if (requestCode == RC_UNLINK_GOOGLE) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getContext(), "unlink successful!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getContext(), "sorry, unable to proceed", Toast.LENGTH_SHORT).show();
            }
            updateLinkButtons();
        }
    }
}
