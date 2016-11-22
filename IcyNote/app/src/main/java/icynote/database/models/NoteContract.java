package icynote.database.models;

/**
 * Structure of the database table for notes.
 *
 * @author Julien Harbulot
 * @author Romain Gehrig
 * @version 2.0
 */
public final class NoteContract {
    private NoteContract() {}

    // Provides the db fields as string constants
    public static final class Notes {
        public static final String TABLE_NAME = "note";
        public static final String COL_ID = "id";
        public static final String COL_USER_ID = "user";
        public static final String COL_TITLE = "title";
        public static final String COL_CREATION_DATE = "creation_date";
        public static final String COL_MODIFICATION_DATE = "modification_date";
        public static final String COL_CONTENT = "content";

        public static final String TYPE_ID = SqlTypes.INTEGER;
        public static final String TYPE_USER_ID = SqlTypes.TEXT;
        public static final String TYPE_TITLE = SqlTypes.TEXT;
        public static final String TYPE_CREATION_DATE = SqlTypes.DATE;
        public static final String TYPE_MODIFICATION_DATE = SqlTypes.DATE;
        public static final String TYPE_CONTENT = SqlTypes.TEXT;
    }
}
