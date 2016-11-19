package icynote.extras.list;


import java.util.HashMap;
import java.util.Map;

public class ProviderFactory implements icynote.extras.ExtraProviderFactory {
    private String mUserId;
    private Map<String, Provider> mProviders;

    public ProviderFactory(String userId) {
        mUserId = userId;
        mProviders = new HashMap<>();
    }

    public Provider getProvider(String ownerId) {
        if (!mProviders.containsKey(ownerId)) {
            mProviders.put(ownerId, new Provider(mUserId, ownerId));
        }
        return mProviders.get(ownerId);
    }
}
