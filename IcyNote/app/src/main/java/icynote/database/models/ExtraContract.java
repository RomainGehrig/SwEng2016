package icynote.database.models;

/**
 * Structure of the database table for extras.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public final class ExtraContract {
    private ExtraContract() {}

    public static final class Extra {
        public static final String TABLE_NAME = "extra";
        public static final String COL_ID = "id";
        public static final String COL_USER_ID = "user";
        public static final String COL_OWNER = "owner";
        public static final String COL_NAME = "name";
        public static final String COL_DATA = "data";

        public static final String TYPE_ID = SqlTypes.INTEGER;
        public static final String TYPE_USER_ID = SqlTypes.TEXT;
        public static final String TYPE_OWNER = SqlTypes.TEXT;
        public static final String TYPE_NAME = SqlTypes.TEXT;
        public static final String TYPE_DATA = SqlTypes.BLOB;
    }
}