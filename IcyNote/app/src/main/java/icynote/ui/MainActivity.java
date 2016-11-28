package icynote.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;

import icynote.loaders.NoteLoader;
import icynote.loaders.NotesLoader;
import icynote.note.Note;
import icynote.note.Response;
import icynote.plugins.FormatterPlugin;
import icynote.plugins.Plugin;
import icynote.ui.contracts.NoteOpenerBase;
import icynote.ui.contracts.NoteOptionsPresenter;
import icynote.ui.contracts.NotePresenter;
import icynote.ui.contracts.NotePresenterBase;
import icynote.ui.contracts.NotesPresenter;
import icynote.ui.contracts.TrashedNotesPresenter;
import icynote.ui.fragments.EditNote;
import icynote.ui.fragments.EditTags;
import icynote.ui.fragments.MetadataNote;
import icynote.ui.fragments.NotesList;
import icynote.ui.fragments.Preferences;
import icynote.ui.fragments.TrashedNotes;
import icynote.ui.utils.ApplicationState;
import util.Optional;

@SuppressWarnings("TryWithIdenticalCatches") //we don't have API high enough for this.
public class MainActivity  extends AppCompatActivity implements
        NotesList.Contract,
        EditNote.Contract,
        MetadataNote.Contract,
        TrashedNotes.Contract
{
    private static final String TAG = "MainActivity";
    private static final String BUNDLE_NOTE_ID = "note_id";

    private ApplicationState applicationState;
    private DrawerLayout drawer;

    private boolean mustReloadListOfNotes = true;
    private boolean mustReloadSingleNote = true;
    private NotePresenterBase singleNotePresenter = null;
    private NotesPresenter listOfNotesPresenter = null;
    private NoteOpenerBase singleNoteOpener = null;
    private TrashedNotesPresenter trashedNotesPresenter = null;

    ////
    private ArrayList<Note<SpannableString>> trashedNotes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationState = new ApplicationState(getIntent().getExtras().getString("userUID"), this);
        applicationState.setLoaderManager(getSupportLoaderManager());

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDelegate().setContentView(R.layout.activity_main);
        drawer = (DrawerLayout) findViewById(R.id.main_layout);

        ////
        trashedNotes = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();
        openListOfNotes(null);
    }

    @Override
    public void onBackPressed() {
        /*
        int nbFragmentInStack = getSupportFragmentManager().getBackStackEntryCount();
        if (nbFragmentInStack > 1) {
            getSupportFragmentManager().popBackStack();
        }
        */
        Toast.makeText(this, "Not implemented yet", Toast.LENGTH_SHORT).show();
    }


    //********************************************************************************************
    //*  MENU
    //**

    /** on click listener that opens the dawer menu or closes it */
    public void toggleMenu(View view) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
        hideKeyboard(this);
    }

    /** menu's on click listener that opens the list of notes */
    public void openListOfNotes(MenuItem item) {
        listOfNotesPresenter = openFragment(NotesList.class, null);
        loadListOfNotes();
    }

    /** menu's on click listener that opens the list of tags */
    public void openListOfTags(MenuItem item) {
        openFragment(EditTags.class, null);
    }

    /** menu's on click listener that opens the list of deleted notes */
    public void openListOfDeletedNotes(MenuItem item) {
        ////
        trashedNotesPresenter = openFragment(TrashedNotes.class, null);
        trashedNotesPresenter.receiveNotes(trashedNotes);
    }

    /** menu's on click listener that opens the settings */
    public void openSettings(MenuItem item) {
        getFragmentManager().beginTransaction().replace(android.R.id.content, new Preferences()).commit();
        //openFragment(Preferences.class, null);
    }

    /** menu's on click listener that logs the current user out */
    public void logout(MenuItem item) {
        applicationState.getLoginManager().logout();
    }

    //********************************************************************************************
    //*  CONTRACTS
    //**

    /** fragment contract */
    @Override
    public void openNote(int id, NoteOpenerBase requester) {
        singleNotePresenter = openFragment(EditNote.class, null);
        loadNote(id);
    }

    /** fragment contract */
    @Override
    public void createNote(NotesPresenter requester) {
        singleNotePresenter = openFragment(EditNote.class, null);
        loadNewNote();
    }

    /** fragment contract */
    @Override
    public void deleteNote(Note<SpannableString> note, NotesPresenter requester) {
        ////
        trashedNotes.add(note);
        Response r = applicationState.getNoteProvider().delete(note.getId());
        if (requester != null) {
            if (r.isPositive()) {
                requester.onNoteDeletionSuccess(note);
            } else {
                requester.onNoteDeletionFailure(note, "could not delete note");
            }
        }
    }

    /** fragment contract */
    @Override
    public void saveNote(Note<SpannableString> note, NotePresenterBase requester) {
        Response r = applicationState.getNoteProvider().persist(note);
        if (!r.isPositive() && requester != null) {
            requester.onSaveNoteFailure("unable to save the note " + note.getTitle());
        }
    }

    ////
    /** fragment contract */
    @Override
    public void deleteTrashedNote(Note<SpannableString> note, TrashedNotesPresenter requester) {
        boolean isRemoved = trashedNotes.remove(note);
        if (requester != null) {
            if (isRemoved) {
                Note n = applicationState.getNoteProvider().createNote().get();
                n.setTitle(note.getTitle());
                n.setContent(note.getContent());
                requester.onTrashedNoteDeletionSuccess(note);
            } else {
                requester.onTrashedNoteDeletionFailure(note, "could not delete note");
            }
        }
    }

    /** fragment contract */
    @Override
    public void openOptionalPresenter(NotePresenter requester) {
        MetadataNote n = openFragment(MetadataNote.class, null);
        singleNotePresenter = n;

        ArrayList<View> buttons = new ArrayList<>();
        for(FormatterPlugin plugin : applicationState.getPluginProvider().getFormatters()) {
            for(View pluginAction : plugin.getMetaButtons(applicationState)) {
                buttons.add(pluginAction);
            }
        }

        n.receivePluginData(buttons);
        reloadNote();
    }

    /** fragment contract */
    @Override
    public void optionPresenterFinished(NoteOptionsPresenter finished) {
        /*
        onBackPressed(); //go back to EditNote.
        */
        // todo change this method when onBackPressed is implemented
        Toast.makeText(this, "Implementation not finished, yet.", Toast.LENGTH_SHORT).show();
        openListOfNotes(null);
    }

    //********************************************************************************************
    //*  FRAGMENTS
    //**


    public EditNote getEditNote() {
        return (EditNote) getFragment(EditNote.class);
    }

    private <F extends Fragment> F openFragment(Class<F> toOpen, Bundle bundle) {
        F f = getFragment(toOpen);
        listOfNotesPresenter = null;
        singleNotePresenter = null;
        drawer.closeDrawer(GravityCompat.START);
        hideKeyboard(this);
        commitFragment(f, toOpen.getSimpleName());
        return f;
    }
    private <F> F getFragment(Class<F> toOpen) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            @SuppressWarnings("unchecked")
            F fragment = (F)fragmentManager.findFragmentByTag(toOpen.getSimpleName());
            if (fragment == null) {
                fragment = (F) toOpen.newInstance();
            }
            return fragment;
        } catch (InstantiationException e) {
            Log.e(TAG, e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }
    private void commitFragment(Fragment fragment, String fragmentTag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction t = fragmentManager.beginTransaction();
        t.replace(R.id.content_frame, fragment, fragmentTag);
        t.addToBackStack(null);
        t.commit();
    }

    private void loadListOfNotes() {
        getSupportLoaderManager().restartLoader(NotesLoader.LOADER_ID, null, notesLoaderCallback);
    }
    private void loadNewNote() {
        getSupportLoaderManager().restartLoader(NoteLoader.LOADER_ID, null, noteLoaderCallback);
    }
    private void loadNote(int noteId) {
        Bundle args = new Bundle();
        args.putInt(BUNDLE_NOTE_ID, noteId);
        Log.d(TAG, "loadNote(" + noteId + ")");
        getSupportLoaderManager().restartLoader(NoteLoader.LOADER_ID, args, noteLoaderCallback);
    }
    private void reloadNote() {
        Log.d(TAG, "reloadingNote");
        getSupportLoaderManager().initLoader(NoteLoader.LOADER_ID, null, noteLoaderCallback);
    }

    //********************************************************************************************
    /**
     * Simple hook for the plugins to be able to start activities for result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Plugin p : applicationState.getPluginProvider().getPlugins()) {
            if (p.canHandle(requestCode)) {
                p.handle(requestCode, resultCode, data, applicationState);
                return;
            }
        }
        Log.i(TAG, "unable to handle requestCode onActivityResult " + requestCode);
    }

    //********************************************************************************************
    /**
     * NoteLoaderCallback, triggered when a note is available.
     *
     */
    private final LoaderManager.LoaderCallbacks<Optional<Note<SpannableString>>>
            noteLoaderCallback =
            new LoaderManager.LoaderCallbacks<Optional<Note<SpannableString>>>(){
                @Override
                public Loader<Optional<Note<SpannableString>>> onCreateLoader(int id, Bundle args) {
                    Optional<Integer> noteId = Optional.empty();
                    if (args != null && args.containsKey(BUNDLE_NOTE_ID)) {
                        noteId = Optional.of(args.getInt(BUNDLE_NOTE_ID));
                    }
                    return new NoteLoader(
                            getApplicationContext(),
                            applicationState.getNoteProvider(),
                            noteId);
                }

                @Override
                public void onLoadFinished(Loader<Optional<Note<SpannableString>>> loader,
                                           Optional<Note<SpannableString>> data) {
                    if (!data.isPresent()) {
                        //todo call error on singleNotePresenter if data is not present
                    } else {
                        Note<SpannableString> note = data.get();
                        Log.i(TAG, "Received note: " + note.getId());
                        if (singleNotePresenter != null) {
                            singleNotePresenter.receiveNote(note);
                        }
                    }
                }

                @Override
                public void onLoaderReset(Loader<Optional<Note<SpannableString>>> loader) { }
            };

    //********************************************************************************************
    /**
     * NotesLoaderCallback, triggered when the list of notes is available.
     *
     */
    private final LoaderManager.LoaderCallbacks<Iterable<Note<SpannableString>>>
            notesLoaderCallback =
            new LoaderManager.LoaderCallbacks<Iterable<Note<SpannableString>>>(){
                @Override
                public Loader<Iterable<Note<SpannableString>>> onCreateLoader(int id, Bundle args) {
                    return new NotesLoader(
                            getApplicationContext(),
                            applicationState.getNoteProvider());
                }

                @Override
                public void onLoadFinished(Loader<Iterable<Note<SpannableString>>> loader,
                                           Iterable<Note<SpannableString>> data) {
                    Log.i(TAG, "Received list of notes");
                    if (listOfNotesPresenter != null) {
                        listOfNotesPresenter.receiveNotes(data);
                    }
                }

                @Override
                public void onLoaderReset(Loader<Iterable<Note<SpannableString>>> loader) { }
            };

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}