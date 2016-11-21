package icynote.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

import icynote.plugins.Plugin;
import icynote.ui.fragments.EditNote;
import icynote.ui.fragments.EditTags;
import icynote.ui.fragments.FragmentWithState;
import icynote.ui.fragments.MetadataNote;
import icynote.ui.fragments.NotesList;
import icynote.ui.utils.ApplicationState;

@SuppressWarnings("TryWithIdenticalCatches") //we don't have API high enough for this.
public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private ApplicationState applicationState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationState = new ApplicationState(getIntent().getExtras().getString("userUID"), this);
        applicationState.setLoaderManager(getSupportLoaderManager());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setUpNavDrawer();
        openFragment(NotesList.class, null);
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
                openFragment(NotesList.class, null);
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

    public void openMetadata(View view) {
        hideKeyboard(this);
        openFragment(MetadataNote.class, null);
    }
    public void openEditNote(int noteId) {
        Bundle args = new Bundle();
        args.putInt(EditNote.KEY_NOTE_ID, noteId);
        openFragment(EditNote.class, args);
    }
    public void reOpenEditNote(View view) {
        openEditNote(applicationState.getLastOpenedNoteId());
    }
    public void openEditNewNote() {
        openFragment(EditNote.class, null);
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

        for(Plugin p : applicationState.getPluginProvider().getPlugins()) {
            if (p.canHandle(requestCode)) {
                p.handle(requestCode, resultCode, data, applicationState);
                return;
            }
        }
        Log.i(TAG, "unable to handle requestCode onActivityResult " + requestCode);
    }

    private void openFragment(Class toOpen, Bundle bundle) {
        try {
            FragmentWithState fragment = (FragmentWithState) toOpen.newInstance();
            fragment.setState(applicationState);

            if (bundle != null) {
                fragment.setArguments(bundle);
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack(null)
                    .commit();

        } catch (InstantiationException e) {
            Log.e(TAG, e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.getMessage());
        }
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
}