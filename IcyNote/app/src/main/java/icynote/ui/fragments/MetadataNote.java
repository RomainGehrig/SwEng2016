package icynote.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import icynote.plugins.FormatterPlugin;
import icynote.ui.R;

public class MetadataNote extends FragmentWithState {

    public MetadataNote() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        //final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), Theme.getTheme().toInt());

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater; //inflater.cloneInContext(contextThemeWrapper);

        // inflate the layout using the cloned inflater, not default inflater
        View v = localInflater.inflate(R.layout.fragment_metadata_note, container, false);


        LinearLayout layout = (LinearLayout) v.findViewById(R.id.layout);

        for(FormatterPlugin formatter : appState().getPluginProvider().getFormatters()) {
            for(View button : formatter.getMetaButtons(getActivity())) {
                layout.addView(button);
            }
        }
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setNoteInfo();
    }

    private void setNoteInfo() {

        // set title
        TextView titleTextView = (TextView) getView().findViewById(R.id.noteTitle);
        titleTextView.setText("Note Title");

        // set date
        TextView dateTextView = (TextView) getView().findViewById(R.id.noteCreationDate);
        dateTextView.setText("01.01.2000");

    }


}
