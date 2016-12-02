package icynote.extras.list;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import icynote.extras.Extra;
import icynote.extras.ExtraData;
import icynote.extras.ExtraProvider;
import icynote.ui.R;
import util.Optional;

public class Provider implements ExtraProvider{
    private String mOwnerId;
    private List<ExtraData> mExtras;
    private int mNextId = 0;

    private static final String ERROR_EXTRA_UNKNOWN = "extra unknown with id ";
    private static final String ERROR_EXTRA_NULL = "null not allowed";

    public Provider(String userId, String ownerId) {
        mOwnerId = ownerId;
        mExtras = new ArrayList<>();
    }

    public Optional<Extra> createExtra() {
        ExtraData created = new ExtraData(mOwnerId, mNextId++);
        mExtras.add(defensiveCopy(created));
        return Optional.of((Extra)created);
    }

    @Override
    public Optional<Extra> getExtra(int extraId) {
        for(ExtraData e : mExtras) {
            if (e.getId() == extraId) {
                return Optional.of((Extra) defensiveCopy(e));
            }
        }
        return Optional.empty();
    }

    @Override
    public Iterable<Extra> getExtras() {
        List<Extra> copy = new ArrayList<>();
        for(Extra e : mExtras) {
            copy.add(defensiveCopy(e));
        }
        return copy;
    }

    /**
     * Gets all extras whose name are either {@code name} if non-{@code null}
     * or empty if {@code null}.
     */
    @Override
    public Iterable<Extra> getExtras(String name) {
        if (name == null) {
            name = "";
        }
        List<Extra> copy = new ArrayList<>();
        for(Extra e : mExtras) {
            if (e.getName().equals(name)) {
                copy.add(defensiveCopy(e)); //defensive copy
            }
        }
        return copy;
    }

    /**
     * @throws RuntimeException if {@code toPersist} wasn't created by {@code this} provider.
     * @throws IllegalArgumentException if {@code toPersist} is {@code null}.
     * @param toPersist an Extra created by {@code this} provider whose change we want to save.
     */
    @Override
    public void persist(Extra toPersist) {
        throwIfNull(toPersist);
        for(int i = 0; i < mExtras.size(); ++i) {
            if (mExtras.get(i).getId() == toPersist.getId()) {
                mExtras.set(i, defensiveCopy(toPersist));
                return;
            }
        }
        throw new RuntimeException(ERROR_EXTRA_UNKNOWN + toPersist.getId());
    }

    /**
     * @throws IllegalArgumentException if {@code toCheck} is {@code null}.
     */
    private void throwIfNull(Extra toCheck) {
        if (toCheck == null) {
            throw new IllegalArgumentException(ERROR_EXTRA_NULL);
        }
    }

    /**
     * Performs a defensive deep-copy of the argument.
     */
    @NonNull
    private ExtraData defensiveCopy(Extra e) {
        return new ExtraData(e);
    }
}
