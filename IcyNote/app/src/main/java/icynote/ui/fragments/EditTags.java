package icynote.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import icynote.ui.R;

public class EditTags extends FragmentWithState {

    public EditTags() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the layout using the cloned inflater, not default inflater
        return inflater.inflate(R.layout.fragment_edit_tags, container, false);
    }

}
