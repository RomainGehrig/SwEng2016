package icynote.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
        // Inflate the layout for this fragment
        //Style.ColorSetting curr = Style.getStyle();
        //container.setBackgroundColor(curr.getBackgroundColor());
        return inflater.inflate(R.layout.fragment_metadata_note, container, false);
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
