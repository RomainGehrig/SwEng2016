package icynote.core;

/**
 * Enumerates whether the order is ascending or descending.
 *
 * @author Julien Harbulot
 * @version 1.0
 * @see icynote.core.IcyNoteCore#getNotes(OrderBy, OrderType)
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
