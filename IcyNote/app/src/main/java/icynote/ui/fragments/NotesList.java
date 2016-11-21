package icynote.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.SpannableString;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import icynote.loaders.NotesLoader;
import icynote.note.Note;
import icynote.note.Response;
import icynote.ui.MainActivity;
import icynote.ui.R;
import icynote.ui.utils.CanDeleteNote;
import icynote.ui.utils.NotesAdapter;

public class NotesList
        extends FragmentWithState
        implements LoaderManager.LoaderCallbacks<Iterable<Note<SpannableString>>>, CanDeleteNote
{
    private static final String TAG = "NotesList";
    private Loader<Iterable<Note<SpannableString>>> loader;

    private ListView listView;
    private NotesAdapter notesAdapter;
    private Spinner spinner;
    private View view;
    private ArrayList<Note<SpannableString>> notes = new ArrayList<>();

    public NotesList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notesAdapter = new NotesAdapter(getActivity(), notes, this);

        if (savedInstanceState != null) {
            Log.i(TAG, "savedInstanceState was not null ! (could save content)");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        appState()
                .getLoaderManager()
                .restartLoader(NotesLoader.LOADER_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_notes_list, container, false);

        listView = (ListView) view.findViewById(R.id.lvNotes);
        listView.setAdapter(null); // to avoid adding the notes twice
        listView.setAdapter(notesAdapter);

        //RelativeLayout layout_2 = (RelativeLayout)view.findViewById(R.id.layout_2);
        //layout_2.setBackgroundColor(Color.rgb(255, 255, 204));

        final TextView tvNumNotes = (TextView)view.findViewById(R.id.tvNumNotes);
        tvNumNotes.setText(notesAdapter.getCount()+ " notes");
        //tvNumNotes.setBackgroundColor(Color.WHITE);

        registerForContextMenu(listView);

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
                ((MainActivity)getActivity()).openEditNewNote();
            }
        });

        Button btDelete = (Button) view.findViewById(R.id.btDelete);
        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Note<SpannableString>> checked = notesAdapter.getCheckedNotes();
                for (Note<SpannableString> n : checked) {
                    Log.i(TAG, "deleting note: " + n.getTitle());
                    notesAdapter.deleteNote(n);
                    deleteNote(n.getId());
                    tvNumNotes.setText(notesAdapter.getCount()+ " notes");
                }
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
    public Response deleteNote(int noteId) {
        return appState().getNoteProvider().delete(noteId);
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
    public Loader<Iterable<Note<SpannableString>>> onCreateLoader(int id, Bundle args) {
        // TODO: indicate we are now waiting for the note
        Log.i(TAG, "Loader initialized");
        loader = new NotesLoader(getContext(), appState().getNoteProvider());
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Iterable<Note<SpannableString>>> loader, Iterable<Note<SpannableString>> data) {
        // TODO display notes
        Log.i(TAG, "Received notes");
        notes.clear();
        for (Note<SpannableString> n: data) {
            notes.add(n);
        }
        notesAdapter.notifyDataSetChanged();
        final TextView tvNumNotes = (TextView)view.findViewById(R.id.tvNumNotes);
        tvNumNotes.setText(notesAdapter.getCount()+ " notes");
    }

    @Override
    public void onLoaderReset(Loader<Iterable<Note<SpannableString>>> loader) {
        Log.i(TAG, "Loader reset");
        // TODO: anything to do ?
    }
}
