/**
 * Based on a sample file from:
 * Copyright 2016 Google Inc. All Rights Reserved.
 * Apache 2.0 licence: http://www.apache.org/licenses/LICENSE-2.0
 *
 * Modifications:
 * 2016 IcyNote/Julien Harbulot
 *
 * - refactor class to extract Google Sign In related methods;
 * - adapts strings to IcyNote App;
 * - adds Google Drive Access;
 * - adds comment;
 * - uses icynote.login.LoginManager to turn the Google Account into an Icynote account.
 */

package icynote.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;

import icynote.login.LoginManager;
import icynote.login.LoginManagerFactory;


/**
 * This activity handles Google Sign In;
 * It is auto-managed by the Google API through the login process;
 * Once the login through Google is done, it logs the user into Icynote using the {@code LoginManager}.
 */
public class GoogleSignIn extends ActivityWithProgressDialog implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener
{
    private static final String TAG = "icynote.ui.Google";
    private static final int RC_SIGN_IN = 9001;
    private TextView mStatusTextView = null;
    private TextView mDetailTextView = null;

    /** Icynote user account manager. */
    private LoginManager loginManager = null;

    /** Client to go through Google Sign In process. */
    private GoogleApiClient mGoogleApiClient = null;

    //----------------------------------------------------------------------------------------
    // methods related to: UI / usual android activity view and onClick callbacks

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google);

        // Views
        mStatusTextView = (TextView) findViewById(R.id.status);
        mDetailTextView = (TextView) findViewById(R.id.detail);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);

        // Login initialization
        loginManager = LoginManagerFactory.getInstance();
        configureGoogleSignIn();
    }

    private void updateUINotLoggedIn() {
        hideProgressDialog();
        mStatusTextView.setText(R.string.signed_out);
        mDetailTextView.setText(null);

        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
        findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
    }

    private void updateUILoggedIn(String email, String userUID) {
        hideProgressDialog();
        mStatusTextView.setText(getString(R.string.google_status_fmt, email));
        mDetailTextView.setText(getString(R.string.icynote_status_fmt, userUID));

        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        } else if (i == R.id.sign_out_button) {
            signOut();
        } else if (i == R.id.disconnect_button) {
            revokeAccess();
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        loginManager.logout();

        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult( Status status) {
                        updateUINotLoggedIn();
                    }
                });
    }

    private void revokeAccess() {
        loginManager.logout();

        // Google revoke access
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult( Status status) {
                        updateUINotLoggedIn();
                    }
                });
    }

    //----------------------------------------------------------------------------------------
    // methods related to: Google API

    private void configureGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER)) //request for google drive
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(
                        this /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with the LoginManager
                GoogleSignInAccount account = result.getSignInAccount();
                icynoteAuthWithGoogle(account);
                /*
                 The login callback registered in the LoginManager will redirect
                 to the next activity. Nothing more to do here.
                 */
            } else {
                // Google Sign In failed, update UI appropriately
                updateUINotLoggedIn();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        log("onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error: unable to connect.", Toast.LENGTH_LONG).show();
    }

    //----------------------------------------------------------------------------------------
    // methods related to: Icynote login

    private void icynoteAuthWithGoogle(GoogleSignInAccount acct) {
        log("icynoteAuthWithGoogle:" + acct.getId());
        showProgressDialog();

        LoginManagerFactory.getInstance().login(acct,
                new LoginManager.Callback2<Boolean, String>() {
                    @Override
                    public void execute(Boolean successful, String errorMessage) {
                        log("signInWithCredential:onComplete:" + successful);

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!successful) {
                            log("signInWithCredential " + errorMessage);
                            Toast.makeText(GoogleSignIn.this, errorMessage,Toast.LENGTH_SHORT).show();
                        }
                        hideProgressDialog();
                    }
                });
    }

    //----------------------------------------------------------------------------------------
    // utility methods

    private void log(String message) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message);
        }
    }
}
