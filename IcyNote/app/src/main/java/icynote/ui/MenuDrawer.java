package icynote.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MenuDrawer extends Fragment
        implements NavigationView.OnNavigationItemSelectedListener {

    public interface Contract {
        void openListOfNotes();
        void openSingleNote();
        void openSignleNoteMetadata();
        void openSettings();
        void logout();
    }

    Contract contracter;
    DrawerLayout drawer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.activity_main, container);
        NavigationView view = (NavigationView) v.findViewById(R.id.menu);
        view.setNavigationItemSelectedListener(this);
        drawer = (DrawerLayout) v.findViewById(R.id.main_layout);
        return v;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAllNotes:
                contracter.openListOfNotes();
                break;
            case R.id.menuSettings:
                contracter.openSettings();
                break;
            case R.id.menuLogout:
                contracter.logout();
                break;
            default:
                return false;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
/*
    public void toggleMenu(View view) {
        hideKeyboard(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.main_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }*/
}
