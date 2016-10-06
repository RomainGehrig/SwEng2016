package ch.epfl.sweng.project.db.models;

import android.provider.BaseColumns;

public final class NoteContract {
    // Prevent from instantiating the class
    private NoteContract() {}

    // Provides the db fields as string constants
    public static final class Note implements BaseColumns {
        // Fields
        public static final String TABLE_NAME = "note";
        public static final String COL_TITLE = "title";
        public static final String COL_CREATION_DATE = "creationDate";
        public static final String COL_MODIFICATION_DATE = "modificationDate";
        public static final String COL_TEXT = "text"; // TODO: Rename COL_TEXT to COL_CONTENT for uniformity ?
    }
}
