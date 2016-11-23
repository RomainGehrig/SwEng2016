package icynote.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
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

import java.util.ArrayList;
import java.util.List;

import icynote.loaders.NoteLoader;
import icynote.loaders.NotesLoader;
import icynote.note.Note;
import icynote.plugins.Plugin;
import icynote.ui.contracts.NotePresenter;
import icynote.ui.contracts.NotesPresenter;
import icynote.ui.fragments.EditNote;
import icynote.ui.fragments.EditTags;
import icynote.ui.fragments.MetadataNote;
import icynote.ui.fragments.NotesList;
import icynote.ui.utils.ApplicationState;
import util.Optional;

@SuppressWarnings("TryWithIdenticalCatches") //we don't have API high enough for this.
public class MainActivity
        extends AppCompatActivity
        implements OnNavigationItemSelectedListener,
        NotesList.Contract, EditNote.Contract, MetadataNote.Contract {

    private static final String TAG = "MainActivity";
    private ApplicationState applicationState;

    private boolean dirtyNotes = true; // list of notes needs a reload
    private List<NotePresenter> presenters;
    private NotesPresenter listPresenter = null;

    private static final String BUNDLE_NOTE_ID = "note_id";
    private LoaderManager.LoaderCallbacks<Optional<Note<SpannableString>>> noteLoaderCallback =
            new LoaderManager.LoaderCallbacks<Optional<Note<SpannableString>>>(){
                @Override
                public Loader<Optional<Note<SpannableString>>> onCreateLoader(int id, Bundle args) {
                    Optional<Integer> noteId = Optional.empty();
                    if (args != null && args.containsKey(BUNDLE_NOTE_ID)) {
                        noteId = Optional.of(args.getInt(BUNDLE_NOTE_ID));
                    }
                    return new NoteLoader(getApplicationContext(), applicationState.getNoteProvider(), noteId);
                }

                @Override
                public void onLoadFinished(Loader<Optional<Note<SpannableString>>> loader,
                                           Optional<Note<SpannableString>> data) {
                    // TODO call observers
                    if (!data.isPresent()) {
                        // TODO call error on presenters if not present
                    } else {
                        Note<SpannableString> note = data.get();
                        Log.i(TAG, "Received note: " + note.getId());
                        for (NotePresenter presenter: presenters) {
                            presenter.receiveNote(note);
                        }
                    }
                }

                @Override
                public void onLoaderReset(Loader<Optional<Note<SpannableString>>> loader) { }
            };

    private LoaderManager.LoaderCallbacks<Iterable<Note<SpannableString>>> notesLoaderCallback =
            new LoaderManager.LoaderCallbacks<Iterable<Note<SpannableString>>>(){
                @Override
                public Loader<Iterable<Note<SpannableString>>> onCreateLoader(int id, Bundle args) {
                    return new NotesLoader(getApplicationContext(), applicationState.getNoteProvider());
                }

                @Override
                public void onLoadFinished(Loader<Iterable<Note<SpannableString>>> loader,
                                           Iterable<Note<SpannableString>> data) {
                    // TODO data received, call observers
                    dirtyNotes = false;
                }

                @Override
                public void onLoaderReset(Loader<Iterable<Note<SpannableString>>> loader) { }
            };


    private void loadNewNote() {
        getSupportLoaderManager().restartLoader(NoteLoader.LOADER_ID, null, noteLoaderCallback);
    }
    private void loadNote(int noteId) {
        Bundle args = new Bundle();
        args.putInt(BUNDLE_NOTE_ID, noteId);
        getSupportLoaderManager().restartLoader(NoteLoader.LOADER_ID, args, noteLoaderCallback);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationState = new ApplicationState(getIntent().getExtras().getString("userUID"), this);
        applicationState.setLoaderManager(getSupportLoaderManager());
        presenters = new ArrayList<>();
        presenters.add(getEditNote());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setUpNavDrawer();
        openEditNewNote();
    }

    private void setUpNavDrawer() {
        NavigationView view = (NavigationView) findViewById(R.id.menu);
        view.setNavigationItemSelectedListener(this);
    }

    public void toggleMenu(View view) {
        hideKeyboard(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menuAllNotes:
                openNotesList();
                break;
            case R.id.menuTagEdition:
                openFragment(EditTags.class, null);
                break;
            case R.id.menuTrash:
                openFragment(NotesList.class, null);
                break;
            case R.id.menuSettings:
                Intent intent = new Intent(this, Preferences.class);
                startActivity(intent);
                break;
            case R.id.menuLogout:
                applicationState.getLoginManager().logout();
                break;
            default:
                return false;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openNotesList() {
        NotesList f = openFragment(NotesList.class, null);
        listPresenter = f;
    }

    public void openMetadata(View view) {
        hideKeyboard(this);
        openFragment(MetadataNote.class, null);
    }

    public void reOpenEditNote(View view) {
        openEditNote(applicationState.getLastOpenedNoteId());
    }
    public void openEditNewNote() {
        loadNewNote();
        commitFragment(getEditNote(), EditNote.class.getSimpleName());
    }
    public void openEditNote(Integer noteId) {
        loadNote(noteId);
        commitFragment(getEditNote(), EditNote.class.getSimpleName());
    }
    public EditNote getEditNote() {
        return (EditNote) getFragment(EditNote.class);
    }

    @Override
    public void onBackPressed() {
        int nbFragmentInStack = getSupportFragmentManager().getBackStackEntryCount();
        if (nbFragmentInStack > 1) {
            getSupportFragmentManager().popBackStack();
        }
    }

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

    private <F extends Fragment> F openFragment(Class<F> toOpen, Bundle bundle) {
        F f = getFragment(toOpen);
        listPresenter = null;
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

    @Override
    public void saveNote(Note<SpannableString> note) {
        applicationState.getNoteProvider().persist(note);
    }

    @Override
    public void openNote(int id) {
        openEditNote(id);
    }

    @Override
    public void createNote() {
        openEditNewNote();
    }

    @Override
    public void deleteNotes(List<Integer> notes) {

    }

}