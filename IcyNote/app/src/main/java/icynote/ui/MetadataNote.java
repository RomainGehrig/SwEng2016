package icynote.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MetadataNote extends Fragment {

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
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), Theme.getTheme().toInt());

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        // inflate the layout using the cloned inflater, not default inflater
        View v = localInflater.inflate(R.layout.fragment_metadata_note, container, false);

        TextView checkBox1 = (TextView)v.findViewById(R.id.checkBox1);
        checkBox1.setTextColor(Theme.getTheme().getTextColor());
        TextView checkBox2 = (TextView)v.findViewById(R.id.checkBox2);
        checkBox2.setTextColor(Theme.getTheme().getTextColor());
        TextView checkBox3 = (TextView)v.findViewById(R.id.checkBox3);
        checkBox3.setTextColor(Theme.getTheme().getTextColor());

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
