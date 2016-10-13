package ch.epfl.sweng.project;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setUpNavDrawer();


        // Set the launching main content
        Fragment  fragment = null;
        try {
            fragment = (Fragment) EditNote.class.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

    }

    private void setUpNavDrawer() {
        NavigationView view = (NavigationView) findViewById(R.id.menu);
        DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.menu_layout);
        view.setNavigationItemSelectedListener(this);
    }

    public void toggleMenu(View view) {
        Log.i("sidebar", "display");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.menu_layout);
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

        Class fragmentClass = null;
        Fragment fragment = null;

        if (id == R.id.allNotes) {
            fragmentClass = NotesList.class;

        } else if (id == R.id.tagEdition) {
            fragmentClass = EditTags.class;

        } else if (id == R.id.trash) { // TODO: kind of notes list ?
            fragmentClass = EditNote.class;

        } else if (id == R.id.settings) {
            fragmentClass = Settings.class;

        } else if (id == R.id.logout) { // TODO
            fragmentClass = EditNote.class;

        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.menu_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void blank(View view) {
        // blank handler for avoiding crashes for not yet implemented buttons
    }

    public void openSettings(View view) {
        // open settings panel
        Log.d("settings", "open");
    }
}
