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

import icynote.database.DatabaseProvider;
import icynote.database.models.NoteContract.Notes;
import icynote.note.Note;
import icynote.note.Response;
import icynote.note.common.ResponseFactory;
import icynote.note.decorators.ConstId;
import icynote.note.decorators.NullInput;
import icynote.note.impl.NoteData;
import icynote.noteproviders.NoteProvider;
import icynote.noteproviders.OrderBy;
import icynote.noteproviders.OrderType;
import util.Optional;

/**
 * Stores the notes in the local SQLite database.
 * The values stored in the database may never be null; this class corrects
 * null value with default value before persisting a note.
 *
 * <p>
 * Implementation note: we don't close the database connection.
 * See http://stackoverflow.com/questions/6608498/best-place-to-close-database-connection
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class SQLiteNoteProvider implements NoteProvider<Note<String>> {
    private static final String LOGGING_TAG = SQLiteNoteProvider.class.getSimpleName();

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

    // =====================================================================================
    // NoteProvider interface implementation

    @Override // NoteProvider interface
    public Optional<Note<String>> createNote() {
        GregorianCalendar now = new GregorianCalendar();
        long noteId = createEntryInNoteTable(now);
        Note<String> note = protect(createNoteData(noteId, now));
        return Optional.of(note);
    }

    @Override // NoteProvider interface
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

        if (cursor != null && cursor.moveToFirst()) try {
            result = Optional.of(protect(getNoteFromCursor(cursor)));
        } catch (RuntimeException e) {
            Log.e(LOGGING_TAG, e.getMessage());
            throw e;
        } finally {
            cursor.close();
        }

        return result;
    }

    @Override // NoteProvider interface
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
                    notes.add(protect(getNoteFromCursor(cursor)));
                }
            } catch (RuntimeException e) {
                Log.e(LOGGING_TAG, e.getMessage());
                throw e;
            } finally {
                    cursor.close();
            }

        return notes;
    }

    @Override // NoteProvider interface
    public Response persist(Note<String> n) {
        n = n.getRaw(); //unwind the decorator stack (if there is one)

        correctNullFields(n); //re - ensure non nullness of data in the database

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

    @Override // NoteProvider interface
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

    /**
     * Wraps the note in a decorator disallowing null input values.
     */
    @NonNull
    private static Note<String> protect(@NonNull Note<String> toProtect) {
        return new NullInput<>(toProtect);
    }

    // =====================================================================================
    // Database helper methods

    /**
     * Creates a new entry in the table for Notes
     *
     * @param now the date of creation of the note
     * @return the id of the newly created note
     */
    private long createEntryInNoteTable(@NonNull GregorianCalendar now) {
        SQLiteDatabase db = mDbProvider.getWritableDatabase();
        long nowMillis = now.getTimeInMillis();

        ContentValues values = new ContentValues();
        values.put(Notes.COL_USER_ID, mCurrentUserUID);
        values.put(Notes.COL_CREATION_DATE, nowMillis);
        values.put(Notes.COL_MODIFICATION_DATE, nowMillis);
        values.put(Notes.COL_CONTENT, "");
        values.put(Notes.COL_TITLE, "");
        return db.insertOrThrow(Notes.TABLE_NAME, null, values);
    }

    @NonNull
    private static Response getResponse(boolean cond) {
        return cond
                ? ResponseFactory.negativeResponse()
                : ResponseFactory.positiveResponse();
    }

    /**
     * Fetches the note currently pointed to by cursor;
     * the fields of the note are corrected with default value if they happen to be {@code null}.
     * @param cursor the cursor from which to fetch the note
     * @return a new note, fetched from the cursor at current position, containing no null fields.
     */
    @NonNull
    private static Note<String> getNoteFromCursor(@NonNull Cursor cursor) {
        Note<String> note = createNoteData(getNoteId(cursor), null);
        note.setTitle(getTitle(cursor));
        note.setCreation(getCreation(cursor));
        note.setLastUpdate(getLastUpdate(cursor));
        note.setContent(getContent(cursor));
        return correctNullFields(note);
    }

    /**
     * Creates a NoteData with immutable noteId set to {@code id}.
     * @param id the id of the note to create
     * @param now the date to which creation and lastUpdate should be initialized
     * @return the created NoteData wrapped in a ConstId proxy
     */
    @NonNull
    private static Note<String> createNoteData(long id, GregorianCalendar now) {
        now = (now != null) ? now : new GregorianCalendar();
        NoteData<String> data = new NoteData<String>("", "");
        data.setId((int) id);
        data.setCreation(now);
        data.setLastUpdate(now);
        return new ConstId<>(data);
    }

    /**
     * Replaces every field that is {@code null} by a default value and
     * returns the provided note after correction.
     */
    @NonNull
    private static Note<String> correctNullFields(@NonNull Note<String> toCorrect) {
        if (toCorrect.getCreation() == null) {
            Log.i(LOGGING_TAG, "correcting null creation date of note " + toCorrect.getId());
            toCorrect.setCreation(new GregorianCalendar());
        }
        if (toCorrect.getLastUpdate() == null) {
            Log.i(LOGGING_TAG, "correcting null update date of note " + toCorrect.getId());
            toCorrect.setLastUpdate(new GregorianCalendar());
        }
        if (toCorrect.getTitle() == null) {
            Log.i(LOGGING_TAG, "correcting null title of note " + toCorrect.getId());
            toCorrect.setTitle("");
        }
        if (toCorrect.getContent() == null) {
            Log.i(LOGGING_TAG, "correcting null content of note " + toCorrect.getId());
            toCorrect.setContent("");
        }
        return toCorrect;
    }

    @NonNull
    private static GregorianCalendar toGregorianCalendar(long creationTimestamp) {
        GregorianCalendar creationDate = new GregorianCalendar();
        creationDate.setTimeInMillis(creationTimestamp);
        return creationDate;
    }

    // =====================================================================================
    // Database's cursor getters methods


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
            case TITLE:       return Notes.COL_TITLE + "  COLLATE NOCASE ";
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
