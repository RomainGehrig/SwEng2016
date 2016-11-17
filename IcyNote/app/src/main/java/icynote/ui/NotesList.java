package icynote.ui;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import icynote.core.Note;
import icynote.core.OrderBy;
import icynote.loaders.NotesLoader;

import static util.ArgumentChecker.requireNonNull;

public class NotesList extends FragmentWithCoreAndLoader implements LoaderManager.LoaderCallbacks<Iterable<Note>> {
    private static final String TAG = "NotesList";
    private Loader<Iterable<Note>> loader;

    private ListView listView;
    private NotesAdapter notesAdapter;
    private Spinner spinner;
    private View view;
    private ArrayList<Note> notes = new ArrayList<>();

    public NotesList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notesAdapter = new NotesAdapter(getActivity(), notes);

        if (savedInstanceState != null) {
            Log.i(TAG, "savedInstanceState was not null ! (could save content)");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getThisLoaderManager().initLoader(1, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_notes_list, container, false);

        listView = (ListView) view.findViewById(R.id.lvNotes);
        listView.setAdapter(null); // to avoid adding the notes twice
        listView.setAdapter(notesAdapter);


        //Once adapter created, new elements may be added to
        /*
        Note noteNew1 = new NoteData();
        noteNew1.setTitle("New Note");
        noteNew1.setContent("\nNew content: line number 1 \nand line number 2.");
        noteNew1.setCreation(new GregorianCalendar());
        notesAdapter.add(noteNew1);
        */

        RelativeLayout layout_2 = (RelativeLayout)view.findViewById(R.id.layout_2);
        layout_2.setBackgroundColor(Color.rgb(255, 255, 204));

        TextView tvNumNotes = (TextView)view.findViewById(R.id.tvNumNotes);
        tvNumNotes.setText(notesAdapter.getCount()+ " notes");
        tvNumNotes.setBackgroundColor(Color.WHITE);

        registerForContextMenu(listView);

        spinner = (Spinner) view.findViewById(R.id.spinner);

        ArrayList<OrderBy> sortingTypes = new ArrayList<>();
        for(OrderBy el : OrderBy.values()) {
            sortingTypes.add(el);
        }

        ArrayAdapter<OrderBy> adapter =
                new ArrayAdapter<>(view.getContext(), android.R.layout.simple_spinner_item, sortingTypes);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setBackgroundColor(Color.LTGRAY);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //OrderBy choice = (OrderBy) parent.getItemAtPosition(position);
                OrderBy choice = CurrentPreferences.getOrderBy();
                switch(choice)
                {
                    case TITLE:
                        notesAdapter.sort(new Comparator<Note>() {
                            @Override
                            public int compare(Note note1, Note note2) {
                                return note1.getTitle().compareTo(note2.getTitle());
                            }
                        });
                        break;
                    /*case TITLE_DOWN:
                        notesAdapter.sort(new Comparator<Note>() {
                            @Override
                            public int compare(Note note1, Note note2) {
                                return note2.getTitle().compareTo(note1.getTitle());
                            }
                        });
                        break;*/
                    case CREATION:
                        notesAdapter.sort(new Comparator<Note>() {
                            @Override
                            public int compare(Note note1, Note note2) {
                                return note1.getCreation().compareTo(note2.getCreation());
                            }
                        });
                        break;
                    case LAST_UPDATE:
                        notesAdapter.sort(new Comparator<Note>() {
                            @Override
                            public int compare(Note note1, Note note2) {
                                return note1.getLastUpdate().compareTo(note2.getLastUpdate());
                            }
                        });
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // initialize search button
        Button btSearch = (Button) view.findViewById(R.id.btSearch);
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = (EditText) view.getRootView().findViewById(R.id.editText);
                editText.setVisibility(View.VISIBLE);
                notesAdapter.getFilter().filter(editText.getText());
            }
        });

        EditText editText = (EditText) view.getRootView().findViewById(R.id.editText);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    notesAdapter.getFilter().filter(v.getText());
                }
                return false;
            }
        });


        // Add a new note button
        Button btAdd = (Button) view.findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create a new note with the core, and get its id
                // TODO
                //((MainActivity)getActivity()).openEditNote(0);
            }
        });

        return  view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View vue, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, vue, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.notes_list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete_id:
                notes.remove(info.position);
                notesAdapter.notifyDataSetChanged();
                TextView tvNumNotes = (TextView) view.findViewById(R.id.tvNumNotes);
                tvNumNotes.setText(notesAdapter.getCount()+ " notes");
                return true;
            case R.id.retour_id:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */
    @Override
    public Loader<Iterable<Note>> onCreateLoader(int id, Bundle args) {
        // TODO: indicate we are now waiting for the note
        Log.i(TAG, "Loader initialized");
        loader = new NotesLoader(getContext(), getCore());
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Iterable<Note>> loader, Iterable<Note> data) {
        // TODO display notes
        Log.i(TAG, "Received notes");
        notes.clear();
        for (Note n: data) {
            notes.add(n);
        }

        notesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Iterable<Note>> loader) {
        Log.i(TAG, "Loader reset");
        // TODO: anything to do ?
    }
}
