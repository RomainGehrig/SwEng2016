package icynote.extras;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Simple {@code Extra} class that stores every field in memory.
 */
public class ExtraData implements Extra{
    private int mId;
    private String mOwnerId;
    private String mName;
    private Serializable mData;

    private static final String ERROR_UNABLE_TO_CRATE_EXTRA_WITHOUT_OWNER = "can't create extra without owner";
    private static final String ERROR_UNABLE_TO_COPY_EXTRA = "unable to copy extra";

    /**
     * @throws IllegalArgumentException if {@code ownerId} is {@code null}.
     * @param ownerId a non {@code null} string corresponding to a unique user id.
     * @param id the unique id of this {@code Extra}.
     */
    public ExtraData(@NonNull String ownerId, int id) {
        if (ownerId == null) {
            throw new IllegalArgumentException(ERROR_UNABLE_TO_CRATE_EXTRA_WITHOUT_OWNER);
        }
        mId = id;
        mOwnerId = ownerId;
    }

    /**
     * Performs a deep copy of the argument including its {@code data} field.
     * @param toDeepCopy an extra to deep copy
     * @throws RuntimeException if the deep copy of the {@code data} field fails
     * @throws NullPointerException if {@code toDeepCopy} is {@code null}
     */
    public ExtraData(@NonNull Extra toDeepCopy) {
        mId = toDeepCopy.getId();
        mOwnerId = toDeepCopy.getOwner();
        mName = toDeepCopy.getName();
        mData = deepClone(toDeepCopy.getData());
        if (mData == null) {
            throw new RuntimeException(ERROR_UNABLE_TO_COPY_EXTRA);
        }
    }

    public int getId() {
        return mId;
    }
    public @NonNull String getOwner() {
        return mOwnerId;
    }
    public @NonNull String getName() {
        return mName;
    }
    public @Nullable Serializable getData() {
        //todo: defensive copy ?
        return mData;
    }

    /**
     * Sets the name of {@code this} to the provided argument, or to the empty string
     * if it is {@code null}.
     * @param name the new name of {@code this}.
     */
    public void setName(@Nullable String name) {
        mName = (name == null) ? "" : name;
    }

    /**
     * Sets the data of {@code this} to the provided argument. {@code null} is allowed.
     * @param data possibly {@code null} data to attach to {@code this}.
     */
    public void setData(@Nullable Serializable data) {
        mData = data;
    }


    /**
     * Performs a deep copy of a {@code Serializable} object by serializing it
     * and deserializing the result into a new {@code Serializable}.
     * @param toClone the object to deep copy.
     * @return a deep copy of {@code toClone}.
     */
    private static @Nullable Serializable deepClone(@Nullable Serializable toClone) {
        if (toClone == null) {
            return null;
        }
        try {
            ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
            ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
            objectOutput.writeObject(toClone);

            ByteArrayInputStream byteInput = new ByteArrayInputStream(byteOutput.toByteArray());
            ObjectInputStream objectInput = new ObjectInputStream(byteInput);
            return (Serializable) objectInput.readObject();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
