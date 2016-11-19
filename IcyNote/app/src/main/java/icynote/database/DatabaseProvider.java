package icynote.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import icynote.database.models.NoteContract.Notes;

/**
 * SQLite database helper to be used to access to the database.
 *
 * @author Julien Harbulot
 * @author Romain Gehrig
 * @version 1.0
 */
public class DatabaseProvider extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Icynote.db";
    private static final String LOGGING_TAG = "DatabaseProvider";

    public DatabaseProvider(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(LOGGING_TAG, "Creating initial DB from DatabaseProvider");
        db.execSQL(SqlCreateTable.NOTE);
        /** TODO : table EXTRA */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*
        The migration policy depends on the previous database structure.
        Since this is version 1.0, we don't need to implement any.
         */
    }

    // SQL queries
    private static final String PRIMARY = " PRIMARY KEY";
    private static final String COMMA_SEP = ", ";

    private static final class SqlCreateTable {
        static final String NOTE =
                "CREATE TABLE " + Notes.TABLE_NAME +
                        " (" + Notes.COL_ID + Notes.TYPE_ID + PRIMARY +
                        COMMA_SEP + Notes.COL_USER_ID + Notes.TYPE_USER_ID +
                        COMMA_SEP + Notes.COL_TITLE + Notes.TYPE_TITLE +
                        COMMA_SEP + Notes.COL_CREATION_DATE + Notes.TYPE_CREATION_DATE +
                        COMMA_SEP + Notes.COL_MODIFICATION_DATE + Notes.TYPE_MODIFICATION_DATE +
                        COMMA_SEP + Notes.COL_CONTENT + Notes.TYPE_CONTENT +
                        " )";

        /** TODO : table EXTRA */

    }

    private static final class SqlDeleteTable {
        private static final String DROP = "DROP TABLE IF EXISTS ";
        static final String NOTE  = DROP + Notes.TABLE_NAME;

        /** TODO : table EXTRA */
    }
}
