package icynote.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import icynote.note.Note;
import icynote.ui.R;
import icynote.ui.contracts.NotesPresenter;
import icynote.ui.utils.NotesAdapter;
import icynote.ui.view.NotesListViewHolder;


public class NotesList extends Fragment implements NotesPresenter {


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface Contract {
        void openNote(int id);
        void createNote();
        void deleteNotes(List<Integer> notes);
    }

    //-------------------------------------------------------------------------------------

    private static final String TAG = "NotesList";

    /** Utility class to hold the view items such as the buttons, text fields, etc. */
    private NotesListViewHolder viewHolder;

    /** The listView adapter containing the list of notes. */
    private NotesAdapter notesAdapter;

    /** The attached activity implementing this fragment's contract. */
    private Contract activity;

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
        activity = (Contract) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_notes_list, container, false);

        //utility class to access the item in the layout
        viewHolder = new NotesListViewHolder(view);

        registerForContextMenu(viewHolder.getListView());

        viewHolder.getSearchBar().setOnEditorActionListener(
                new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    userFilterNotesListener();
                }
                return false;
            }
        });
        viewHolder.getBtAdd().setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userCreateNoteListener();
            }
        });

        viewHolder.getBtDelete().setOnClickListener(
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDeleteNotesListener();
            }
        });

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

    @Override
    public void receiveNotes(List<Note<SpannableString>> notes) {
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
    public void onDeleteNoteFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        restaureNoteSetForDeletion();
    }

    //-------------------------------------------------------------------------------------

    private void enableView() {
        viewHolder.enableAll();
        viewHolder.getTvNumNotes().setText(notesAdapter.getCount() + "notes");
        viewHolder.getSearchBar().setHint("Enter text to find");
    }
    private void restaureNoteSetForDeletion() {
        //List<notesAdapter.getCheckedNotes();
    }
    private void userFilterNotesListener() {
        if (notesAdapter != null) {
            notesAdapter.getFilter().filter(viewHolder.getSearchBar().getText());
        }
    }
    private void userCreateNoteListener() {
        activity.createNote();
    }
    private void userDeleteNotesListener() {
        List<Note<SpannableString>> toDelete = notesAdapter.getCheckedNotes();
        ArrayList<Integer> toDeleteIds = new ArrayList<>();
        for(Note<SpannableString> note : toDelete) {
            toDeleteIds.add(note.getId());
            notesAdapter.deleteNote(note);
        }
        activity.deleteNotes(toDeleteIds);
    }
}
