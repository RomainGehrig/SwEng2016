package icynote.noteproviders.persistent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.GregorianCalendar;

import icynote.note.Note;
import icynote.noteproviders.NoteProvider;
import icynote.noteproviders.OrderBy;
import icynote.noteproviders.OrderType;
import icynote.note.Response;
import icynote.note.impl.NoteData;
import icynote.note.common.ResponseFactory;
import icynote.note.decorators.ConstId;
import icynote.database.DatabaseProvider;
import icynote.database.models.NoteContract.Notes;
import util.Optional;

/**
 * Stores the notes in the local SQLite database.
 * <p>
 * Implementation note: we don't close the database connection.
 * See http://stackoverflow.com/questions/6608498/best-place-to-close-database-connection
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class SQLiteNoteProvider implements NoteProvider<Note<String>> {
    private static final String LOGGING_TAG = "SQLiteNoteProvider";

    private String mCurrentUserUID;
    private DatabaseProvider mDbProvider;
    private final String[] mNoteColumns = {
            Notes.COL_ID,
            Notes.COL_USER_ID,
            Notes.COL_TITLE,
            Notes.COL_CREATION_DATE,
            Notes.COL_MODIFICATION_DATE,
            Notes.COL_CONTENT
    };

    public SQLiteNoteProvider(Context context, String userUID) {
        mCurrentUserUID = userUID;
        mDbProvider = new DatabaseProvider(context);
    }

    @Override
    public Optional<Note<String>> createNote() {
        GregorianCalendar now = new GregorianCalendar();
        long noteId = createEntryInNoteTable(now);
        Note<String> note = createNote(noteId, now);
        return Optional.of(note);
    }

    @Override
    public Optional<Note<String>> getNote(int id) {
        SQLiteDatabase db = mDbProvider.getReadableDatabase();

        Cursor cursor = db.query(Notes.TABLE_NAME
                , mNoteColumns          // The columns to return
                , Notes.COL_ID + " = ?" // The columns for the WHERE clause
                , new String[]{"" + id} // The values for the WHERE clause
                , null                  // don't group the rows
                , null                  // don't filter by row groups
                , null                  // don't sort
        );

        Optional<Note<String>> result = Optional.empty();

        if (cursor != null) try {
            cursor.moveToFirst();
            result = Optional.of(getNoteFromCursor(cursor));
        } catch (RuntimeException e) {
            Log.e(LOGGING_TAG, e.getMessage());
        } finally {
            cursor.close();
        }

        return result;
    }

    @Override
    public Iterable<Note<String>> getNotes(OrderBy index, OrderType order) {
        SQLiteDatabase db = mDbProvider.getReadableDatabase();

        Cursor cursor = db.query(Notes.TABLE_NAME
                , mNoteColumns                  // The columns to return
                , Notes.COL_USER_ID + " = ?"    // The columns for the WHERE clause
                , new String[]{mCurrentUserUID} // The values for the WHERE clause
                , null                          // don't group the rows
                , null                          // don't filter by row groups
                , getField(index) + " " + getOrder(order)  // The sort order
        );

        Collection<Note<String>> notes = new ArrayList<>();

        if (cursor != null) try {
                while (cursor.moveToNext()) {
                    notes.add(getNoteFromCursor(cursor));
                }
            } catch (RuntimeException e) {
                Log.e(LOGGING_TAG, e.getMessage());
            } finally {
                    cursor.close();
            }

        return notes;
    }

    @Override
    public Response persist(Note<String> n) {

        ContentValues values = new ContentValues();
        values.put(Notes.COL_TITLE, n.getTitle());
        values.put(Notes.COL_CREATION_DATE, n.getCreation().getTimeInMillis());
        values.put(Notes.COL_MODIFICATION_DATE, n.getLastUpdate().getTimeInMillis());
        values.put(Notes.COL_CONTENT, n.getContent());

        SQLiteDatabase db = mDbProvider.getReadableDatabase();
        int count = db.update(Notes.TABLE_NAME
                , values
                , Notes.COL_ID + " LIKE ?" + " AND " + Notes.COL_USER_ID + " LIKE ?"
                , new String[]{"" + n.getId(), mCurrentUserUID});

        return getResponse(count < 1);
    }

    @Override
    public Response delete(int id) {
        SQLiteDatabase db = mDbProvider.getReadableDatabase();

        int count = db.delete(Notes.TABLE_NAME
                , Notes.COL_ID + " LIKE ?" + " AND " + Notes.COL_USER_ID + " LIKE ?"
                , new String[]{"" + id, mCurrentUserUID});

        return getResponse(count < 1);
    }

    /**
     * Deletes every note corresponding to the specified user.
     *
     * @param userUID the unique id of the user whose notes we want to delete.
     * @return the number of deleted rows.
     */
    int deleteUser(@NonNull String userUID) {
        SQLiteDatabase db = mDbProvider.getReadableDatabase();

        return db.delete(Notes.TABLE_NAME
                , Notes.COL_USER_ID + " LIKE ?"
                , new String[]{userUID});
    }

    //------------------------------------------------------------------

    /**
     * Creates a new entry in the table for Notes
     *
     * @param now the date of creation
     * @return the id of the newly created note
     */
    private long createEntryInNoteTable(@NonNull GregorianCalendar now) {
        SQLiteDatabase db = mDbProvider.getWritableDatabase();
        long nowMillis = now.getTimeInMillis();

        ContentValues values = new ContentValues();
        values.put(Notes.COL_USER_ID, mCurrentUserUID);
        values.put(Notes.COL_CREATION_DATE, nowMillis);
        values.put(Notes.COL_MODIFICATION_DATE, nowMillis);

        return db.insert(Notes.TABLE_NAME, null, values);
    }

    @NonNull
    private static Response getResponse(boolean cond) {
        return cond
                ? ResponseFactory.negativeResponse()
                : ResponseFactory.positiveResponse();
    }

    @NonNull
    private static Note<String> getNoteFromCursor(@NonNull Cursor cursor) {
        Note<String> note = createNote(getNoteId(cursor), null);
        note.setTitle(getTitle(cursor));
        note.setCreation(getCreation(cursor));
        note.setLastUpdate(getLastUpdate(cursor));
        note.setContent(getContent(cursor));
        return note;
    }

    @NonNull
    private static Note<String> createNote(long id, GregorianCalendar now) {
        NoteData data = new NoteData();
        data.setId((int) id);
        data.setCreation(now);
        data.setLastUpdate(now);
        return new ConstId<String>(data);
    }

    @NonNull
    private static GregorianCalendar toGregorianCalendar(long creationTimestamp) {
        GregorianCalendar creationDate = new GregorianCalendar();
        creationDate.setTimeInMillis(creationTimestamp);
        return creationDate;
    }

    private static long getNoteId(@NonNull Cursor cursor) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(Notes.COL_ID));
    }

    private static String getContent(@NonNull Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(Notes.COL_CONTENT));
    }

    @NonNull
    private static GregorianCalendar getLastUpdate(@NonNull Cursor cursor) {
        int columnIndex = cursor.getColumnIndexOrThrow(Notes.COL_MODIFICATION_DATE);
        long timestamp = cursor.getLong(columnIndex);
        return toGregorianCalendar(timestamp);
    }

    @NonNull
    private static GregorianCalendar getCreation(@NonNull Cursor cursor) {
        int columnIndex = cursor.getColumnIndexOrThrow(Notes.COL_CREATION_DATE);
        long timestamp = cursor.getLong(columnIndex);
        return toGregorianCalendar(timestamp);
    }

    private static String getTitle(@NonNull Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(Notes.COL_TITLE));
    }

    @NonNull
    private static String getField(@NonNull OrderBy index) {
        switch (index) {
            case CREATION:    return Notes.COL_CREATION_DATE;
            case LAST_UPDATE: return Notes.COL_MODIFICATION_DATE;
            case TITLE:       return Notes.COL_TITLE;
            default:          throw new AssertionError("unexpected value");
        }
    }

    @NonNull
    private static String getOrder(@NonNull OrderType type) {
        switch (type) {
            case ASC: return "ASC";
            case DSC: return "DESC";
            default:  throw new AssertionError("unexpected value");
        }
    }
}
