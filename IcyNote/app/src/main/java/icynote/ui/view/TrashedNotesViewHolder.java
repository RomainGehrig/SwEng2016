package icynote.ui.view;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import icynote.ui.R;


/**
 * The trashed notes view holder.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class TrashedNotesViewHolder {
    private final ListView listView;
    private final TextView tvNumNotes;
    private final EditText searchBar;
    private final Button btRestore;
    private final TextView emptyText;


    /**
     * Instantiates a new Trashed notes view holder.
     *
     * @param notesList the notes list
     */
    public TrashedNotesViewHolder(View notesList) {
        listView = (ListView) notesList.findViewById(R.id.lvNotes);
        tvNumNotes = (TextView)notesList.findViewById(R.id.tvNumNotes);
        searchBar = (EditText) notesList.findViewById(R.id.searchBar);
        btRestore = (Button) notesList.findViewById(R.id.btRestore);
        emptyText = (TextView) notesList.findViewById(R.id.emptyText);

    }

    /**
     * Enable all the elements.
     */
    public void enableAll() {
        tvNumNotes.setEnabled(true);
        searchBar.setEnabled(true);
        btRestore.setEnabled(true);
    }

    /**
     * Gets the list view.
     *
     * @return the list view
     */
    public ListView getListView() {
        return listView;
    }

    /**
     * Gets the number of notes.
     *
     * @return the tv num notes
     */
    public TextView getTvNumNotes() {
        return tvNumNotes;
    }

    /**
     * Gets the search bar.
     *
     * @return the search bar
     */
    public EditText getSearchBar() {
        return searchBar;
    }

    /**
     * Gets the button restore.
     *
     * @return the button restore
     */
    public Button getBtRestore() {
        return btRestore;
    }

    /**
     * Gets the empty text.
     *
     * @return the empty text
     */
    public TextView getEmptyText() {
        return emptyText;
    }
}
