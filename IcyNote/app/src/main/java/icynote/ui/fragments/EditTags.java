package icynote.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import icynote.ui.R;

/**
 * The fragment to edit tags.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class EditTags extends FragmentWithState {

    /**
     * Instantiates a new Edit tags. Required empty public constructor
     */
    public EditTags() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the layout using the cloned inflater, not default inflater
        return inflater.inflate(R.layout.fragment_edit_tags, container, false);
    }

}
