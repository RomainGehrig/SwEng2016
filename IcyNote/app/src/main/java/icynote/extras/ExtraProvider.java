package icynote.extras;

import util.Optional;

/**
 * An {@code ExtraProvider} provides and persists {@code Extra}.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public interface ExtraProvider {
    /**
     * Creates a new Extra with a unique id.
     */
    Optional<Extra> createExtra();

    /**
     * Fetches the unique Extra corresponding to the provided id.
     */
    Optional<Extra> getExtra(int extraId);

    /**
     * Fetches every Extra.
     */
    Iterable<Extra> getExtras();

    /**
     * Fetches every Extra associated with the provided name.
     */
    Iterable<Extra> getExtras(String name);

    /**
     * Persists an Extra and save every changes made to it.
     */
    void persist(Extra toPersist);
}
