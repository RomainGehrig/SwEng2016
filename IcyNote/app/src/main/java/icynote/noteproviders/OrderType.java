package icynote.noteproviders;

/**
 * Enumerates whether the order is ascending or descending.
 *
 * @author Julien Harbulot
 * @version 1.0
 * @see NoteProvider#getNotes(OrderBy, OrderType)
 */
public enum OrderType {
    ASC {
        @Override
        public String toString() {
            return "ASC";
        }
    },
    DSC {
        @Override
        public String toString() {
            return "DSC";
        }
    };

    @Override
    public String toString() {
        return "ASC";
    }
}
