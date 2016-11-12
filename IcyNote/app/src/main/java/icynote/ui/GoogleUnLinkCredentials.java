package icynote.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.Auth;

import icynote.login.LoginManagerFactory;

public class GoogleUnLinkCredentials extends GoogleClient {
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        super.onConnected(bundle);
        boolean success = LoginManagerFactory.getInstance().unlinkGoogleAccount();

        Intent data = new Intent();
        if (success) {
            Auth.GoogleSignInApi.signOut(apiClient());
            setResult(RESULT_OK, data);
        } else {
            setResult(RESULT_CANCELED, data);
        }
        finish();

    }
}
