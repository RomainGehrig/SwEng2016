package icynote.ui;

import android.graphics.Color;
import android.media.audiofx.BassBoost;
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

import ch.epfl.sweng.project.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static Settings.ColorSetting STYLE = Settings.ColorSetting.BRIGHT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setUpNavDrawer();
        openFragment(EditNote.class);
    }

    private void setUpNavDrawer() {
        NavigationView view = (NavigationView) findViewById(R.id.menu);
        // DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.menu_layout);
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


        if (id == R.id.allNotes) {
            openFragment(NotesList.class);

        } else if (id == R.id.tagEdition) {
            openFragment(EditTags.class);

        } else if (id == R.id.trash) {
            openFragment(EditNote.class);

        } else if (id == R.id.settings) {
            openFragment(Settings.class);

        } else if (id == R.id.logout) {
            openFragment(EditNote.class);

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

    private void openFragment(Class fragmentClass) {
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.menu_layout);
        drawer.closeDrawer(GravityCompat.START);

    }
}
