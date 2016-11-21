package icynote.extras;


/**
 * An {@code ExtraProviderFactory} provides or creates the {@ExtraProvider}
 * associated with a specific user.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public interface ExtraProviderFactory {
    /**
     * Fetches or create a provider for the unique user corresponding
     * to {@code userId}.
     * @param ownerId the unique if of an existing provider owner, such as a plugin.
     * @return the {@code ExtraProvider} associated with the specified user.
     */
    ExtraProvider getProvider(String ownerId);
}
