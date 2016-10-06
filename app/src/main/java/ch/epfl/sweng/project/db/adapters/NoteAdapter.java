package ch.epfl.sweng.project.db.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.Closeable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.project.db.DatabaseHelper;
import ch.epfl.sweng.project.db.models.NoteContract.Note;
import icynote.core.NoteData;
import icynote.core.NotePreviewData;
import icynote.core.NoteStorageBdr;
import icynote.core.Optional;

public class NoteAdapter implements Closeable, NoteStorageBdr {

    private static final String TAG = "NoteAdapter";
    private SQLiteDatabase database;
    private final DatabaseHelper dbHelper;
    private final String[] noteColumns = {
            Note._ID,
            Note.COL_TITLE,
            Note.COL_CREATION_DATE,
            Note.COL_MODIFICATION_DATE,
            Note.COL_TEXT
    };

    // Cached notes, mapped by ID
    private Map<Integer, NoteData> notes;

    // Temporary function to convert NoteData to NotePreviewData.
    // TODO: find a way to convert automatically the note, using for instance interfaces
    private NotePreviewData previewFromNote(NoteData note) {
        return new NotePreviewData(note.getId(), note.getTitle(), note.getCreation(), note.getLastModification());
    }

    public NoteAdapter(Context context) {
        dbHelper = new DatabaseHelper(context);
        // TODO: should we get an instance of the db on demand only ?
        database = dbHelper.getWritableDatabase();
    }

    private void constructLocalNotes() {
        // We don't build the local cache if it already exists
        if (this.notes != null) {
            return;
        }

        // Map from note id to its data
        Map<Integer, NoteData> notes = new HashMap<>();
        Cursor cursor = database.query(
                Note.TABLE_NAME, // table name
                null, // columns (null for getting all)
                null, // selection
                null, // selection args
                null, // groupBy
                null, // having
                null); // orderBy

        // TODO: There must exist a better way to get the indices for the columns
        Map<String,Integer> columns = new HashMap<>();
        for (String col: noteColumns)
            columns.put(col, cursor.getColumnIndex(col));

        // TODO: is it sensible to iterate over all notes and create every one of them ?
        try {
            while (cursor.moveToNext()) {
                int note_id = cursor.getInt(columns.get(Note._ID));
                NoteData note = new NoteData(
                        note_id,
                        Date.valueOf(cursor.getString(columns.get(Note.COL_CREATION_DATE))),
                        Date.valueOf(cursor.getString(columns.get(Note.COL_MODIFICATION_DATE))));

                note.setTitle(cursor.getString(columns.get(Note.COL_TITLE)));
                note.setContent(cursor.getString(columns.get(Note.COL_TEXT)));

                notes.put(note_id, note);
            }
        } finally {
            cursor.close();
        }

        this.notes = notes;
    }

    @Override
    public List<NotePreviewData> previewAllNotes() {
        constructLocalNotes();
        List<NotePreviewData> previews = new ArrayList<>();
        // Construct the previews
        // TODO: The function should be simpler when the conversion between notes is fixed
        for (NoteData note: notes.values()) {
            previews.add(previewFromNote(note));
        }

        return previews;
    }

    @Override
    public Optional<NoteData> getNote(int id) {
        // If the cache exists, try to get it from there, else try to get from DB
        if (notes != null) {
            NoteData note = notes.get(id);
            if (note != null) {
                return Optional.of(note);
            }
        }

        Cursor cursor = database.query(Note.TABLE_NAME, // table name
                null, // columns (null for getting all)
                "? = ?", // selection
                new String[] { Note._ID, Integer.toString(id) }, // selection args
                null, // groupBy
                null, // having
                null); // orderBy

        try {
            // There is no corresponding note
            if (!cursor.moveToFirst()) {
                return Optional.empty();
            }

            // TODO: abstract the note retrieval/creation (used in constructLocalNotes)
            Integer note_id = cursor.getInt(cursor.getColumnIndexOrThrow(Note._ID));
            NoteData note = new NoteData(
                    note_id,
                    Date.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(Note.COL_CREATION_DATE))),
                    Date.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(Note.COL_MODIFICATION_DATE))));

            note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(Note.COL_TITLE)));
            note.setContent(cursor.getString(cursor.getColumnIndexOrThrow(Note.COL_TEXT)));

            // Add the note to local cache
            notes.put(note_id, note);

            return Optional.of(note);

        } finally {
            cursor.close();
        }
    }

    @Override
    public SaveNoteResponse saveNote(NoteData toSave) {
        ContentValues values = new ContentValues();

        // TODO: what to do with the note ID ?
        values.put(Note._ID, toSave.getId());
        values.put(Note.COL_TITLE, toSave.getTitle());
        values.put(Note.COL_TEXT, toSave.getContent());
        // TODO: find a way to get Date be saved in the right format
        values.put(Note.COL_CREATION_DATE, toSave.getCreation().toString());
        values.put(Note.COL_MODIFICATION_DATE, toSave.getLastModification().toString());

        // is equivalent to "update or insert"
        long error = database.insertWithOnConflict(Note.TABLE_NAME,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);

        if (error == -1)
            return SaveNoteResponse.REFUSED;
        else
            return SaveNoteResponse.OK;
    }

    @Override
    public UpdateTitleResponse updateTitle(int id, String title) {
        ContentValues values = new ContentValues();

        values.put(Note.COL_TITLE, title);

        int rowsAffected = database.update(Note.TABLE_NAME,
                values,
                "? = ?", new String[] { Note._ID, Integer.toString(id) } );

        if (rowsAffected > 1) {
            Log.wtf(TAG, "Updating the title for note with id " + id + " affected "
                    + rowsAffected + " rows instead of 1!");
            // TODO: should the answer be REFUSED or OK ?
            return UpdateTitleResponse.REFUSED;
        } else if (rowsAffected == 0)
            return UpdateTitleResponse.REFUSED;
        else
            return UpdateTitleResponse.OK;
    }

    @Override
    public DeleteNoteResponse deleteNote(int id) {
        int rowsAffected = database.delete(Note.TABLE_NAME,
                "? = ?", new String[] { Note._ID, Integer.toString(id)} );

        if (rowsAffected > 1) {
            Log.wtf(TAG, "Deleting note with id " + id + " affected "
                    + rowsAffected + " rows instead of 1!");
            // TODO: should the answer be REFUSED or OK ?
            return DeleteNoteResponse.REFUSED;
        } else if (rowsAffected == 0)
            return DeleteNoteResponse.REFUSED;
        else
            return DeleteNoteResponse.OK;
    }

    @Override
    public void close() {
        dbHelper.close();
    }
}
