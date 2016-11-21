package icynote.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import icynote.login.LoginManagerFactory;
import icynote.note.Note;
import icynote.noteproviders.NoteProvider;
import icynote.noteproviders.impl.Singleton;
import icynote.plugins.Plugin;
import icynote.plugins.PluginsProvider;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private NoteProvider<Note<SpannableString>> core;

    public static View editNoteView = null;
    public static MainActivity instance = null;

    private enum FragmentID {
        EditTags(EditTags.class), EditNote(EditNote.class, true),
        MetadataNote(MetadataNote.class),
        NotesList(NotesList.class, true);

        private final Class fragmentClass;
        private final boolean needCoreAndLoader;

        FragmentID(Class fragmentClass) {
            this(fragmentClass, false);
        }

        FragmentID(Class fragmentClass, boolean needCoreAndLoader) {
            this.fragmentClass = fragmentClass;
            this.needCoreAndLoader = needCoreAndLoader;
        }

        public Fragment instantiateFragment() {
            Fragment fragment = null;

            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (IllegalAccessException e) {
            } catch (InstantiationException e) {
            }

            return fragment;
        }
        public Fragment instantiateFragment(NoteProvider<Note<SpannableString>> core, LoaderManager loaderManager) {
            if (!needCoreAndLoader) {
                return instantiateFragment();
            }

            Fragment fragment = null;
            try {
                FragmentWithCoreAndLoader tmpFragment = (FragmentWithCoreAndLoader) fragmentClass.newInstance();
                tmpFragment.setCore(core);
                tmpFragment.setLoaderManager(loaderManager);
                fragment = tmpFragment;
            } catch (IllegalAccessException e) {
            } catch (InstantiationException e) {
            }

            return fragment;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setUpNavDrawer();
        core = Singleton.getCore();
        openFragment(FragmentID.NotesList);
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
        //hideSoftKeyboard();

        if (id == R.id.menuAllNotes) {
            openFragment(FragmentID.NotesList);
        } else if (id == R.id.menuTagEdition) {
            openFragment(FragmentID.EditTags);
        } else if (id == R.id.menuTrash) {
            openFragment(FragmentID.EditNote);
        } else if (id == R.id.menuSettings) {
            Intent intent = new Intent(this, Preferences.class);
            startActivity(intent);
        } else if (id == R.id.menuLogout) {
            LoginManagerFactory.getInstance().logout();
        }

        return true;
    }

    public void openSettings(View view) {
        openFragment(FragmentID.MetadataNote);
    }

    public void backToNote(View view) {
        openEditNote();
    }

    public void openEditNewNote() {
        openFragment(FragmentID.EditNote);
    }
    public void openEditNote() {
        //re-open last opened note
        Bundle args = new Bundle();
        args.putInt(EditNote.KEY_NOTE_ID, EditNote.note.getId());
        openFragment(FragmentID.EditNote, args);
    }
    public void openEditNote(int noteId) {
        Bundle args = new Bundle();
        args.putInt(EditNote.KEY_NOTE_ID, noteId);
        openFragment(FragmentID.EditNote, args);
    }

    public void openFragment(FragmentID fragmentID) {
        openFragment(fragmentID, null);
    }
    public void openFragment(FragmentID fragmentID, Bundle bundle) {
        Fragment fragment = null;
        if (fragmentID.needCoreAndLoader) {
            fragment = fragmentID.instantiateFragment(core, getSupportLoaderManager());
        } else {
            fragment = fragmentID.instantiateFragment();
        }

        if (bundle != null) {
            fragment.setArguments(bundle);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .addToBackStack(null)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    @Override
    public void onBackPressed() {
        int nbFragmentInStack = getSupportFragmentManager().getBackStackEntryCount();
        if(nbFragmentInStack > 1) {
            getSupportFragmentManager().popBackStack();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        for(Plugin p : PluginsProvider.getPlugins()) {
            if (p.canHandle(requestCode)) {
                p.handle(requestCode, resultCode, data, this);
                return;
            }
        }

        Log.i(TAG, "unable to handle requestCode onActivityResult " + requestCode);

    }

}
