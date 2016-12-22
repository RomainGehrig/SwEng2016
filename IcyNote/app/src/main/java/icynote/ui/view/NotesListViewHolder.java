package icynote.ui.view;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import icynote.ui.R;


/**
 * The notes list view holder.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class NotesListViewHolder {
    private final ListView listView;
    private final TextView tvNumNotes;
    private final EditText searchBar;
    private final Button btAdd;
    private final Button btDelete;
    private final TextView emptyText;


    /**
     * Instantiates a new Notes list view holder.
     *
     * @param notesList the notes list
     */
    public NotesListViewHolder(View notesList) {
        listView = (ListView) notesList.findViewById(R.id.lvNotes);
        tvNumNotes = (TextView)notesList.findViewById(R.id.tvNumNotes);
        searchBar = (EditText) notesList.findViewById(R.id.searchBar);
        btAdd = (Button) notesList.findViewById(R.id.btAdd);
        btDelete = (Button) notesList.findViewById(R.id.btDelete);
        emptyText = (TextView) notesList.findViewById(R.id.emptyText);

    }

    /**
     * Enables all the elements.
     */
    public void enableAll() {
        tvNumNotes.setEnabled(true);
        searchBar.setEnabled(true);
        btAdd.setEnabled(true);
        btDelete.setEnabled(true);
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
     * Gets the tv num notes.
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
     * Gets the buttun add.
     *
     * @return the button add
     */
    public Button getBtAdd() {
        return btAdd;
    }

    /**
     * Gets the button delete.
     *
     * @return the button delete
     */
    public Button getBtDelete() {
        return btDelete;
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
