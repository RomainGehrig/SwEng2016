package ch.epfl.sweng.project.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ch.epfl.sweng.project.db.models.NoteContract.Note;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Notes.db";
    // Tag used for the logging
    private static final String TAG = "DatabaseHelper";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Creating initial DB from DatabaseHelper");
        db.execSQL(SQL_CREATE_NOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: implement a migration policy instead of deleting and recreating the db
        Log.w(TAG, "Upgrading DB from version " + oldVersion + " to version " + newVersion + ".");
        db.execSQL(SQL_DELETE_NOTE_TABLE);
        onCreate(db);
    }

    // SQL queries
    private static final String TEXT_TYPE = " TEXT";
    private static final String DATE_TYPE = " DATE";
    private static final String COMMA_SEP = ", ";

    // CREATE TABLE(S)
    private static final String SQL_CREATE_NOTE_TABLE =
            "CREATE TABLE " + Note.TABLE_NAME + " (" +
                    Note._ID + " INTEGER PRIMARY KEY," +
                    Note.COL_TITLE + TEXT_TYPE + COMMA_SEP +
                    Note.COL_CREATION_DATE + DATE_TYPE + COMMA_SEP +
                    Note.COL_MODIFICATION_DATE + DATE_TYPE + COMMA_SEP +
                    Note.COL_TEXT + TEXT_TYPE + " )";


    // DELETE TABLE(S)
    private static final String SQL_DELETE_NOTE_TABLE =
            "DROP TABLE IF EXISTS " + Note.TABLE_NAME;

}
