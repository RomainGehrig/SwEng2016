package icynote.ui.login_activities;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import icynote.ui.R;
import icynote.ui.login_activities.GoogleClient;
import util.Callback2;

/**
 * The class to sign in with google.
 *
 * @author Julien Harbulot
 * @version 1.0
 */
public class GoogleSignIn extends GoogleClient {
    /**
     * The constant RC_GOOGLE_SIGN_IN.
     */
    public static final int RC_GOOGLE_SIGN_IN = 9001;

    @Override
    public void onStart() {
        super.onStart();
        startSignIn();
    }

    private void startSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient());
        startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from signIn()
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            signInResult(result);
        }
    }

    /**
     * Sets the result of a google sign in.
     *
     * @param result
     */
    void signInResult(GoogleSignInResult result) {
        Callback2<Boolean, String> callback = new Callback2<Boolean, String>() {
            @Override
            @SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
            public void execute(Boolean success, String errorMessage) {
                if (!success) {
                    if (errorMessage == null || errorMessage.equals("")) {
                        errorMessage = getString(R.string.error_google_sign_in);
                    }
                    Toast.makeText(GoogleSignIn.this, errorMessage, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(GoogleSignIn.this, LoginMenu.class));
                } else {
                    // Note: the onLoginSuccess callback will be notified. It handles the transition
                    //       to the next activity.
                    Toast.makeText(GoogleSignIn.this, R.string.success_google_sign_in, Toast.LENGTH_SHORT).show();
                }
            }
        };

        if (result.isSuccess()) {
            loginManager().login(result.getSignInAccount(), callback);
            Log.d("GoogleSignIn", "GoogleSignInApi.signedIn");
        } else {
            callback.execute(false, result.getStatus().getStatusMessage());
        }
    }
}
