package icynote.ui.view;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import icynote.ui.R;


public class NotesListViewHolder {
    private ListView listView;
    private TextView tvNumNotes;
    private EditText searchBar;
    private Button btAdd;
    private Button btDelete;

    public NotesListViewHolder(View notesList) {
        listView = (ListView) notesList.findViewById(R.id.lvNotes);
        tvNumNotes = (TextView)notesList.findViewById(R.id.tvNumNotes);
        searchBar = (EditText) notesList.findViewById(R.id.searchBar);
        btAdd = (Button) notesList.findViewById(R.id.btAdd);
        btDelete = (Button) notesList.findViewById(R.id.btDelete);
    }

    public void enableAll() {
        tvNumNotes.setEnabled(true);
        searchBar.setEnabled(true);
        btAdd.setEnabled(true);
        btDelete.setEnabled(true);
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

    public Button getBtAdd() {
        return btAdd;
    }

    public Button getBtDelete() {
        return btDelete;
    }
}
