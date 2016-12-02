package icynote.ui.loginactivities;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import icynote.ui.R;
import util.Callback2;

public class GoogleLinkCredentials extends GoogleSignIn {
    @Override
    protected void signInResult(GoogleSignInResult result) {
        Callback2<Boolean, String> callback = new Callback2<Boolean, String>() {
            @Override
            @SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
            public void execute(Boolean success, String errorMessage) {
                Intent data = new Intent();
                if (!success) {
                    data.putExtra(getString(R.string.extra_google_link_message), errorMessage);
                } else {
                    data.putExtra(getString(R.string.extra_google_link_message), getString(R.string.sucess_google_link_credentials_put_extra));
                }
                setResult(RESULT_OK, data);
                finish();
            }
        };

        if (result.isSuccess()) {
            loginManager().linkCurrentUserWith(result.getSignInAccount(), callback);
            Log.d("GoogleLinkCredentials", "linking sent to loginManager");
        } else {
            callback.execute(false, result.getStatus().getStatusMessage());
        }
    }
}
