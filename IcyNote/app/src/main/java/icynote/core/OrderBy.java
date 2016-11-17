package icynote.core;

/**
 * Enumerates the attributes by which a collection of notes may be sorted.
 *
 * @author Julien Harbulot
 * @version 1.0
 * @see icynote.core.IcyNoteCore#getNotes(OrderBy, OrderType)
 */
public enum OrderBy {
    TITLE {
        @Override
        public String toString() {
            return "TITLE";
        }
    },
    CREATION {
        @Override
        public String toString() {
            return "CREATION";
        }
    },
    LAST_UPDATE {
        @Override
        public String toString() {
            return "LAST_MOD";
        }
    };
    //TITLE, TITLE_DOWN, CREATION, LAST_UPDATE

    @Override
    public String toString() {
        return "TITLE";
    }
}
