package lol.navigationtest;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
    }

    public void onBackPressed(View view) {
        Log.d("sidebar", "display");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    // CURRENTLY DOES NOT SEEM TO WORK
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Log.d("sidebar","camera");
        } else if (id == R.id.nav_gallery) {
            Log.d("sidebar","gallery");
        } else if (id == R.id.nav_slideshow) {
            Log.d("sidebar","slideshow");
        } else if (id == R.id.nav_manage) {
            Log.d("sidebar","manage");
        } else if (id == R.id.nav_share) {
            Log.d("sidebar","share");
        } else if (id == R.id.nav_send) {
            Log.d("sidebar","send");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
