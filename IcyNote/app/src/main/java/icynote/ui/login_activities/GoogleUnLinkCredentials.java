package icynote.ui.login_activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.Auth;

import icynote.login.LoginManagerFactory;

/**
 * The class to unlink Google credentials.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
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
