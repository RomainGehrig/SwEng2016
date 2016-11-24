package icynote.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import icynote.note.Note;
import icynote.ui.R;
import icynote.ui.contracts.NotesPresenter;
import icynote.ui.utils.NotesAdapter;
import icynote.ui.view.NotesListViewHolder;

public class NotesList extends Fragment
        implements NotesPresenter {
    private static final String LOG_TAG = NotesList.class.getSimpleName();

    /** Utility class to hold the view items such as the buttons, text fields, etc. */
    private NotesListViewHolder viewHolder;

    /** The listView adapter containing the list of notes. */
    private NotesAdapter notesAdapter;

    /** The attached contractor implementing this fragment's contract. */
    private Contract contractor;

    /** Indicates whether receivedNote was called or not */
    private boolean notesReceived = false;

    //-------------------------------------------------------------------------------------

    public NotesList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        contractor = (Contract) getActivity();
        notesAdapter = new NotesAdapter(
                getActivity(),
                new NotesAdapter.BucketClickedListener() {
            @Override
            public void onClick(NotesAdapter.Bucket b) {
                contractor.openNote(b.note.getId(), NotesList.this);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);
        viewHolder = new NotesListViewHolder(view);
        registerForContextMenu(viewHolder.getListView());
        setViewListeners();

        if (notesReceived) {
            enableView();
        }
        return  view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu,
                                    View vue,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, vue, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.notes_list_context_menu, menu);
    }

    //-------------------------------------------------------------------------------------
    // NotePresenter callbacks

    @Override
    public void receiveNotes(Iterable<Note<SpannableString>> notes) {
        Toast.makeText(getActivity(), "notes received !", Toast.LENGTH_LONG).show();
        notesAdapter.setNotes(notes);
        enableView();
    }
    @Override
    public void onOpenNoteFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onCreateNoteFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoteDeletionFailure(Note<SpannableString> note, String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        log("note could not be deleted: " + note.getId());
        notesAdapter.setEnabled(true, note);
    }

    @Override
    public void onNoteDeletionSuccess(Note<SpannableString> note) {
        log("note deleted: " + note.getId());
        notesAdapter.deleteNote(note);
        numNotesChanged();
    }

    //-------------------------------------------------------------------------------------
    // View listeners

    private void enableView() {
        viewHolder.enableAll();
        viewHolder.getTvNumNotes().setText(notesAdapter.getCount() + "notes");
        viewHolder.getSearchBar().setHint("Enter text to find");
        viewHolder.getListView().setAdapter(null); //reset
        viewHolder.getListView().setAdapter(notesAdapter);
    }
    private void setViewListeners() {
        /*viewHolder.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(NotesList.this.getActivity(), "clicked", Toast.LENGTH_SHORT).show();
                Note<SpannableString> clickedNote = notesAdapter.getItem(position).note;
                userOpenedNoteListener(clickedNote.getId());
            }
        });*/
        viewHolder.getSearchBar().setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            userFilteredNotesListener();
                        }
                        return false;
                    }
                });
        viewHolder.getBtAdd().setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userCreatedNoteListener();
                    }
                });

        viewHolder.getBtDelete().setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userDeletedNotesListener();
                    }
                });
    }
    private void userFilteredNotesListener() {
        if (notesAdapter != null) {
            notesAdapter.getFilter().filter(viewHolder.getSearchBar().getText());
        }
    }
    private void userCreatedNoteListener() {
        contractor.createNote(this);
    }
    private void userDeletedNotesListener() {
        for(int i = 0; i < notesAdapter.getCount(); ++i) {
            NotesAdapter.Bucket bucket = notesAdapter.getItem(i);
            if (bucket != null && bucket.checked) {
                bucket.enabled = false;
                contractor.deleteNote(bucket.note, this);
            }
        }
    }
    private void userOpenedNoteListener(int id) {
        contractor.openNote(id, this);
    }
    private void numNotesChanged() {
        viewHolder.getTvNumNotes().setText(notesAdapter.getCount() + "notes");
    }
    private void log(String msg) {
        Log.d(this.getClass().getSimpleName(), msg);
    }
}
