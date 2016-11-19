package icynote.extras;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * An {@code Extra} allows plugins to store media and data.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public interface Extra {

    /**
     * Returns the unique id of this Extra.
     * @return the unique id of this Extra.
     */
    int getId();

    /**
     * Returns the unique id of this Extra's owner plugin.
     * @return the unique id of this Extra's owner plugin.
     */
    @NonNull String getOwner();

    /**
     * Returns the name associated with this Extra.
     * Multiple Extra may have the same name.
     * A name can be anything from the id of a note to a custom tag
     * used by the plugin to retrieve some data, such as "index".
     * @return the name of this Extra.
     */
    @NonNull String getName();

    /**
     * Returns the data attached to this extra.
     * @return the data attached to this extra.
     */
    @Nullable Serializable getData();

    /**
     * Changes the name associated with this Extra.
     * Multiple Extra may have the same name.
     * A name can be anything from the id of a note to a custom tag
     * used by the plugin to retrieve some data, such as "index".
     * If {@code name} is {@code null} then it will be replaced by the empty string.
     * @param name a possibly null name.
     */
    void setName(@Nullable String name);

    /**
     * Attaches data to this Extra.
     * @param data any Serializable object to attach to this extra. May be null.
     */
    void setData(@Nullable Serializable data);
}
