package icynote.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import icynote.core.Note;
import icynote.core.impl.NoteData;

public class ShowNoteActivity extends AppCompatActivity {

    private ArrayList<Note> notes = new ArrayList<>();
    private NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note);

        Intent i = getIntent();

        NoteData currentNote = i.getParcelableExtra("icynote.core.impl.NOTE_DATA");

        notes = new ArrayList<>();
        notes.add(currentNote);
        notesAdapter = new NotesAdapter(this, notes);
        ListView listView = (ListView) findViewById(R.id.lvOneNote);
        listView.setAdapter(notesAdapter);

        //add context menu
        registerForContextMenu(listView);
    }

    public void goBack(@SuppressWarnings("UnunsedParameters") View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View vue, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, vue, menuInfo);
        getMenuInflater().inflate(R.menu.notes_list_context_menu, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ////le cast ici dessous est très important pour avoir accès au champ position
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete_id:
                notes.remove(info.position);
                notesAdapter.notifyDataSetChanged();
                return true;
            case R.id.retour_id:
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}