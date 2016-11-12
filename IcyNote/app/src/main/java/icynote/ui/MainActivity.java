package icynote.ui;

import android.app.Activity;
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
import android.view.inputmethod.InputMethodManager;

import icynote.login.LoginManagerFactory;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Settings.OnSpinnerSelection {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Theme.initTheme(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setUpNavDrawer();

        openFragment(NotesList.class);
    }
/*
@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.r_main);
    ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.expandableListView1);
    LayoutInflater li = getLayoutInflater();
    String[][] data = {{"child-1", "child-2", "child-3"},{"child-1", "child-2", "child-3"}};
    expandableListView.setAdapter(new SampleExpandableListAdapter(li, data));
}*/
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

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.menuAllNotes) {
            openFragment(NotesList.class);

        } else if (id == R.id.menuTagEdition) {
            openFragment(EditTags.class);

        } else if (id == R.id.menuTrash) {
            openFragment(EditNote.class);

        } else if (id == R.id.menuSettings) {
            openFragment(Settings.class);

        } else if (id == R.id.menuLogout) {
            Log.d("MainActivity", "menuLogout");
            LoginManagerFactory.getInstance().logout();
        }

        return true;
    }

    public void openSettings(View view) {
        openFragment(MetadataNote.class);
    }


    public void backToNote(View view) {
        openFragment(EditNote.class);
    }

    public void openFragment(Class fragmentClass) {
        Fragment fragment = null;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (InstantiationException e) {

        } catch (IllegalAccessException e) {

        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onThemeSelected(Theme.ThemeType currentTheme)
    {
        openFragment(Settings.class);
    }
}