package icynote.ui;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.util.Comparator;
import java.util.GregorianCalendar;

import icynote.core.Note;
import icynote.core.OrderBy;
import icynote.core.impl.NoteData;


public class NotesList extends Fragment {
    // tmp storage
    static public ArrayList<Note> notes = new ArrayList<>();

    private ListView listView;
    private NotesAdapter notesAdapter;
    private Spinner spinner;
    private View view;


    public NotesList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create some notes to display
        notes.clear();
        for(int i=0; i<10; i++){
            Note note = new NoteData();
            note.setTitle("Note number " + i + ".\n");
            note.setId(i);
            note.setCreation(new GregorianCalendar());
            note.setContent("Some content of note number" + note.getId() +
                    "\n Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                    "Duis ante nibh, accumsan vel dui ultrices, lacinia euismod nibh. " +
                    "Suspendisse aliquam lacus et cursus blandit." +
                    " Vivamus mattis accumsan vulputate. Cras bibendum justo" +
                    " quis ante mollis, at imperdiet augue condimentum. ");

            notes.add(note);
        }

        notesAdapter = new NotesAdapter(getActivity(), notes);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        Style.ColorSetting curr = Style.getStyle();
        container.setBackgroundColor(curr.getBackgroundColor());
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
                OrderBy choice = (OrderBy) parent.getItemAtPosition(position);
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
                    case TITLE_DOWN:
                        notesAdapter.sort(new Comparator<Note>() {
                            @Override
                            public int compare(Note note1, Note note2) {
                                return note2.getTitle().compareTo(note1.getTitle());
                            }
                        });
                        break;
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
                ((MainActivity)getActivity()).openEditNote(0);
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


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

}
