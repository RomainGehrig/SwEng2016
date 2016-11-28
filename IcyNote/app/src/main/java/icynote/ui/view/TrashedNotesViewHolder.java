package icynote.ui.view;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import icynote.ui.R;


public class TrashedNotesViewHolder {
    private ListView listView;
    private TextView tvNumNotes;
    private EditText searchBar;
    private Button btRestore;
    private TextView emptyText;


    public TrashedNotesViewHolder(View notesList) {
        listView = (ListView) notesList.findViewById(R.id.lvNotes);
        tvNumNotes = (TextView)notesList.findViewById(R.id.tvNumNotes);
        searchBar = (EditText) notesList.findViewById(R.id.searchBar);
        btRestore = (Button) notesList.findViewById(R.id.btRestore);
        emptyText = (TextView) notesList.findViewById(R.id.emptyText);

    }

    public void enableAll() {
        tvNumNotes.setEnabled(true);
        searchBar.setEnabled(true);
        btRestore.setEnabled(true);
    }

    public ListView getListView() {
        return listView;
    }

    public TextView getTvNumNotes() {
        return tvNumNotes;
    }

    public EditText getSearchBar() {
        return searchBar;
    }

    public Button getBtRestore() {
        return btRestore;
    }

    public TextView getEmptyText() {
        return emptyText;
    }
}
