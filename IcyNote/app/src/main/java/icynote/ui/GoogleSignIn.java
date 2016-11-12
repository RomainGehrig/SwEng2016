package icynote.ui;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import util.Callback2;

public class GoogleSignIn extends GoogleClient {
    private static final int RC_GOOGLE_SIGN_IN = 9001;

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

    void signInResult(GoogleSignInResult result) {
        Callback2<Boolean, String> callback = new Callback2<Boolean, String>() {
            @Override
            @SuppressWarnings("ParameterNameDiffersFromOverriddenParameter")
            public void execute(Boolean success, String errorMessage) {
                if (!success) {
                    if (errorMessage == null || errorMessage.equals("")) {
                        errorMessage = "Google Sign In cancelled";
                    }
                    Toast.makeText(GoogleSignIn.this, errorMessage, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(GoogleSignIn.this, LoginMenu.class));
                } else {
                    // Note: the onLoginSuccess callback will be notified. It handles the transition
                    //       to the next activity.
                    Toast.makeText(GoogleSignIn.this, "Google Sign In success", Toast.LENGTH_SHORT).show();
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
