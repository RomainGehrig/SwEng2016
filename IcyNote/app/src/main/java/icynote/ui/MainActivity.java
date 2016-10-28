package icynote.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import icynote.core.IcyNoteCore;
import icynote.core.Note;
import icynote.core.impl.CoreFactory;
import icynote.storage.ListStorage;
import util.Optional;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private IcyNoteCore core;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Style.initStyle();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setUpNavDrawer();
        Log.i(TAG, "Initialization of the core");
        core = CoreFactory.core(new ListStorage());
        for (int i=0; i<5; i++){
            Note note = core.createNote().get();
            note.setTitle("OMG TITLE " + i);
            note.setContent("OMG CONTENT " + i);
            core.persist(note);
        }

        openFragment(EditNote.class);
    }

    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    private void setUpNavDrawer() {
        NavigationView view = (NavigationView) findViewById(R.id.menu);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.main_layout);
        view.setNavigationItemSelectedListener(this);
    }


    public void toggleMenu(View view) {
        Log.i("sidebar", "display");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }


    // Handles click events related to the menu
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuAllNotes) {
            openFragment(NotesList.class, getSupportLoaderManager(), core);
        } else if (id == R.id.menuTagEdition) {
            openFragment(EditTags.class);
        } else if (id == R.id.menuTrash) { // TODO: kind of notes list ?
            openFragment(EditNote.class);
        } else if (id == R.id.menuSettings) {
            openFragment(Settings.class);
        } else if (id == R.id.menuLogout) {
            openFragment(Logout.class);
        }

        return true;
    }

    public void openSettings(View view) {
        // open settings panel
        openFragment(MetadataNote.class);

    }


    public void backToNote(View view) {
        openFragment(EditNote.class);
    }


    // ----- COLOR SETTINGS DEV BLOCK
    public enum ColorSetting {
        DARK, BRIGHT
    }

    /**
     * FIXME: Temp function to add a loader manager
     */
    public void openFragment(Class fragmentClass, LoaderManager loaderManager, IcyNoteCore core) {
        NotesList fragment = null;

        try {
            fragment = (NotesList) fragmentClass.newInstance();
        } catch (InstantiationException e) {
            // e.printStackTrace();
        } catch (IllegalAccessException e) {
            // e.printStackTrace();
        }

        fragment.setLoaderManager(loaderManager);
        fragment.setCore(core);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    public void openFragment(Class fragmentClass) {
        Fragment fragment = null;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (InstantiationException e) {
            // e.printStackTrace();
        } catch (IllegalAccessException e) {
            // e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

}
