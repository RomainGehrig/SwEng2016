package icynote.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.ArrayList;

import icynote.loaders.NoteLoader;
import icynote.loaders.NotesLoader;
import icynote.login.LoginManager;
import icynote.login.LoginManagerFactory;
import icynote.note.Note;
import icynote.note.Response;
import icynote.note.decorators.NoteDecoratorFactory;
import icynote.note.impl.NoteData;
import icynote.noteproviders.NoteProvider;
import icynote.noteproviders.impl.Factory;
import icynote.plugins.FormatterPlugin;
import icynote.plugins.Plugin;
import icynote.plugins.PluginData;
import icynote.plugins.PluginsProvider;
import icynote.ui.contracts.NoteOpenerBase;
import icynote.ui.contracts.NoteOptionsPresenter;
import icynote.ui.contracts.NotePresenter;
import icynote.ui.contracts.NotePresenterBase;
import icynote.ui.contracts.NotesPresenter;
import icynote.ui.contracts.PluginPresenter;
import icynote.ui.contracts.TrashedNotesPresenter;
import icynote.ui.fragments.EditNote;
import icynote.ui.fragments.EditTags;
import icynote.ui.fragments.MetadataNote;
import icynote.ui.fragments.NotesList;
import icynote.ui.fragments.Preferences;
import icynote.ui.fragments.TrashedNotes;
import util.Callback;
import util.Optional;

@SuppressWarnings("TryWithIdenticalCatches") //we don't have API high enough for this.
public class MainActivity  extends AppCompatActivity implements
        NotesList.Contract,
        EditNote.Contract,
        MetadataNote.Contract,
        TrashedNotes.Contract,
        PluginPresenter.Contract
{
    private static final String TAG = "MainActivity";
    private static final String BUNDLE_NOTE_ID = "note_id";
    private static final String BUNDLE_SEL_START = "sel_start";
    private static final String BUNDLE_SEL_STOP = "sel_stop";

    private DrawerLayout drawer;
    private MenuItem lastOpenedNoteMenuItem;

    private PluginData pluginData;
    private PluginsProvider pluginProvider;
    private NoteProvider<Note<SpannableString>> noteProvider;
    private LoginManager loginManager;

    private NotePresenterBase singleNotePresenter = null;
    private NotesPresenter listOfNotesPresenter = null;
    private Integer lastOpenedNoteId;

    private ArrayList<Note<SpannableString>> trashedNotes;

    private util.Callback executeOnStart;
    private final util.Callback executeOnStartDefault = new Callback() {
        @Override
        public void execute() {
            openListOfNotes(null);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String userId = (getIntent() == null || getIntent().getExtras() == null)
                ? "anonymousUserId"
                : getIntent().getExtras().getString("userUID");

        Log.i(TAG, "onCreate");

        pluginData = new PluginData(this);
        pluginData.setContractor(this);

        loginManager = LoginManagerFactory.getInstance();
        pluginProvider = new PluginsProvider();
        NoteDecoratorFactory<SpannableString> temp = new NoteDecoratorFactory<>();
        for(FormatterPlugin p : pluginProvider.getFormatters()) {
            temp = temp.andThen(p.getInteractorFactory(pluginData));
        }
        noteProvider = Factory.make(this, userId, temp);
        pluginProvider = new PluginsProvider();
        trashedNotes = new ArrayList<>();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDelegate().setContentView(R.layout.activity_main);

        drawer = (DrawerLayout) findViewById(R.id.main_layout);
        Menu menu = ((NavigationView) findViewById(R.id.menu)).getMenu();
        menu.findItem(R.id.menuAllNotes).setChecked(true);
        lastOpenedNoteMenuItem = menu.findItem(R.id.menuLastNote);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(BUNDLE_NOTE_ID)) {
                lastOpenedNoteId = savedInstanceState.getInt(BUNDLE_NOTE_ID);
            }
            pluginData.setSelectionStart(savedInstanceState.getInt(BUNDLE_SEL_START));
            pluginData.setSelectionEnd(savedInstanceState.getInt(BUNDLE_SEL_STOP));
        }

        executeOnStart = executeOnStartDefault;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (lastOpenedNoteId != null) {
            savedInstanceState.putInt(BUNDLE_NOTE_ID, lastOpenedNoteId);
        }
        savedInstanceState.putInt(BUNDLE_SEL_START, pluginData.getSelectionStart());
        savedInstanceState.putInt(BUNDLE_SEL_STOP, pluginData.getSelectionEnd());

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.i(TAG, "onStart");
        executeOnStart.execute(); //execute last registered callback
        executeOnStart = executeOnStartDefault; //go back to default strategy
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        toggleMenu(null);
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

    /** menu's on click listener that opens the list of notes */
    public void openLastOpenedNote(MenuItem item) {
        Log.e(TAG, "openLastOpenedNote menu item");
        if (lastOpenedNoteId == null) {
            Toast.makeText(this, "There is no last opened note.",
                    Toast.LENGTH_SHORT).show();
            toggleMenu(null);
        } else {
            singleNotePresenter = openFragment(EditNote.class, null);
            lastOpenedNoteMenuItem.setChecked(true);
            reloadNote();
        }
    }
    /** menu's on click listener that opens the list of tags */
    public void openListOfTags(MenuItem item) {
        openFragment(EditTags.class, null);
    }

    /** menu's on click listener that opens the list of deleted notes */
    public void openListOfTrashedNotes(MenuItem item) {
        TrashedNotesPresenter trashedNotesPresenter = openFragment(TrashedNotes.class, null);
        trashedNotesPresenter.receiveNotes(trashedNotes);
    }

    /** menu's on click listener that opens the settings */
    public void openSettings(MenuItem item) {
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new Preferences())
                .commit();
        //openFragment(Preferences.class, null);
    }

    /** menu's on click listener that logs the current user out */
    public void logout(MenuItem item) {
        loginManager.logout();
    }

    //********************************************************************************************
    //*  CONTRACTS
    //**

    /** fragment contract */
    @Override
    public void openNote(int id, NoteOpenerBase requester) {
        singleNotePresenter = openFragment(EditNote.class, null);
        lastOpenedNoteMenuItem.setChecked(true);
        loadNote(id);
    }

    /** fragment contract */
    @Override
    public void reOpenLastOpenedNote(NoteOpenerBase requester) {
        Log.e(TAG, "reopening note");
        openLastOpenedNote((MenuItem)null);
    }

    /** fragment contract */
    @Override
    public void createNote(NotesPresenter requester) {
        singleNotePresenter = openFragment(EditNote.class, null);
        lastOpenedNoteMenuItem.setChecked(true);
        loadNewNote();
    }

    /** fragment contract */
    @Override
    public void deleteNote(Note<SpannableString> note, NotesPresenter requester) {
        Response r = noteProvider.delete(note.getId());
        if (requester != null) {
            if (r.isPositive()) {
                trashedNotes.add(note);
                if (lastOpenedNoteId != null && lastOpenedNoteId == note.getId()) {
                    lastOpenedNoteId = null;
                    pluginData.setLastOpenedNote(null);
                }
                requester.onNoteDeletionSuccess(note);
            } else {
                requester.onNoteDeletionFailure(note, "could not delete note");
            }
        }
    }

    /** fragment contract */
    @Override
    public void saveNote(Note<SpannableString> note, NotePresenterBase requester) {
        Response r = noteProvider.persist(note);
        if (!r.isPositive()) {
            if (requester != null) {
                requester.onSaveNoteFailure("unable to save the note " + note.getTitle());
            } else {
                Toast.makeText(this, "Unexpected error: unable to perist the note",
                        Toast.LENGTH_SHORT).show();
                Log.i(TAG, "Unexpected error: unable to perist the note" + note.getId());
            }
        }
    }

    /** fragment contract */
    @Override
    public void updateSelection(int start, int end) {
        pluginData.setSelectionStart(start);
        pluginData.setSelectionEnd(end);
    }

    /** fragment contract */
    @Override
    public void restoreTrashedNote(Note<SpannableString> note, TrashedNotesPresenter requester) {
        if (!trashedNotes.contains(note)) {
            Log.i(TAG, "attempt to restore a non deleted note: (id=" + note.getId() + ")");
            requester.onTrashedNoteRestoredSuccess(note);
            return;
        }
        Optional<Note<SpannableString>>
                created = noteProvider.createNote();

        if (created.isPresent()) {
            NoteData<SpannableString> rawNote = note.getRaw();
            rawNote.setId(created.get().getId());
            Response r = noteProvider.persist(rawNote);
            if (r.isPositive()) {
                trashedNotes.remove(note);
                requester.onTrashedNoteRestoredSuccess(note);
            } else {
                requester.onTrashedNoteRestoredFailure(note,
                        "Sorry, unable to persist the note");
            }
        } else {
            requester.onTrashedNoteRestoredFailure(note,
                    "Sorry, unable to create a new holder for your note");
        }
    }

    /** fragment contract */
    @Override
    public void openOptionalPresenter(NotePresenter requester) {
        MetadataNote n = openFragment(MetadataNote.class, null);
        singleNotePresenter = n;

        ArrayList<View> buttons = new ArrayList<>();
        for(FormatterPlugin plugin : pluginProvider.getFormatters()) {
            for(View pluginAction : plugin.getMetaButtons(pluginData)) {
                buttons.add(pluginAction);
            }
        }

        n.receivePluginData(buttons);
        reloadNote();
    }

    /** fragment contract */
    @Override
    public void optionPresenterFinished(NoteOptionsPresenter finished) {
        openLastOpenedNote(null);
    }

    /** fragment contract */
    @Override
    public void registerOnStartCallback(util.Callback executeOnActivityStart) {
        executeOnStart = executeOnActivityStart;
    }

    //********************************************************************************************
    //*  FRAGMENTS
    //**

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
        Bundle args = new Bundle();
        args.putInt(BUNDLE_NOTE_ID, lastOpenedNoteId); //if loader was reset for whatever reasons
        Log.d(TAG, "reloadingNote (lastOpenedId is " + lastOpenedNoteId + ")");
        getSupportLoaderManager().initLoader(NoteLoader.LOADER_ID, args, noteLoaderCallback);
    }

    //********************************************************************************************
    /**
     * Simple hook for the plugins to be able to start activities for result.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for (Plugin p : pluginProvider.getPlugins()) {
            if (p.canHandle(requestCode)) {
                p.handle(requestCode, resultCode, data, pluginData);
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
                            noteProvider,
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
                            lastOpenedNoteId = note.getId();
                            pluginData.setLastOpenedNote(note);
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
                            noteProvider);
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