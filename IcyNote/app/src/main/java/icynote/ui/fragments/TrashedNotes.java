package icynote.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;

import icynote.note.Note;
import icynote.ui.R;
import icynote.ui.contracts.TrashedNotesPresenter;
import icynote.ui.utils.NotesAdapter;
import icynote.ui.view.TrashedNotesViewHolder;

public class TrashedNotes extends Fragment
        implements TrashedNotesPresenter {
    private static final String LOG_TAG = TrashedNotes.class.getSimpleName();

    /** Utility class to hold the view items such as the buttons, text fields, etc. */
    private TrashedNotesViewHolder viewHolder;

    /** The listView adapter containing the list of notes. */
    private NotesAdapter notesAdapter;

    /** The attached contractor implementing this fragment's contract. */
    private Contract contractor;

    /** Indicates whether the notes were received */
    private ArrayList<Note<SpannableString>> notesReceived;

    //-------------------------------------------------------------------------------------

    public TrashedNotes() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_trashed_notes, container, false);
        viewHolder = new TrashedNotesViewHolder(view);
        registerForContextMenu(viewHolder.getListView());
        setViewListeners();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        enableViewIfNeeded();
    }

    @Override
    public void onResume() {
        super.onResume();
        contractor = (Contract) getActivity();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewHolder = null;
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
    public void receiveNotes(Collection<Note<SpannableString>> notes) {
        log("Received list of notes " + ((notes == null) ? "null" : notes.iterator().hasNext()));
        //need to create if notes received before fragment is resumed.

        notesReceived = new ArrayList<>(notes);
        enableViewIfNeeded();
    }
    @Override
    public void onOpenNoteFailure(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTrashedNoteRestoredFailure(Note<SpannableString> note, String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        log("note could not be restore: " + note.getId());
        notesAdapter.setEnabled(true, note);
    }

    @Override
    public void onTrashedNoteRestoredSuccess(Note<SpannableString> note) {
        log("note restored: " + note.getId());
        notesAdapter.deleteNote(note);
        numNotesChanged();
    }

    //-------------------------------------------------------------------------------------
    // View listeners

    private void enableViewIfNeeded() {
        if (getView() == null || notesReceived == null) {
            /*
            note: you cannot test if (viewHolder == null),
            because it can happen that the fragment is currently executing `onCreateView`,
            and `viewHolder` is created at the beginning of the method, but the view won't
            be updated until the method returns.
            Hence, it can happen that : (getView() == null) but (viewHolder != null).
             */
            log("not enabling view"
                    + ((getView() == null) ? " getView is null" : "")
                    + ((notesAdapter == null) ? " notesAdapter is null" : "" ));
            return;
        }
        log("enabling view");
        notesAdapter = getOrCreateAdapter();
        notesAdapter.setNotes(notesReceived);
        notesReceived = null; //not needed anymore.

        viewHolder.enableAll();
        viewHolder.getTvNumNotes().setText(notesAdapter.getCount() + " notes");
        viewHolder.getSearchBar().setHint("Enter text to find");
        viewHolder.getListView().setAdapter(null); //reset
        viewHolder.getListView().setAdapter(notesAdapter);
        setPlaceholderText();
    }
    private void setPlaceholderText() {
        if(notesAdapter.getCount() == 0){
            viewHolder.getListView().setVisibility(View.GONE);
            viewHolder.getEmptyText().setVisibility(View.VISIBLE);
        } else {
            viewHolder.getListView().setVisibility(View.VISIBLE);
            viewHolder.getEmptyText().setVisibility(View.GONE);
        }
    }

    private void setViewListeners() {
        viewHolder.getSearchBar().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                userFilteredNotesListener();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        viewHolder.getBtRestore().setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userRestoreNotesListener();
                    }
                });
    }
    private void userFilteredNotesListener() {
        if (notesAdapter != null) {
            Log.d(LOG_TAG, "filtering list: " + viewHolder.getSearchBar().getText());
            notesAdapter.getFilter().filter(viewHolder.getSearchBar().getText());
        }
    }
    ////
    private void userRestoreNotesListener() {
        ArrayList<Note<SpannableString>> toDelete = new ArrayList();

        //first make a copy to avoid concurrency issues
        for (int i = 0; i < notesAdapter.getCount(); ++i) {
            NotesAdapter.Bucket bucket = notesAdapter.getItem(i);
            if (bucket != null && bucket.isChecked()) {
                bucket.setEnabled(false);
                toDelete.add(bucket.getNote());
            }
        }
        notesAdapter.notifyDataSetChanged();

        for (Note<SpannableString> n : toDelete) {
            contractor.restoreTrashedNote(n, this);
        }
    }
    private void numNotesChanged() {
        viewHolder.getTvNumNotes().setText(notesAdapter.getCount() + "notes");
        setPlaceholderText();
    }

    private NotesAdapter getOrCreateAdapter() {
        if (notesAdapter == null) {
            notesAdapter = new NotesAdapter(
                    getActivity(),
                    new NotesAdapter.BucketClickedListener() {
                        @Override
                        public void onClick(NotesAdapter.Bucket b) {
                            b.setChecked(!b.isChecked());
                            notesAdapter.notifyDataSetChanged();
                        }
                    });
        }
        return notesAdapter;
    }

    private void log(String msg) {
        Log.d(this.getClass().getSimpleName(), msg);
    }
}